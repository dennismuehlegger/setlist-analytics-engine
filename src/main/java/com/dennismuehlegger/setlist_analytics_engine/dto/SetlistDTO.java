package com.dennismuehlegger.setlist_analytics_engine.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetlistDTO {

    private String songName;

    private String eventDate;

    private String venueName;

    public SetlistDTO(String songName, String eventDate, String venueName) {
        this.songName = songName;
        this.eventDate = eventDate;
        this.venueName = venueName;
    }

}