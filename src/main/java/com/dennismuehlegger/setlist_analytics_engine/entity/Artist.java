package com.dennismuehlegger.setlist_analytics_engine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "artists")
public class Artist {
    @Id
    private String mbid;
/*  thornhill: 368f19e5-51c0-45d7-80c5-963be6971ac7
    static dress: 9f61afe8-7142-44ee-b518-4dbb1caefb82
    loathe: 56eb02c4-1f16-4613-8bb3-b4a752283fc3
    slow crush: 7196f17e-a84b-4805-b623-7fc899f96334
    pinkpantheress: 7441014f-f8f5-494f-81db-ff166fbc078d*/


    @Column(nullable = false)
    private String name;

    private String disambiguation;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private List<Setlist> setlists;

    public Artist() {}
}
