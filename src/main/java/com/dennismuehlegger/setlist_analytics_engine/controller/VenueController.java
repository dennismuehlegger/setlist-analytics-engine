package com.dennismuehlegger.setlist_analytics_engine.controller;

import com.dennismuehlegger.setlist_analytics_engine.dto.VenueDTO;
import com.dennismuehlegger.setlist_analytics_engine.service.VenueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venue")
public class VenueController {


    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @GetMapping("artist/{mbid}/top-venues")
    public ResponseEntity<List<VenueDTO>> getTopVenue(@PathVariable String mbid) {

        List<VenueDTO> topVenues = venueService.getTopVenues(mbid);
        return ResponseEntity.ok(topVenues);
    }
}
