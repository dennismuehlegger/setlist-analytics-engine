package com.dennismuehlegger.setlist_analytics_engine.controller;

import com.dennismuehlegger.setlist_analytics_engine.dto.VenueAvgSetlistLengthDTO;
import com.dennismuehlegger.setlist_analytics_engine.dto.VenuePlayCountStatsDTO;
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
    public ResponseEntity<List<VenuePlayCountStatsDTO>> getTopVenue(@PathVariable String mbid) {

        List<VenuePlayCountStatsDTO> topVenues = venueService.getTopVenues(mbid);
        return ResponseEntity.ok(topVenues);
    }

    @GetMapping("/{venueid}/avg-length-per-venue")
    public ResponseEntity<VenueAvgSetlistLengthDTO> getAvgSetlistLengthPerVenue(@PathVariable String venueid) {

        VenueAvgSetlistLengthDTO venueSetlistLength = venueService.getAvgSetlistLengthPerVenue(venueid);
        return ResponseEntity.ok(venueSetlistLength);
    }
}
