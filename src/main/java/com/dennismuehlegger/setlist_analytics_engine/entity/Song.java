package com.dennismuehlegger.setlist_analytics_engine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "songs")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private int songOrder;

    private Integer durationMs;

    @ManyToOne
    @JoinColumn(name = "setlist_id", nullable = false)
    private Setlist setlist;

    public Song() {}
}
