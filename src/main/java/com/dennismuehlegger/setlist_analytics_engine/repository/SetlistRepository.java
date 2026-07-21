package com.dennismuehlegger.setlist_analytics_engine.repository;

import com.dennismuehlegger.setlist_analytics_engine.entity.Setlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SetlistRepository extends JpaRepository<Setlist, String> {

    @Query("SELECT s FROM Setlist s WHERE s.artist.mbid = :mbid")
    List<Setlist> findByMbid(@Param("mbid") String mbid);

    @Query("SELECT s FROM Setlist s WHERE s.venue.id = :venueId")
    List<Setlist> findByVenueId(@Param("venueId") String venueId);
}
