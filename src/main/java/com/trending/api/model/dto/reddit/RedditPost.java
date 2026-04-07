package com.trending.api.model.dto.reddit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RedditPost {

    private String id;
    private String title;
    private String url;
    private String thumbnail;
    private String subreddit;
    private String author;
    private Integer score;

    @JsonProperty("num_comments")
    private Integer numComments;

    @JsonProperty("created_utc")
    private Long createdUtc;

    private String permalink;

    @JsonProperty("is_video")
    private Boolean isVideo;

    @JsonProperty("post_hint")
    private String postHint;
}
