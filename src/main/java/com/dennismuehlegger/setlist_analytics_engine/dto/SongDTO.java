package com.dennismuehlegger.setlist_analytics_engine.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongDTO {

    private String songName;
    private Long playCount;

    public SongDTO(String songName, Long playCount) {
        this.songName = songName;
        this.playCount = playCount;
    }
}
