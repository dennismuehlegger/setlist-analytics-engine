package com.dennismuehlegger.setlist_analytics_engine.controller;

import com.dennismuehlegger.setlist_analytics_engine.dto.SongDTO;
import com.dennismuehlegger.setlist_analytics_engine.dto.SongRarityDTO;
import com.dennismuehlegger.setlist_analytics_engine.service.SongService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/setlist")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/artist/{mbid}/top-songs")
    public ResponseEntity<List<SongDTO>> getTopSongs(
            @PathVariable String mbid,
            @RequestParam("start") String startStr,
            @RequestParam("end") String endStr) {

        LocalDate startDate = LocalDate.parse(startStr);
        LocalDate endDate = LocalDate.parse(endStr);

        List<SongDTO> topSongs = songService.getTopSongs(mbid, startDate, endDate);
        return ResponseEntity.ok(topSongs);
    }

    @GetMapping("/artist/{mbid}/top-song")
    public ResponseEntity<SongDTO> getTopSong(
            @PathVariable String mbid,
            @RequestParam("year") Integer year) {
        SongDTO topSong = songService.getTopSong(mbid, year);
        return ResponseEntity.ok(topSong);
    }

    @GetMapping("/artist/{mbid}/song-rarity")
    public ResponseEntity<List<SongRarityDTO>> getSongRarity(
            @PathVariable String mbid,
            @RequestParam(value = "year", required = false) Integer year) {
        List<SongRarityDTO> topSong = songService.getSongRarity(mbid, year);
        return ResponseEntity.ok(topSong);
    }

}
