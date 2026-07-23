package com.dennismuehlegger.setlist_analytics_engine.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongRarityDTO {

    private String songName;

    private Integer totalAppearances;

    private Integer totalSetlists;

    private double rarityPercentage;

    public SongRarityDTO(String songName, Integer totalAppearances, Integer totalSetlists, double rarityPercentage) {
        this.songName = songName;
        this.totalAppearances = totalAppearances;
        this.totalSetlists = totalSetlists;
        this.rarityPercentage = rarityPercentage;
    }
}
