package com.dennismuehlegger.setlist_analytics_engine.repository;

import com.dennismuehlegger.setlist_analytics_engine.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
}
