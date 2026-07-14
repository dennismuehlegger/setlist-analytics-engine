package com.dennismuehlegger.setlist_analytics_engine.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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

    public Venue() {}
}
