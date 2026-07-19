package com.dennismuehlegger.setlist_analytics_engine.controller;

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

    @PostMapping("/{mbid}/import-duration")
    public ResponseEntity<Void> importSongDuration(@PathVariable String mbid) throws InterruptedException {
        musicBrainzService.loadSongDuration(mbid);
        return ResponseEntity.ok().build();
    }

}

