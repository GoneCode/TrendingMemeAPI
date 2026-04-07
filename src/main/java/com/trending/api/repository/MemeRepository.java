package com.trending.api.repository;

import com.trending.api.model.entity.Meme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MemeRepository extends JpaRepository<Meme, Long> {

    Optional<Meme> findByRedditId(String redditId);

    Page<Meme> findAllByOrderByScoreDescCreatedUtcDesc(Pageable pageable);

    Page<Meme> findBySubredditOrderByScoreDescCreatedUtcDesc(String subreddit, Pageable pageable);

    void deleteByFetchedAtBefore(LocalDateTime cutoffDate);

    boolean existsByRedditId(String redditId);
}
