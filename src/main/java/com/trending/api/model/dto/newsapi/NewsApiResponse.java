package com.trending.api.model.dto.newsapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsApiResponse {

    private String status;

    @JsonProperty("totalResults")
    private Integer totalResults;

    private List<Article> articles;
}
