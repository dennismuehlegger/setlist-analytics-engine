package com.dennismuehlegger.setlist_analytics_engine.repository;

import com.dennismuehlegger.setlist_analytics_engine.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue, Long> {
}
