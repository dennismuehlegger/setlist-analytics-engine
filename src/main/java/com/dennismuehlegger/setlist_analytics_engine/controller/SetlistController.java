package com.dennismuehlegger.setlist_analytics_engine.controller;


import com.dennismuehlegger.setlist_analytics_engine.dto.SetlistDTO;
import com.dennismuehlegger.setlist_analytics_engine.dto.SetlistDurationDTO;
import com.dennismuehlegger.setlist_analytics_engine.service.MusicBrainzService;
import com.dennismuehlegger.setlist_analytics_engine.service.SetlistService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/setlist")
public class SetlistController {

    private final SetlistService setlistService;

    private final MusicBrainzService musicBrainzService;

    public SetlistController(SetlistService setlistService, MusicBrainzService musicBrainzService) {
        this.setlistService = setlistService;
        this.musicBrainzService = musicBrainzService;
    }

    @GetMapping("{mbId}/songs")
    public ResponseEntity<List<SetlistDTO>> getSetlist(@PathVariable String mbId) {
        List<SetlistDTO> songs = setlistService.getSetlist(mbId);
        return ResponseEntity.ok(songs);
    }

    @GetMapping("/{setlistId}/duration")
    public ResponseEntity<SetlistDurationDTO> getSetlistLength(@PathVariable String setlistId) {
        SetlistDurationDTO setlistDuration = setlistService.getSetlistLength(setlistId);
        return ResponseEntity.ok(setlistDuration);
    }

    @PostMapping("/{mbId}/import")
    public ResponseEntity<Void> importArtist(@PathVariable String mbId) throws JsonProcessingException {
        setlistService.importArtist(mbId);
        return ResponseEntity.ok().build();
    }
}

