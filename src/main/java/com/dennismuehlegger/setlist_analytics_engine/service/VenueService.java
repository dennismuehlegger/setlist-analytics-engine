package com.dennismuehlegger.setlist_analytics_engine.service;

import com.dennismuehlegger.setlist_analytics_engine.dto.VenueDTO;
import com.dennismuehlegger.setlist_analytics_engine.entity.Setlist;
import com.dennismuehlegger.setlist_analytics_engine.entity.Venue;
import com.dennismuehlegger.setlist_analytics_engine.repository.SetlistRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VenueService {

    private final SetlistRepository setlistRepository;

    public VenueService(SetlistRepository setlistRepository) {
        this.setlistRepository = setlistRepository;
    }

    public List<VenueDTO> getTopVenues(String mbid) {
        List<Setlist> allSetlists = setlistRepository.findArtistByMbid(mbid);
        List<Venue> venueList = new ArrayList<>();
        Map<Venue, Integer> countVenueOccurrences = new HashMap<>();

        for (Setlist setlist : allSetlists) {
            venueList.add(setlist.getVenue());
        }
        for (Venue venue : venueList) {
            countVenueOccurrences.put(venue, countVenueOccurrences.getOrDefault(venue, 0) + 1);
        }

        return countVenueOccurrences.entrySet().stream()
                .filter(entry -> entry.getValue() >= 3)
                .sorted(Map.Entry.<Venue, Integer>comparingByValue().reversed())
                .map(entry -> new VenueDTO(entry.getKey().getName(), entry.getKey().getCity(), entry.getKey().getCountry(), entry.getValue()))
                .toList();
    }
}
