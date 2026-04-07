package com.trending.api.model.dto.reddit;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RedditAuthResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private Long expiresIn;

    private String scope;
}
