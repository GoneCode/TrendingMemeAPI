package com.trending.api.controller;

import com.trending.api.model.entity.Meme;
import com.trending.api.service.MemeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/memes")
public class MemeController {

    private static final Logger log = LoggerFactory.getLogger(MemeController.class);
    private final MemeService memeService;

    public MemeController(MemeService memeService) {
        this.memeService = memeService;
    }

    @GetMapping("/trending")
    public ResponseEntity<Page<Meme>> getTrendingMemes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {

        log.info("GET /api/v1/memes/trending - page: {}, size: {}", page, size);

        Page<Meme> memes = memeService.getTrendingMemes(page, size);
        return ResponseEntity.ok(memes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meme> getMemeById(@PathVariable Long id) {
        log.info("GET /api/v1/memes/{}", id);

        Meme meme = memeService.getMemeById(id);
        return ResponseEntity.ok(meme);
    }

    @GetMapping("/subreddit/{subreddit}")
    public ResponseEntity<Page<Meme>> getMemesBySubreddit(
            @PathVariable String subreddit,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {

        log.info("GET /api/v1/memes/subreddit/{} - page: {}, size: {}", subreddit, page, size);

        Page<Meme> memes = memeService.getMemesBySubreddit(subreddit, page, size);
        return ResponseEntity.ok(memes);
    }
}
