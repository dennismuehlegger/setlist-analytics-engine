package com.dennismuehlegger.setlist_analytics_engine.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongDTO {

    private String songName;

    private Integer playCount;

    public SongDTO(String songName, Integer playCount) {
        this.songName = songName;
        this.playCount = playCount;
    }
}
