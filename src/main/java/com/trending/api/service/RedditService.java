package com.trending.api.service;

import com.trending.api.exception.ApiClientException;
import com.trending.api.model.dto.reddit.RedditAuthResponse;
import com.trending.api.model.dto.reddit.RedditPost;
import com.trending.api.model.dto.reddit.RedditResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
public class RedditService {

    private final WebClient redditWebClient;
    private final WebClient redditAuthWebClient;

    @Value("${reddit.api.client-id}")
    private String clientId;

    @Value("${reddit.api.client-secret}")
    private String clientSecret;

    @Value("${reddit.api.user-agent}")
    private String userAgent;

    private String accessToken;
    private long tokenExpiryTime = 0;

    public RedditService(@Qualifier("redditWebClient") WebClient redditWebClient,
                        @Qualifier("redditAuthWebClient") WebClient redditAuthWebClient) {
        this.redditWebClient = redditWebClient;
        this.redditAuthWebClient = redditAuthWebClient;
    }

    public List<RedditPost> fetchPostsFromSubreddit(String subreddit, int limit) {
        try {
            ensureValidToken();

            log.info("Fetching {} posts from r/{}", limit, subreddit);

            RedditResponse response = redditWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/r/{subreddit}/hot")
                            .queryParam("limit", limit)
                            .build(subreddit))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .header(HttpHeaders.USER_AGENT, userAgent)
                    .retrieve()
                    .bodyToMono(RedditResponse.class)
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                    .timeout(Duration.ofSeconds(10))
                    .block();

            if (response != null && response.getData() != null && response.getData().getChildren() != null) {
                List<RedditPost> posts = new ArrayList<>();
                for (RedditResponse.RedditChild child : response.getData().getChildren()) {
                    if (child.getData() != null) {
                        posts.add(child.getData());
                    }
                }
                log.info("Successfully fetched {} posts from r/{}", posts.size(), subreddit);
                return posts;
            }

            log.warn("No posts found for r/{}", subreddit);
            return new ArrayList<>();

        } catch (Exception e) {
            log.error("Error fetching posts from r/{}: {}", subreddit, e.getMessage());
            throw new ApiClientException("Failed to fetch posts from Reddit: " + e.getMessage(), e);
        }
    }

    private void ensureValidToken() {
        if (accessToken == null || System.currentTimeMillis() >= tokenExpiryTime) {
            log.info("Acquiring new Reddit access token");
            authenticate();
        }
    }

    private void authenticate() {
        try {
            String credentials = clientId + ":" + clientSecret;
            String encodedCredentials = Base64.getEncoder()
                    .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("grant_type", "client_credentials");

            RedditAuthResponse authResponse = redditAuthWebClient.post()
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + encodedCredentials)
                    .header(HttpHeaders.USER_AGENT, userAgent)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(RedditAuthResponse.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();

            if (authResponse != null && authResponse.getAccessToken() != null) {
                this.accessToken = authResponse.getAccessToken();
                this.tokenExpiryTime = System.currentTimeMillis() + (authResponse.getExpiresIn() * 1000) - 60000;
                log.info("Successfully authenticated with Reddit API");
            } else {
                throw new ApiClientException("Failed to obtain access token from Reddit");
            }

        } catch (Exception e) {
            log.error("Reddit authentication failed: {}", e.getMessage());
            throw new ApiClientException("Reddit authentication failed: " + e.getMessage(), e);
        }
    }
}
