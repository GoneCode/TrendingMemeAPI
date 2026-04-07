package com.trending.api.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "news", indexes = {
        @Index(name = "idx_source", columnList = "source"),
        @Index(name = "idx_published_at", columnList = "published_at DESC"),
        @Index(name = "idx_category", columnList = "category"),
        @Index(name = "idx_subreddit", columnList = "subreddit")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", unique = true, nullable = false, length = 100)
    private String externalId;

    @Column(nullable = false, length = 100)
    private String source;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 1000)
    private String url;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @Column(length = 200)
    private String author;

    @Column(name = "published_at", nullable = false)
    private LocalDateTime publishedAt;

    @Column(name = "fetched_at", nullable = false)
    private LocalDateTime fetchedAt;

    @Column(length = 50)
    private String category;

    @Column(length = 100)
    private String subreddit;

    @Column
    private Integer score = 0;

    @Column(name = "num_comments")
    private Integer numComments = 0;

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
    }
}
