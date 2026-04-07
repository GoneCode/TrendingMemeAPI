# API Response Examples

This document shows example responses from the API endpoints.

## Memes API

### GET /api/v1/memes/trending

**Request:**
```bash
curl "http://localhost:8080/api/v1/memes/trending?page=0&size=2"
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "redditId": "1b2c3d4",
      "title": "When you finally understand recursion",
      "url": "https://i.redd.it/example123.jpg",
      "thumbnailUrl": "https://b.thumbs.redditmedia.com/example-thumb.jpg",
      "subreddit": "memes",
      "author": "meme_master_2024",
      "score": 15234,
      "numComments": 342,
      "createdUtc": "2026-04-07T10:30:15",
      "fetchedAt": "2026-04-07T11:00:00",
      "permalink": "/r/memes/comments/1b2c3d4/when_you_finally_understand_recursion/",
      "isVideo": false
    },
    {
      "id": 2,
      "redditId": "5e6f7g8",
      "title": "Me explaining my code to the rubber duck",
      "url": "https://i.redd.it/example456.png",
      "thumbnailUrl": "https://b.thumbs.redditmedia.com/example-thumb2.jpg",
      "subreddit": "dankmemes",
      "author": "code_ninja",
      "score": 12890,
      "numComments": 201,
      "createdUtc": "2026-04-07T09:15:30",
      "fetchedAt": "2026-04-07T11:00:00",
      "permalink": "/r/dankmemes/comments/5e6f7g8/me_explaining_my_code/",
      "isVideo": false
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 2,
    "sort": {
      "sorted": false,
      "empty": true,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": false,
  "totalPages": 50,
  "totalElements": 100,
  "size": 2,
  "number": 0,
  "sort": {
    "sorted": false,
    "empty": true,
    "unsorted": true
  },
  "first": true,
  "numberOfElements": 2,
  "empty": false
}
```

### GET /api/v1/memes/123

**Request:**
```bash
curl "http://localhost:8080/api/v1/memes/123"
```

**Response:**
```json
{
  "id": 123,
  "redditId": "abc123xyz",
  "title": "Classic programming meme",
  "url": "https://i.redd.it/example789.jpg",
  "thumbnailUrl": "https://b.thumbs.redditmedia.com/example-thumb3.jpg",
  "subreddit": "memes",
  "author": "developer_420",
  "score": 8765,
  "numComments": 156,
  "createdUtc": "2026-04-07T08:20:10",
  "fetchedAt": "2026-04-07T11:00:00",
  "permalink": "/r/memes/comments/abc123xyz/classic_programming_meme/",
  "isVideo": false
}
```

### GET /api/v1/memes/subreddit/dankmemes

**Request:**
```bash
curl "http://localhost:8080/api/v1/memes/subreddit/dankmemes?page=0&size=1"
```

**Response:**
```json
{
  "content": [
    {
      "id": 45,
      "redditId": "dank456",
      "title": "Dank meme of the day",
      "url": "https://i.redd.it/dank123.jpg",
      "thumbnailUrl": "https://b.thumbs.redditmedia.com/dank-thumb.jpg",
      "subreddit": "dankmemes",
      "author": "dank_lord",
      "score": 20500,
      "numComments": 450,
      "createdUtc": "2026-04-07T07:45:00",
      "fetchedAt": "2026-04-07T11:00:00",
      "permalink": "/r/dankmemes/comments/dank456/dank_meme/",
      "isVideo": false
    }
  ],
  "totalElements": 48,
  "totalPages": 48,
  "size": 1,
  "number": 0
}
```

## News API

### GET /api/v1/news/trending

