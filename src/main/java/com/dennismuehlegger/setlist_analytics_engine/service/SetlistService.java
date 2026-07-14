package com.dennismuehlegger.setlist_analytics_engine.service;

import com.dennismuehlegger.setlist_analytics_engine.dto.SetlistDTO;
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

    public SetlistService(RestClient setlistRestClient) {
        this.setlistRestClient = setlistRestClient;
        this.objectMapper = new ObjectMapper();
    }

    public List<SetlistDTO> getSetlist(String mbid) {
        String rawJson = setlistRestClient.get()
                .uri("/artist/{mbid}/setlists", mbid)
                .retrieve()
                .body(String.class);

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
}