package com.dennismuehlegger.setlist_analytics_engine.config;


import com.dennismuehlegger.setlist_analytics_engine.service.SetlistService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializerConfig {

    @Bean
    @ConditionalOnProperty(name = "app.import-on-startup.enabled", havingValue = "true")
    CommandLineRunner importArtistOnStartup(SetlistService setlistService) {
        return args -> {
            String defaultMbid = "368f19e5-51c0-45d7-80c5-963be6971ac7";

            try {
                System.out.println("ARTIST IMPORTED AUTOMATICALLY");
                setlistService.importArtist(defaultMbid);
            } catch (Exception e) {
                System.out.println("error while importing artist on startup: " + e.getMessage());
            }
        };
    }
}