package com.dennismuehlegger.setlist_analytics_engine.service;

import com.dennismuehlegger.setlist_analytics_engine.dto.SetlistDTO;
import com.dennismuehlegger.setlist_analytics_engine.dto.SetlistDurationDTO;
import com.dennismuehlegger.setlist_analytics_engine.entity.Artist;
import com.dennismuehlegger.setlist_analytics_engine.entity.Setlist;
import com.dennismuehlegger.setlist_analytics_engine.entity.Song;
import com.dennismuehlegger.setlist_analytics_engine.entity.Venue;
import com.dennismuehlegger.setlist_analytics_engine.exception.SetlistNotFoundException;
import com.dennismuehlegger.setlist_analytics_engine.repository.ArtistRepository;
import com.dennismuehlegger.setlist_analytics_engine.repository.SetlistRepository;
import com.dennismuehlegger.setlist_analytics_engine.repository.SongRepository;
import com.dennismuehlegger.setlist_analytics_engine.repository.VenueRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class SetlistService {

    private final RestClient setlistRestClient;

    private final ObjectMapper objectMapper;

    private final ArtistRepository artistRepository;

    private final SetlistRepository setlistRepository;

    private final SongRepository songRepository;

    private final VenueRepository venueRepository;


    public SetlistService(@Qualifier("setlistClient") RestClient setlistRestClient, ArtistRepository artistRepository, SetlistRepository setlistRepository, SongRepository songRepository, VenueRepository venueRepository, SongService songService) {
        this.setlistRestClient = setlistRestClient;
        this.artistRepository = artistRepository;
        this.setlistRepository = setlistRepository;
        this.songRepository = songRepository;
        this.venueRepository = venueRepository;
        this.objectMapper = new ObjectMapper();
    }

    public List<SetlistDTO> getSetlist(String mbid) {
        String rawJson = retrieveSetlist(mbid);

        List<SetlistDTO> songList = new ArrayList<>();

        try {
            JsonNode response = objectMapper.readTree(rawJson);

            if (response != null && response.has("setlist")) {
                for (JsonNode setlistNode : response.get("setlist")) {
                    String eventDate = setlistNode.path("eventDate").asText();
                    String venueName = setlistNode.path("venue").path("name").asText();

                    JsonNode setsNode = setlistNode.path("sets").path("set");
                    for (JsonNode set : setsNode) {
                        JsonNode songsNode = set.path("song");
                        for (JsonNode song : songsNode) {
                            String songName = song.path("name").asText();
                            songList.add(new SetlistDTO(songName, eventDate, venueName));
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse setlist.fm response", e);
        }

        return songList;
    }

    public void importArtist(String mbid) throws JsonProcessingException, InterruptedException {
        String rawJson = retrieveSetlist(mbid);

        JsonNode response = objectMapper.readTree(rawJson);
        int total = response.get("total").asInt();
        int itemsPerPage = response.get("itemsPerPage").asInt();
        int totalPages = (int) Math.ceil((double) total / itemsPerPage);

        parseAndSave(response);

        for (int page = 2; page <= totalPages; page++) {
            String pageJson = setlistRestClient.get()
                    .uri("/artist/{mbid}/setlists?p={page}", mbid, page)
                    .retrieve()
                    .body(String.class);
            JsonNode pageNode = objectMapper.readTree(pageJson);
            parseAndSave(pageNode);
        }
    }

    public SetlistDurationDTO getSetlistLength(String setlistId) {
        Setlist setlist = setlistRepository.findById(setlistId)
                .orElseThrow(() -> new SetlistNotFoundException("Setlist not found: " + setlistId));
        List<Song> songs = setlist.getSongs();

        Integer totalDuration = songs.stream()
                .filter(s -> s.getDurationMs() != null)
                .mapToInt(Song::getDurationMs)
                .sum();

        return new SetlistDurationDTO(setlist.getVenue().getName(), setlist.getEventDate().toString(), totalDuration);
    }

    private void parseAndSave(JsonNode response) throws InterruptedException {
        Thread.sleep(1000);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        if (response == null || !response.has("setlist")) return;

        List<Song> songsToSave = new ArrayList<>();
        List<Setlist> setlistsToSave = new ArrayList<>();

        Set<String> processedArtists = new HashSet<>();
        Set<String> processedVenues = new HashSet<>();

        String mbid = "";

        for (JsonNode setlistNode : response.get("setlist")) {

            JsonNode artistNode = setlistNode.path("artist");
            mbid = artistNode.path("mbid").asText();
            Artist artist;
            if (processedArtists.contains(mbid)) {
                artist = artistRepository.getReferenceById(mbid);
            } else {
                String finalMbid = mbid;
                artist = artistRepository.findById(mbid).orElseGet(() -> {
                    Artist newArtist = new Artist();
                    newArtist.setMbid(finalMbid);
                    newArtist.setName(artistNode.path("name").asText());
                    return artistRepository.saveAndFlush(newArtist);
                });
                processedArtists.add(mbid);
            }

            JsonNode venueNode = setlistNode.path("venue");
            String venueId = venueNode.path("id").asText();
            Venue venue;
            if (processedVenues.contains(venueId)) {
                venue = venueRepository.getReferenceById(venueId);
            } else {
                venue = venueRepository.findById(venueId).orElseGet(() -> {
                    Venue newVenue = new Venue();
                    newVenue.setId(venueId);
                    newVenue.setName(venueNode.path("name").asText());
                    newVenue.setCity(venueNode.path("city").path("name").asText());
                    newVenue.setCountry(venueNode.path("city").path("country").path("name").asText());
                    return venueRepository.saveAndFlush(newVenue);
                });
                processedVenues.add(venueId);
            }


            String setlistId = setlistNode.path("id").asText();
            boolean isNew = setlistRepository.findById(setlistId).isEmpty();
            Setlist setlist = setlistRepository.findById(setlistId).orElseGet(() -> {
                Setlist newSetlist = new Setlist();
                newSetlist.setId(setlistId);
                String dateStr = setlistNode.path("eventDate").asText();
                if (dateStr != null && !dateStr.isEmpty()) {
                    newSetlist.setEventDate(LocalDate.parse(dateStr, formatter));
                }
                newSetlist.setTourName(setlistNode.path("tour").path("name").asText());
                newSetlist.setArtist(artist);
                newSetlist.setVenue(venue);
                return newSetlist;
            });

            if (isNew) {
                int globalOrder = 1;
                setlist.getSongs().clear();

                for (JsonNode setNode : setlistNode.path("sets").path("set")) {
                    for (JsonNode songNode : setNode.path("song")) {
                        Song song = new Song();
                        song.setSongOrder(globalOrder++);
                        song.setName(songNode.path("name").asText());
                        song.setSetlist(setlist);

                        setlist.getSongs().add(song);
                        songsToSave.add(song);
                    }
                }
                setlistsToSave.add(setlist);
            }

            setlistRepository.saveAll(setlistsToSave);
            songRepository.saveAll(songsToSave);
        }

    }

    public String retrieveSetlist(String mbid) {
        return setlistRestClient.get()
                .uri("/artist/{mbid}/setlists", mbid)
                .retrieve()
                .body(String.class);
    }
}