package com.dennismuehlegger.setlist_analytics_engine.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${setlistfm.api.key}")
    private String apiKey;

    @Bean("setlistClient")
    public RestClient setlistRestClient() {
        return RestClient.builder()
                .baseUrl("https://api.setlist.fm/rest/1.0")
                .defaultHeader("x-api-key", apiKey)
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean("musicbrainzClient")
    public RestClient musicbrainzRestClient() {
        return RestClient.builder()
                .baseUrl("https://musicbrainz.org/ws/2/recording")
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("User-Agent", "setlist-analytics-engine/1.0 muhleggerd@outlook.de")
                .build();
    }
}
