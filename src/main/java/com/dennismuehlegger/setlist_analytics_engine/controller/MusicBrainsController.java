package com.dennismuehlegger.setlist_analytics_engine.controller;

import com.dennismuehlegger.setlist_analytics_engine.dto.SetlistDurationDTO;
import com.dennismuehlegger.setlist_analytics_engine.service.MusicBrainzService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/musicBrainz")
public class MusicBrainsController {


    private final MusicBrainzService musicBrainzService;

    public MusicBrainsController(MusicBrainzService musicBrainzService) {
        this.musicBrainzService = musicBrainzService;
    }

    @PostMapping("/{mbId}/importDuration")
    public ResponseEntity<Void> importSongDuration(@PathVariable String mbId) throws InterruptedException {
        musicBrainzService.loadSongDuration(mbId);
        return ResponseEntity.ok().build();
    }

}

