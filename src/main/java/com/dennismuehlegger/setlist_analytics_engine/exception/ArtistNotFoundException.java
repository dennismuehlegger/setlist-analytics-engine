package com.dennismuehlegger.setlist_analytics_engine.exception;

public class ArtistNotFoundException extends RuntimeException {
    public ArtistNotFoundException(String message){
        super(message);
    }
}
