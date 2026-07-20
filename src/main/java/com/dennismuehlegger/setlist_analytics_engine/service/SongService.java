package com.dennismuehlegger.setlist_analytics_engine.service;

import com.dennismuehlegger.setlist_analytics_engine.dto.SongDTO;
import com.dennismuehlegger.setlist_analytics_engine.entity.Setlist;
import com.dennismuehlegger.setlist_analytics_engine.entity.Song;
import com.dennismuehlegger.setlist_analytics_engine.exception.SetlistDataNotFoundException;
import com.dennismuehlegger.setlist_analytics_engine.repository.SetlistRepository;
import com.dennismuehlegger.setlist_analytics_engine.repository.SongRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class SongService {

    private final SongRepository songRepository;

    private final SetlistRepository setlistRepository;

    public SongService(SongRepository songRepository, SetlistRepository setlistRepository) {
        this.songRepository = songRepository;
        this.setlistRepository = setlistRepository;
    }

    public List<SongDTO> getTopSongs(String mbid, LocalDate startDate, LocalDate endDate) {
        try {
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Start date must be before end date.");
            }
            return songRepository.findTopSongs(mbid, startDate, endDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD.");
        }
    }

    public SongDTO getTopSong(String mbid, Integer year){
        List<Setlist> allSetlists = setlistRepository.findArtistByMbid(mbid);
        List<Setlist> filteredSetlists = new ArrayList<>();
        Map<String, Integer> countSongOccurrences = new HashMap<>();

        for (Setlist setlist : allSetlists) {
            if (setlist.getEventDate().getYear() == year) {
                filteredSetlists.add(setlist);
            }
        }

        for (Setlist filteredSetlist : filteredSetlists) {
            List<Song> songList = filteredSetlist.getSongs();
            for (Song song : songList) {
                String songName = song.getName();
                countSongOccurrences.put(songName, countSongOccurrences.getOrDefault(songName, 0) + 1);
            }
        }

        Map.Entry<String, Integer> topSong = countSongOccurrences.entrySet().stream().max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new SetlistDataNotFoundException("No setlist data available"));

        return new SongDTO(topSong.getKey(), topSong.getValue());
    }
}