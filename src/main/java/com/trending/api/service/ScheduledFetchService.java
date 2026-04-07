package com.trending.api.service;

import com.trending.api.model.dto.newsapi.Article;
import com.trending.api.model.dto.reddit.RedditPost;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduledFetchService {

    private final RedditService redditService;
    private final NewsApiService newsApiService;
    private final MemeService memeService;
    private final NewsService newsService;

    @Value("${reddit.subreddits.memes}")
    private List<String> memeSubreddits;

    @Value("${reddit.subreddits.news}")
    private List<String> newsSubreddits;

    @Value("${newsapi.categories}")
    private List<String> newsCategories;

    @Value("${scheduling.memes.fetch-limit}")
    private int memeFetchLimit;

    @Value("${scheduling.news.fetch-limit}")
    private int newsFetchLimit;

    @Value("${scheduling.memes.retention-days}")
    private int memeRetentionDays;

    @Value("${scheduling.news.retention-days}")
    private int newsRetentionDays;

    @Scheduled(fixedRate = 1800000) // Every 30 minutes
    public void fetchTrendingMemes() {
        log.info("Starting scheduled meme fetch job");

        try {
            for (String subreddit : memeSubreddits) {
                try {
                    List<RedditPost> posts = redditService.fetchPostsFromSubreddit(subreddit, memeFetchLimit);
                    memeService.saveMemes(posts);
                } catch (Exception e) {
                    log.error("Failed to fetch memes from r/{}: {}", subreddit, e.getMessage());
                }
            }

            log.info("Completed meme fetch job");
        } catch (Exception e) {
            log.error("Meme fetch job failed: {}", e.getMessage(), e);
        }
    }

    @Scheduled(fixedRate = 900000) // Every 15 minutes
    public void fetchTrendingNewsFromReddit() {
        log.info("Starting scheduled Reddit news fetch job");

        try {
            for (String subreddit : newsSubreddits) {
                try {
                    List<RedditPost> posts = redditService.fetchPostsFromSubreddit(subreddit, newsFetchLimit);
                    newsService.saveNewsFromReddit(posts, subreddit);
                } catch (Exception e) {
                    log.error("Failed to fetch news from r/{}: {}", subreddit, e.getMessage());
                }
            }

            log.info("Completed Reddit news fetch job");
        } catch (Exception e) {
            log.error("Reddit news fetch job failed: {}", e.getMessage(), e);
        }
    }

    @Scheduled(fixedRate = 1200000) // Every 20 minutes
    public void fetchNewsFromNewsApi() {
        log.info("Starting scheduled News API fetch job");

        try {
            for (String category : newsCategories) {
                try {
                    List<Article> articles = newsApiService.fetchTopHeadlines(category, newsFetchLimit);
                    newsService.saveNewsFromNewsApi(articles, category);
                } catch (Exception e) {
                    log.error("Failed to fetch news for category {}: {}", category, e.getMessage());
                }
            }

            log.info("Completed News API fetch job");
        } catch (Exception e) {
            log.error("News API fetch job failed: {}", e.getMessage(), e);
        }
    }

    @Scheduled(cron = "0 0 2 * * *") // Daily at 2:00 AM
    public void cleanupOldData() {
        log.info("Starting scheduled cleanup job");

        try {
            memeService.deleteOldMemes(memeRetentionDays);
            newsService.deleteOldNews(newsRetentionDays);

            log.info("Completed cleanup job");
        } catch (Exception e) {
            log.error("Cleanup job failed: {}", e.getMessage(), e);
        }
    }
}
