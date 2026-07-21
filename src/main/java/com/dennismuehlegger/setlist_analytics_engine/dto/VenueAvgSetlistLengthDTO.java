package com.dennismuehlegger.setlist_analytics_engine.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VenueAvgSetlistLengthDTO {

    private String venueName;

    private String cityName;

    private String countryName;

    private Double avgDurationMinutes;

    private Double avgSongCount;

    public VenueAvgSetlistLengthDTO(Double avgSongCount, Double avgDurationMinutes, String countryName, String cityName, String venueName) {
        this.avgSongCount = avgSongCount;
        this.avgDurationMinutes = avgDurationMinutes;
        this.countryName = countryName;
        this.cityName = cityName;
        this.venueName = venueName;
    }
}
