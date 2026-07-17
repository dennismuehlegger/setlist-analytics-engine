package com.dennismuehlegger.setlist_analytics_engine.service;

import com.dennismuehlegger.setlist_analytics_engine.dto.SongDTO;
import com.dennismuehlegger.setlist_analytics_engine.repository.SongRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class SongService {

    private final SongRepository songRepository;
    private final RestClient musicbrainzClient;

    public SongService(SongRepository songRepository, RestClient musicbrainzClient) {
        this.songRepository = songRepository;
        this.musicbrainzClient = musicbrainzClient;
    }

    public List<SongDTO> getMostPlayedSongs(String mbid, LocalDate startDate, LocalDate endDate) {
        try {
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Start date must be before end date.");
            }
            return songRepository.findMostPlayedSongs(mbid, startDate, endDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD.");
        }
    }

    public Integer fetchSongDuration(String mbid, String songName) {
        try {
            JsonNode response = musicbrainzClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("")
                            .queryParam("fmt", "json")
                            .queryParam("query", "arid:" + mbid + " AND recording:\"" + songName + "\"")
                            .build())
                    .retrieve()
                    .body(JsonNode.class);

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