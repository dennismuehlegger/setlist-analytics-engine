package com.dennismuehlegger.setlist_analytics_engine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "venues")
public class Venue {
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    private String city;

    private String country;

    @OneToMany(mappedBy = "venue")
    private List<Setlist> setlists;

    public Venue() {}
}
