package com.dennismuehlegger.setlist_analytics_engine.service;

import com.dennismuehlegger.setlist_analytics_engine.entity.Artist;
import com.dennismuehlegger.setlist_analytics_engine.entity.Setlist;
import com.dennismuehlegger.setlist_analytics_engine.entity.Song;
import com.dennismuehlegger.setlist_analytics_engine.exception.ArtistNotFoundException;
import com.dennismuehlegger.setlist_analytics_engine.repository.ArtistRepository;
import com.dennismuehlegger.setlist_analytics_engine.repository.SongRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MusicBrainzService {

    private final RestClient musicBrainzClient;

    private final ArtistRepository artistRepository;

    private final SongRepository songRepository;

    private final ObjectMapper objectMapper;

    public MusicBrainzService(@Qualifier("musicBrainzClient")RestClient musicBrainzClient, ArtistRepository artistRepository, SongRepository songRepository) {
        this.musicBrainzClient = musicBrainzClient;
        this.artistRepository = artistRepository;
        this.songRepository = songRepository;
        this.objectMapper = new ObjectMapper();
    }

    public void loadSongDuration(String mbId) throws InterruptedException {
        Artist artist = artistRepository.findById(mbId)
                .orElseThrow(() -> new ArtistNotFoundException("Artist not found: " + mbId));

        List<Setlist> setlist = artist.getSetlists();
        Set<String> uniqueSongs = new HashSet<>();

        for (Setlist uniqueSetlist : setlist) {
            List<Song> songList = uniqueSetlist.getSongs();
            for (Song song : songList) {
                if (song.getDurationMs() == null) {
                    uniqueSongs.add(song.getName());
                }
            }
        }

        for (String song : uniqueSongs) {
            songRepository.updateDurationForSongName(song, mbId, fetchSongDuration(mbId, song));
            Thread.sleep(2000);
        }
    }

    public Integer fetchSongDuration(String mbId, String songName){
        try {
            String rawResponse = musicBrainzClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("")
                            .queryParam("fmt", "json")
                            .queryParam("query", "arid:" + mbId + " AND recording:\"" + songName + "\"")
                            .build())
                    .retrieve()
                    .body(String.class);

            JsonNode response = objectMapper.readTree(rawResponse);

            if (response != null && response.has("recordings") && !response.path("recordings").isEmpty()) {
                JsonNode firstMatch = response.path("recordings").get(0);
                if (firstMatch.has("length")) {
                    return firstMatch.path("length").asInt();
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch duration for " + songName + ": " + e.getMessage());
        }

        return null;
    }
}
