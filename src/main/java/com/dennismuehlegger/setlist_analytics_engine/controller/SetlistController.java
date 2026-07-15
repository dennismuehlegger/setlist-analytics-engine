package com.dennismuehlegger.setlist_analytics_engine.controller;


import com.dennismuehlegger.setlist_analytics_engine.dto.SetlistDTO;
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

    @GetMapping("{mbId}/songs")
    public ResponseEntity<List<SetlistDTO>> getSetlist(@PathVariable String mbId) {
        List<SetlistDTO> songs = setlistService.getSetlist(mbId);
        return ResponseEntity.ok(songs);
    }

    @PostMapping("/{mbid}/import")
    public ResponseEntity<Void> importArtist(@PathVariable String mbid) throws JsonProcessingException {
        setlistService.importArtist(mbid);
        return ResponseEntity.ok().build();
    }
}

