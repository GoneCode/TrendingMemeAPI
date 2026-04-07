package com.trending.api.model.dto.reddit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RedditResponse {

    private String kind;
    private RedditData data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RedditData {
        private List<RedditChild> children;
        private String after;
        private String before;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RedditChild {
        private String kind;
        private RedditPost data;
    }
}
