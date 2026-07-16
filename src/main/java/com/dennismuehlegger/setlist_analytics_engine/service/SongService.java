package com.dennismuehlegger.setlist_analytics_engine.service;

import com.dennismuehlegger.setlist_analytics_engine.dto.SongDTO;
import com.dennismuehlegger.setlist_analytics_engine.repository.SongRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class SongService {

    private final SongRepository songRepository;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
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
}
