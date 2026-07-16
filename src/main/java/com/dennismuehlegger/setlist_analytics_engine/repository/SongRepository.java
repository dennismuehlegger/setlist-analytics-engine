package com.dennismuehlegger.setlist_analytics_engine.repository;

import com.dennismuehlegger.setlist_analytics_engine.dto.SongDTO;
import com.dennismuehlegger.setlist_analytics_engine.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    @Query("SELECT s.name AS songName, COUNT(s) AS playCount " +
            "FROM Song s " +
            "JOIN s.setlist sl " +
            "WHERE sl.artist.mbid = :mbid " +
            "AND sl.eventDate BETWEEN :startDate AND :endDate " +
            "GROUP BY s.name " +
            "ORDER BY COUNT(s) DESC")
    List<SongDTO> findMostPlayedSongs(
            @Param("mbid") String mbid,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
