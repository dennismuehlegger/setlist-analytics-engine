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
        Map<String, Integer> countSongOccurrence = new HashMap<>();

        for (Setlist setlist : allSetlists) {
            if (setlist.getEventDate().getYear() == year) {
                filteredSetlists.add(setlist);
            }
        }

        for (Setlist filteredSetlist : filteredSetlists) {
            List<Song> songList = filteredSetlist.getSongs();
            for (Song song : songList) {
                String songName = song.getName();
                countSongOccurrence.put(songName, countSongOccurrence.getOrDefault(songName, 0) + 1);
            }
        }
        System.out.println("Total setlists: " + allSetlists.size());
        System.out.println("Filtered setlists for year " + year + ": " + filteredSetlists.size());
        Map.Entry<String, Integer> topSong = countSongOccurrence.entrySet().stream().max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new SetlistDataNotFoundException("No setlist data available"));

        return new SongDTO(topSong.getKey(), topSong.getValue());
    }
}