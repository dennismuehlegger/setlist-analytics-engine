package com.dennismuehlegger.setlist_analytics_engine.controller;

import com.dennismuehlegger.setlist_analytics_engine.dto.SongDTO;
import com.dennismuehlegger.setlist_analytics_engine.service.SongService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/setlist")
public class SongController {

    private SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/artist/{mbid}/topsongs")
    public ResponseEntity<List<SongDTO>> getTopSongs(
            @PathVariable String mbid,
            @RequestParam("start") String startStr,
            @RequestParam("end") String endStr) {

        LocalDate startDate = LocalDate.parse(startStr);
        LocalDate endDate = LocalDate.parse(endStr);

        List<SongDTO> topSongs = songService.getMostPlayedSongs(mbid, startDate, endDate);
        return ResponseEntity.ok(topSongs);
    }

}
