package com.dennismuehlegger.setlist_analytics_engine.service;

import com.dennismuehlegger.setlist_analytics_engine.dto.SetlistDTO;
import com.dennismuehlegger.setlist_analytics_engine.entity.Artist;
import com.dennismuehlegger.setlist_analytics_engine.entity.Setlist;
import com.dennismuehlegger.setlist_analytics_engine.entity.Song;
import com.dennismuehlegger.setlist_analytics_engine.entity.Venue;
import com.dennismuehlegger.setlist_analytics_engine.repository.ArtistRepository;
import com.dennismuehlegger.setlist_analytics_engine.repository.SetlistRepository;
import com.dennismuehlegger.setlist_analytics_engine.repository.SongRepository;
import com.dennismuehlegger.setlist_analytics_engine.repository.VenueRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetlistService {

    private final RestClient setlistRestClient;

    private final ObjectMapper objectMapper;

    private final ArtistRepository artistRepository;

    private final SetlistRepository setlistRepository;

    private final SongRepository songRepository;

    private final VenueRepository venueRepository;

    public SetlistService(RestClient setlistRestClient, ArtistRepository artistRepository, SetlistRepository setlistRepository, SongRepository songRepository, VenueRepository venueRepository) {
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

    public void importArtist(String mbid) throws JsonProcessingException {
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

    public void parseAndSave(JsonNode response){
        if (response != null && response.has("setlist")){
            for (JsonNode setlistNode : response.get("setlist")){
                JsonNode artistNode = setlistNode.path("artist");
                Artist artist = artistRepository.findById(artistNode.path("mbid").asText())
                        .orElseGet(() -> {
                            Artist newArtist = new Artist();
                            newArtist.setMbid(artistNode.path("mbid").asText());
                            newArtist.setName(artistNode.path("name").asText());
                            return artistRepository.save(newArtist);
                        });

                JsonNode venueNode = setlistNode.path("venue");
                Venue venue = venueRepository.findById(venueNode.path("id").asText())
                        .orElseGet(() -> {
                            Venue newVenue = new Venue();
                            newVenue.setId(venueNode.path("id").asText());
                            newVenue.setName(venueNode.path("name").asText());
                            newVenue.setCity(venueNode.path("city").path("name").asText());
                            newVenue.setCountry(venueNode.path("city").path("country").path("name").asText());
                            return venueRepository.save(newVenue);
                        });

                Setlist setlist = setlistRepository.findById(setlistNode.path("id").asText())
                        .orElseGet(() -> {
                            Setlist newSetlist = new Setlist();
                            newSetlist.setId(setlistNode.path("id").asText());
                            newSetlist.setEventDate(setlistNode.path("eventDate").asText());
                            newSetlist.setTourName(setlistNode.path("tour").path("name").asText());
                            newSetlist.setArtist(artist);
                            newSetlist.setVenue(venue);
                            return setlistRepository.save(newSetlist);
                        });

                for (JsonNode setNode : setlistNode.path("sets").path("set")) {
                    int order = 1;
                    for (JsonNode songNode : setNode.path("song")) {
                        Song song = new Song();
                        song.setSongOrder(order++);
                        song.setName(songNode.path("name").asText());
                        song.setSetlist(setlist);
                        songRepository.save(song);
                    }
                }
            }
        }
    }

    public String retrieveSetlist(String mbid){
        return setlistRestClient.get()
                .uri("/artist/{mbid}/setlists", mbid)
                .retrieve()
                .body(String.class);
    }
}