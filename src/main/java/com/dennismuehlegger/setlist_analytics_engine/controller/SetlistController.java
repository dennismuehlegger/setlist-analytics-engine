package com.dennismuehlegger.setlist_analytics_engine.controller;


import com.dennismuehlegger.setlist_analytics_engine.dto.SetlistDTO;
import com.dennismuehlegger.setlist_analytics_engine.dto.SetlistDurationDTO;
import com.dennismuehlegger.setlist_analytics_engine.service.SetlistService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/setlist")
public class SetlistController {

    private final SetlistService setlistService;

    public SetlistController(SetlistService setlistService) {
        this.setlistService = setlistService;
    }

    @GetMapping("{mbid}/songs")
    public ResponseEntity<List<SetlistDTO>> getSetlist(@PathVariable String mbid) {
        List<SetlistDTO> songs = setlistService.getSetlist(mbid);
        return ResponseEntity.ok(songs);
    }

    @GetMapping("/{setlistId}/duration")
    public ResponseEntity<SetlistDurationDTO> getSetlistLength(@PathVariable String setlistId) {
        SetlistDurationDTO setlistDuration = setlistService.getSetlistLength(setlistId);
        return ResponseEntity.ok(setlistDuration);
    }

    @PostMapping("/{mbid}/import")
    public ResponseEntity<Void> importArtist(@PathVariable String mbid) throws JsonProcessingException, InterruptedException {
        setlistService.importArtist(mbid);
        return ResponseEntity.ok().build();
    }
}

