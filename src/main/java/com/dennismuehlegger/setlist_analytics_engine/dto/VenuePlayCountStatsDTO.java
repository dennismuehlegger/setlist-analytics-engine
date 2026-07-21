package com.dennismuehlegger.setlist_analytics_engine.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VenuePlayCountStatsDTO {

    private String venueName;

    private String cityName;

    private String countryName;

    private Integer playCount;

    public VenuePlayCountStatsDTO(String venueName, String cityName, String countryName, Integer playCount) {
        this.venueName = venueName;
        this.cityName = cityName;
        this.countryName = countryName;
        this.playCount = playCount;
    }
}
