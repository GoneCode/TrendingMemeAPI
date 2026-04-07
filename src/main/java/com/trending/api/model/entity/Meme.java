package com.trending.api.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "memes", indexes = {
        @Index(name = "idx_subreddit", columnList = "subreddit"),
        @Index(name = "idx_score", columnList = "score DESC"),
        @Index(name = "idx_created_utc", columnList = "created_utc DESC"),
        @Index(name = "idx_fetched_at", columnList = "fetched_at DESC")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reddit_id", unique = true, nullable = false, length = 50)
    private String redditId;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, length = 1000)
    private String url;

    @Column(name = "thumbnail_url", length = 1000)
    private String thumbnailUrl;

    @Column(nullable = false, length = 100)
    private String subreddit;

    @Column(length = 100)
    private String author;

    @Column(nullable = false)
    private Integer score = 0;

    @Column(name = "num_comments")
    private Integer numComments = 0;

    @Column(name = "created_utc", nullable = false)
    private LocalDateTime createdUtc;

    @Column(name = "fetched_at", nullable = false)
    private LocalDateTime fetchedAt;

    @Column(length = 500)
    private String permalink;

    @Column(name = "is_video")
    private Boolean isVideo = false;

    @PrePersist
    protected void onCreate() {
        if (fetchedAt == null) {
            fetchedAt = LocalDateTime.now();
        }
        if (score == null) {
            score = 0;
        }
        if (numComments == null) {
            numComments = 0;
        }
        if (isVideo == null) {
            isVideo = false;
        }
    }
}
