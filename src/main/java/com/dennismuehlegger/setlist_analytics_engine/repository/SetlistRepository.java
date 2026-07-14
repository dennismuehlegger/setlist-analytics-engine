package com.dennismuehlegger.setlist_analytics_engine.repository;

import com.dennismuehlegger.setlist_analytics_engine.entity.Setlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SetlistRepository extends JpaRepository<Setlist, Long> {
}
