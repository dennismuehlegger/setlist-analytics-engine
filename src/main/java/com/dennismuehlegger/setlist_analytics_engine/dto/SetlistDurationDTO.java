package com.dennismuehlegger.setlist_analytics_engine.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetlistDurationDTO {

    private String venueName;

    private String eventDate;

    private Integer setlistDuration;

    public SetlistDurationDTO(String venueName, String eventDate, Integer setlistDuration) {
        this.venueName = venueName;
        this.eventDate = eventDate;
        this.setlistDuration = setlistDuration;
    }
}
