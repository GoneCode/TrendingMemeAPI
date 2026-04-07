package com.trending.api.service;

import com.trending.api.exception.ResourceNotFoundException;
import com.trending.api.model.dto.newsapi.Article;
import com.trending.api.model.dto.reddit.RedditPost;
import com.trending.api.model.entity.News;
import com.trending.api.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    @Transactional
    public List<News> saveNewsFromReddit(List<RedditPost> redditPosts, String subreddit) {
        List<News> savedNews = new ArrayList<>();

        for (RedditPost post : redditPosts) {
            String externalId = "reddit_" + post.getId();
            if (!newsRepository.existsByExternalId(externalId)) {
                News news = convertRedditPostToNews(post, subreddit);
                News saved = newsRepository.save(news);
                savedNews.add(saved);
            } else {
                log.debug("News with external ID {} already exists, skipping", externalId);
            }
        }

        log.info("Saved {} new Reddit news items out of {} fetched from r/{}", savedNews.size(), redditPosts.size(), subreddit);
        return savedNews;
    }

    @Transactional
    public List<News> saveNewsFromNewsApi(List<Article> articles, String category) {
        List<News> savedNews = new ArrayList<>();

        for (Article article : articles) {
            String externalId = "newsapi_" + generateArticleId(article);
            if (!newsRepository.existsByExternalId(externalId)) {
                News news = convertArticleToNews(article, category);
                News saved = newsRepository.save(news);
                savedNews.add(saved);
            } else {
                log.debug("News with external ID {} already exists, skipping", externalId);
            }
        }

        log.info("Saved {} new News API items out of {} fetched for category: {}", savedNews.size(), articles.size(), category);
        return savedNews;
    }

    public Page<News> getTrendingNews(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findAllByOrderByPublishedAtDesc(pageable);
    }

    public Page<News> getNewsBySource(String source, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findBySourceOrderByPublishedAtDesc(source, pageable);
    }

    public Page<News> getNewsByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findByCategoryOrderByPublishedAtDesc(category, pageable);
    }

    public News getNewsById(Long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News not found with id: " + id));
    }

    @Transactional
    public void deleteOldNews(int retentionDays) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(retentionDays);
        newsRepository.deleteByFetchedAtBefore(cutoffDate);
        log.info("Deleted news older than {} days", retentionDays);
    }

    private News convertRedditPostToNews(RedditPost post, String subreddit) {
        News news = new News();
        news.setExternalId("reddit_" + post.getId());
        news.setSource("reddit");
        news.setTitle(post.getTitle());
        news.setUrl(post.getUrl());
        news.setImageUrl(post.getThumbnail());
        news.setAuthor(post.getAuthor());
        news.setSubreddit(subreddit);
        news.setScore(post.getScore() != null ? post.getScore() : 0);
        news.setNumComments(post.getNumComments() != null ? post.getNumComments() : 0);

        if (post.getCreatedUtc() != null) {
            news.setPublishedAt(LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(post.getCreatedUtc()),
                    ZoneId.systemDefault()
            ));
        } else {
            news.setPublishedAt(LocalDateTime.now());
        }

        return news;
    }

    private News convertArticleToNews(Article article, String category) {
        News news = new News();
        news.setExternalId("newsapi_" + generateArticleId(article));
        news.setSource("newsapi");
        news.setTitle(article.getTitle());
        news.setDescription(article.getDescription());
        news.setUrl(article.getUrl());
        news.setImageUrl(article.getUrlToImage());
        news.setAuthor(article.getAuthor());
        news.setCategory(category);

        if (article.getPublishedAt() != null) {
            try {
                news.setPublishedAt(LocalDateTime.parse(article.getPublishedAt(),
                        DateTimeFormatter.ISO_DATE_TIME));
            } catch (Exception e) {
                log.warn("Failed to parse publishedAt: {}", article.getPublishedAt());
                news.setPublishedAt(LocalDateTime.now());
            }
        } else {
            news.setPublishedAt(LocalDateTime.now());
        }

        return news;
    }

    private String generateArticleId(Article article) {
        return Integer.toHexString((article.getTitle() + article.getUrl()).hashCode());
    }
}
