package com.trending.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Configuration
public class ApiClientConfig {

    @Value("${reddit.api.base-url}")
    private String redditBaseUrl;

    @Value("${reddit.api.timeout}")
    private int redditTimeout;

    @Value("${newsapi.api.base-url}")
    private String newsApiBaseUrl;

    @Value("${newsapi.api.timeout}")
    private int newsApiTimeout;

    @Bean(name = "redditWebClient")
    public WebClient redditWebClient() {
        return WebClient.builder()
                .baseUrl(redditBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();
    }

    @Bean(name = "newsApiWebClient")
    public WebClient newsApiWebClient() {
        return WebClient.builder()
                .baseUrl(newsApiBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();
    }

    @Bean(name = "redditAuthWebClient")
    public WebClient redditAuthWebClient(@Value("${reddit.api.auth-url}") String authUrl) {
        return WebClient.builder()
                .baseUrl(authUrl)
                .build();
    }
}