**Request:**
```bash
curl "http://localhost:8080/api/v1/news/trending?page=0&size=2"
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "externalId": "newsapi_a1b2c3d4",
      "source": "newsapi",
      "title": "Major Tech Company Announces Revolutionary AI Breakthrough",
      "description": "A leading technology company has unveiled a groundbreaking artificial intelligence system that promises to transform the industry.",
      "url": "https://example-news-site.com/tech-ai-breakthrough",
      "imageUrl": "https://example-news-site.com/images/ai-tech.jpg",
      "author": "Jane Tech Reporter",
      "publishedAt": "2026-04-07T14:30:00",
      "fetchedAt": "2026-04-07T15:00:00",
      "category": "technology",
      "subreddit": null,
      "score": 0,
      "numComments": 0
    },
    {
      "id": 2,
      "externalId": "reddit_xyz789",
      "source": "reddit",
      "title": "Breaking: Global Climate Summit Reaches Historic Agreement",
      "description": null,
      "url": "https://climate-news.example.com/summit-agreement",
      "imageUrl": "https://preview.redd.it/climate-summit.jpg",
      "author": "news_poster",
      "publishedAt": "2026-04-07T13:15:00",
      "fetchedAt": "2026-04-07T14:00:00",
      "category": null,
      "subreddit": "worldnews",
      "score": 45600,
      "numComments": 2340
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 2
  },
  "totalPages": 75,
  "totalElements": 150,
  "size": 2,
  "number": 0
}
```

### GET /api/v1/news/trending?source=reddit

**Request:**
```bash
curl "http://localhost:8080/api/v1/news/trending?source=reddit&page=0&size=1"
```

**Response:**
```json
{
  "content": [
    {
      "id": 34,
      "externalId": "reddit_qwe123",
      "source": "reddit",
      "title": "Scientists Discover New Species in Amazon Rainforest",
      "description": null,
      "url": "https://science-article.example.com/new-species",
      "imageUrl": "https://preview.redd.it/species-discovery.jpg",
      "author": "science_fan_99",
      "publishedAt": "2026-04-07T12:00:00",
      "fetchedAt": "2026-04-07T14:00:00",
      "category": null,
      "subreddit": "news",
      "score": 23400,
      "numComments": 890
    }
  ],
  "totalElements": 65,
  "totalPages": 65,
  "size": 1
}
```

### GET /api/v1/news/category/technology

**Request:**
```bash
curl "http://localhost:8080/api/v1/news/category/technology?page=0&size=1"
```

**Response:**
```json
{
  "content": [
    {
      "id": 12,
      "externalId": "newsapi_tech567",
      "source": "newsapi",
      "title": "New Smartphone Features Quantum Encryption Technology",
      "description": "The latest flagship smartphone incorporates cutting-edge quantum encryption for unprecedented security.",
      "url": "https://tech-news.example.com/quantum-phone",
      "imageUrl": "https://tech-news.example.com/images/quantum-phone.jpg",
      "author": "Tech Correspondent",
      "publishedAt": "2026-04-07T11:45:00",
      "fetchedAt": "2026-04-07T12:00:00",
      "category": "technology",
      "subreddit": null,
      "score": 0,
      "numComments": 0
    }
  ],
  "totalElements": 42,
  "totalPages": 42,
  "size": 1
}
```

## Health Check

### GET /actuator/health

**Request:**
```bash
curl http://localhost:8080/actuator/health
```

**Response:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "H2",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 500000000000,
        "free": 250000000000,
        "threshold": 10485760,
        "exists": true
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

## Error Responses

### 404 Not Found

**Request:**
```bash
curl http://localhost:8080/api/v1/memes/99999
```

**Response:**
```json
{
  "timestamp": "2026-04-07T15:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Meme not found with id: 99999",
  "path": "/api/v1/memes/99999"
}
```

### 503 Service Unavailable (API Error)

**Response when external API fails:**
```json
{
  "timestamp": "2026-04-07T15:30:00",
  "status": 503,
  "error": "Service Unavailable",
  "message": "External API error: Failed to fetch posts from Reddit: Connection timeout",
  "path": "/api/v1/memes/trending"
}
```

## Pagination

All list endpoints support pagination with these query parameters:

- `page` (default: 0) - Zero-based page number
- `size` (default: 25) - Number of items per page

The response includes:
- `content` - Array of items for current page
- `totalElements` - Total number of items across all pages
- `totalPages` - Total number of pages
- `size` - Items per page
- `number` - Current page number (zero-based)
- `first` - Boolean indicating if this is the first page
- `last` - Boolean indicating if this is the last page

## Testing with Different Page Sizes

```bash
# Get 5 items
curl "http://localhost:8080/api/v1/memes/trending?size=5"

# Get page 2 (third page, zero-indexed)
curl "http://localhost:8080/api/v1/memes/trending?page=2&size=10"

# Get 100 items (max recommended)
curl "http://localhost:8080/api/v1/news/trending?size=100"
```
