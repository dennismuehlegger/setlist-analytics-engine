package com.dennismuehlegger.setlist_analytics_engine.controller;


import com.dennismuehlegger.setlist_analytics_engine.dto.SetlistDTO;
import com.dennismuehlegger.setlist_analytics_engine.service.SetlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

