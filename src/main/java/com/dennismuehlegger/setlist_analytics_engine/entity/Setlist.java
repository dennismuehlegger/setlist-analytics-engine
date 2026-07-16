package com.dennismuehlegger.setlist_analytics_engine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "setlists")
public class Setlist {
    @Id
    private String id;

    private LocalDate eventDate;

    private String tourName;

    @ManyToOne(cascade = CascadeType.ALL)
    private Artist artist;

    @ManyToOne(cascade = CascadeType.ALL)
    private Venue venue;

    @OneToMany(mappedBy = "setlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Song> songs = new ArrayList<>();

    public Setlist() {}
}

