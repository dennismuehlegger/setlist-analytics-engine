package com.dennismuehlegger.setlist_analytics_engine.repository;

import com.dennismuehlegger.setlist_analytics_engine.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {
}
