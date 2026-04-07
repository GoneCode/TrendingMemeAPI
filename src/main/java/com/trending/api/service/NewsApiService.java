package com.trending.api.service;

import com.trending.api.exception.ApiClientException;
import com.trending.api.model.dto.newsapi.Article;
import com.trending.api.model.dto.newsapi.NewsApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class NewsApiService {

    private final WebClient newsApiWebClient;

    @Value("${newsapi.api.api-key}")
    private String apiKey;

    public NewsApiService(@Qualifier("newsApiWebClient") WebClient newsApiWebClient) {
        this.newsApiWebClient = newsApiWebClient;
    }

    public List<Article> fetchTopHeadlines(String category, int pageSize) {
        try {
            log.info("Fetching top {} headlines for category: {}", pageSize, category);

            NewsApiResponse response = newsApiWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/top-headlines")
                            .queryParam("category", category)
                            .queryParam("pageSize", pageSize)
                            .queryParam("apiKey", apiKey)
                            .queryParam("language", "en")
                            .build())
                    .retrieve()
                    .bodyToMono(NewsApiResponse.class)
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                    .timeout(Duration.ofSeconds(10))
                    .block();

            if (response != null && "ok".equalsIgnoreCase(response.getStatus()) && response.getArticles() != null) {
                log.info("Successfully fetched {} articles for category: {}", response.getArticles().size(), category);
                return response.getArticles();
            }

            log.warn("No articles found for category: {}", category);
            return new ArrayList<>();

        } catch (Exception e) {
            log.error("Error fetching news from News API for category {}: {}", category, e.getMessage());
            throw new ApiClientException("Failed to fetch news from News API: " + e.getMessage(), e);
        }
    }

    public List<Article> fetchTopHeadlinesAllCategories(List<String> categories, int pageSize) {
        List<Article> allArticles = new ArrayList<>();

        for (String category : categories) {
            try {
                List<Article> articles = fetchTopHeadlines(category, pageSize);
                allArticles.addAll(articles);
            } catch (Exception e) {
                log.error("Failed to fetch headlines for category {}: {}", category, e.getMessage());
            }
        }

        return allArticles;
    }
}
