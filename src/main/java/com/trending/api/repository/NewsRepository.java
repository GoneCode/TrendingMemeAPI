package com.trending.api.repository;

import com.trending.api.model.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    Optional<News> findByExternalId(String externalId);

    Page<News> findAllByOrderByPublishedAtDesc(Pageable pageable);

    Page<News> findBySourceOrderByPublishedAtDesc(String source, Pageable pageable);

    Page<News> findByCategoryOrderByPublishedAtDesc(String category, Pageable pageable);

    void deleteByFetchedAtBefore(LocalDateTime cutoffDate);

    boolean existsByExternalId(String externalId);
}
