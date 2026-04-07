package com.trending.api.service;

import com.trending.api.exception.ResourceNotFoundException;
import com.trending.api.model.dto.reddit.RedditPost;
import com.trending.api.model.entity.Meme;
import com.trending.api.repository.MemeRepository;
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
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemeService {

    private final MemeRepository memeRepository;

    @Transactional
    public List<Meme> saveMemes(List<RedditPost> redditPosts) {
        List<Meme> savedMemes = new ArrayList<>();

        for (RedditPost post : redditPosts) {
            if (!memeRepository.existsByRedditId(post.getId())) {
                Meme meme = convertToMeme(post);
                Meme saved = memeRepository.save(meme);
                savedMemes.add(saved);
            } else {
                log.debug("Meme with Reddit ID {} already exists, skipping", post.getId());
            }
        }

        log.info("Saved {} new memes out of {} fetched", savedMemes.size(), redditPosts.size());
        return savedMemes;
    }

    public Page<Meme> getTrendingMemes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return memeRepository.findAllByOrderByScoreDescCreatedUtcDesc(pageable);
    }

    public Page<Meme> getMemesBySubreddit(String subreddit, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return memeRepository.findBySubredditOrderByScoreDescCreatedUtcDesc(subreddit, pageable);
    }

    public Meme getMemeById(Long id) {
        return memeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meme not found with id: " + id));
    }

    @Transactional
    public void deleteOldMemes(int retentionDays) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(retentionDays);
        memeRepository.deleteByFetchedAtBefore(cutoffDate);
        log.info("Deleted memes older than {} days", retentionDays);
    }

    private Meme convertToMeme(RedditPost post) {
        Meme meme = new Meme();
        meme.setRedditId(post.getId());
        meme.setTitle(post.getTitle());
        meme.setUrl(post.getUrl());
        meme.setThumbnailUrl(post.getThumbnail());
        meme.setSubreddit(post.getSubreddit());
        meme.setAuthor(post.getAuthor());
        meme.setScore(post.getScore() != null ? post.getScore() : 0);
        meme.setNumComments(post.getNumComments() != null ? post.getNumComments() : 0);
        meme.setPermalink(post.getPermalink());
        meme.setIsVideo(post.getIsVideo() != null ? post.getIsVideo() : false);

        if (post.getCreatedUtc() != null) {
            meme.setCreatedUtc(LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(post.getCreatedUtc()),
                    ZoneId.systemDefault()
            ));
        } else {
            meme.setCreatedUtc(LocalDateTime.now());
        }

        return meme;
    }
}
