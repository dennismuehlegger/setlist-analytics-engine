package com.dennismuehlegger.setlist_analytics_engine.service;

import com.dennismuehlegger.setlist_analytics_engine.dto.VenueAvgSetlistLengthDTO;
import com.dennismuehlegger.setlist_analytics_engine.dto.VenuePlayCountStatsDTO;
import com.dennismuehlegger.setlist_analytics_engine.entity.Setlist;
import com.dennismuehlegger.setlist_analytics_engine.entity.Song;
import com.dennismuehlegger.setlist_analytics_engine.entity.Venue;
import com.dennismuehlegger.setlist_analytics_engine.repository.SetlistRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VenueService {

    private final SetlistRepository setlistRepository;

    public VenueService(SetlistRepository setlistRepository) {
        this.setlistRepository = setlistRepository;
    }

    public List<VenuePlayCountStatsDTO> getTopVenues(String mbid) {
        List<Setlist> allSetlists = setlistRepository.findByMbid(mbid);
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
                .map(entry -> new VenuePlayCountStatsDTO(entry.getKey().getName(), entry.getKey().getCity(), entry.getKey().getCountry(), entry.getValue()))
                .toList();
    }

    public VenueAvgSetlistLengthDTO getAvgSetlistLengthPerVenue(String venueid){
        List<Setlist> allSetlists = setlistRepository.findByVenueId(venueid);
        Venue venue = allSetlists.get(0).getVenue();

        double avgSetlistSongCount = allSetlists.stream()
                .mapToInt(setlist -> setlist.getSongs().size()).average().orElse(0.0);
        double avgSetlistDuration = allSetlists.stream()
                .mapToInt(setlist -> setlist.getSongs().stream()
                        .filter(song -> song.getDurationMs() != null)
                        .mapToInt(Song::getDurationMs).sum()).filter(totalMs -> totalMs > 0).average().orElse(0.0) / 60000.0;

        Double avgSetlistSongCountRounded = Math.round(avgSetlistSongCount * 100) / 100.0;
        Double avgSetlistLengthRounded = Math.round(avgSetlistDuration * 100) / 100.0;

        return new VenueAvgSetlistLengthDTO(avgSetlistSongCountRounded, avgSetlistLengthRounded, venue.getCountry(), venue.getCity(), venue.getName());
    }
}
