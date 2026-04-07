package com.trending.api.controller;

import com.trending.api.model.entity.News;
import com.trending.api.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {

    private static final Logger log = LoggerFactory.getLogger(NewsController.class);
    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/trending")
    public ResponseEntity<Page<News>> getTrendingNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam(required = false) String source) {

        log.info("GET /api/v1/news/trending - page: {}, size: {}, source: {}", page, size, source);

        Page<News> news;
        if (source != null && !source.isEmpty()) {
            news = newsService.getNewsBySource(source, page, size);
        } else {
            news = newsService.getTrendingNews(page, size);
        }

        return ResponseEntity.ok(news);
    }

    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable Long id) {
        log.info("GET /api/v1/news/{}", id);

        News news = newsService.getNewsById(id);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<News>> getNewsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {

        log.info("GET /api/v1/news/category/{} - page: {}, size: {}", category, page, size);

        Page<News> news = newsService.getNewsByCategory(category, page, size);
        return ResponseEntity.ok(news);
    }
}
