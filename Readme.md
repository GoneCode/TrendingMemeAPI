# Trending Memes & News API

A Spring Boot REST API that aggregates and serves trending memes and news from Reddit and News API. The application periodically fetches content from multiple sources, stores it in a database, and exposes REST endpoints for easy access.

## Features

- **Trending Memes**: Fetch trending memes from r/memes and r/dankmemes
- **Trending News**: Aggregate news from Reddit (r/worldnews, r/news) and News API
- **Scheduled Updates**: Automatic content fetching every 15-30 minutes
- **REST API**: Clean REST endpoints with pagination support
- **Database Storage**: Persistent storage with automatic cleanup of old content
- **H2 Console**: Built-in database console for development

## Tech Stack

- **Java 17**
- **Spring Boot 3.2.4**
- **Spring Data JPA**
- **H2 Database** (development) / **PostgreSQL** (production)
- **WebClient** for async HTTP calls
- **Lombok** for code simplification
- **Maven** for build management

## Prerequisites

Before running the application, you need to obtain API credentials:

1. **Reddit API Credentials**:
   - Go to https://www.reddit.com/prefs/apps
   - Click "Create App" or "Create Another App"
   - Select "script" as the app type
   - Note your `client_id` and `client_secret`

2. **News API Key**:
   - Visit https://newsapi.org/
   - Sign up for a free account
   - Get your API key from the dashboard

## Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd TrendingMemeAPI
```

### 2. Configure Environment Variables

Create a `.env` file in the project root (or set system environment variables):

```bash
# Reddit API Credentials
REDDIT_CLIENT_ID=your_reddit_client_id
REDDIT_CLIENT_SECRET=your_reddit_client_secret

# News API Key
NEWSAPI_KEY=your_newsapi_key
```

**Important**: Never commit the `.env` file to git. It's already in `.gitignore`.

### 3. Build the Project

```bash
mvn clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Memes

#### Get Trending Memes
```http
GET /api/v1/memes/trending?page=0&size=25
```

**Query Parameters**:
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 25) - Number of items per page

**Response**:
```json
{
  "content": [
    {
      "id": 1,
      "redditId": "abc123",
      "title": "Funny meme title",
      "url": "https://i.redd.it/...",
      "thumbnailUrl": "https://...",
      "subreddit": "memes",
      "author": "username",
      "score": 1500,
      "numComments": 50,
      "createdUtc": "2026-04-07T10:30:00",
      "fetchedAt": "2026-04-07T11:00:00",
      "permalink": "/r/memes/comments/...",
      "isVideo": false
    }
  ],
  "pageable": {...},
  "totalElements": 100,
  "totalPages": 4
}
```

#### Get Meme by ID
```http
GET /api/v1/memes/{id}
```

#### Get Memes by Subreddit
```http
GET /api/v1/memes/subreddit/{subreddit}?page=0&size=25
```

Example: `GET /api/v1/memes/subreddit/dankmemes`

### News

#### Get Trending News
```http
GET /api/v1/news/trending?page=0&size=25&source=reddit
```

**Query Parameters**:
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 25) - Number of items per page
- `source` (optional) - Filter by source: `reddit`, `newsapi`, or omit for all

**Response**:
```json
{
  "content": [
    {
      "id": 1,
      "externalId": "reddit_xyz789",
      "source": "reddit",
      "title": "Breaking news headline",
      "description": "News description...",
      "url": "https://...",
      "imageUrl": "https://...",
      "author": "author_name",
      "publishedAt": "2026-04-07T09:00:00",
      "fetchedAt": "2026-04-07T10:00:00",
      "category": "general",
      "subreddit": "worldnews",
      "score": 5000,
      "numComments": 200
    }
  ],
  "pageable": {...},
  "totalElements": 150,
  "totalPages": 6
}
```

#### Get News by ID
```http
GET /api/v1/news/{id}
```

#### Get News by Category
```http
GET /api/v1/news/category/{category}?page=0&size=25
```

Categories: `general`, `technology`, `business`, `entertainment`

Example: `GET /api/v1/news/category/technology`

### Health Check

```http
GET /actuator/health
```

Returns application health status.

## H2 Database Console

During development, access the H2 console at:
```
http://localhost:8080/h2-console
```

**Connection Settings**:
- JDBC URL: `jdbc:h2:mem:trendingdb`
- Username: `sa`
- Password: (leave blank)

## Scheduled Jobs

The application automatically runs these scheduled tasks:

1. **Meme Fetching**: Every 30 minutes
   - Fetches top 100 posts from r/memes and r/dankmemes

2. **Reddit News Fetching**: Every 15 minutes
   - Fetches top 50 posts from r/worldnews and r/news

3. **News API Fetching**: Every 20 minutes
   - Fetches top headlines from all configured categories

4. **Cleanup Job**: Daily at 2:00 AM
   - Deletes memes older than 7 days
   - Deletes news older than 30 days

## Configuration

Key configuration properties in `application.yml`:

```yaml
# Adjust fetch limits
scheduling:
  memes:
    fetch-limit: 100
    retention-days: 7
  news:
    fetch-limit: 50
    retention-days: 30

# Modify subreddits
reddit:
  subreddits:
    memes:
      - memes
      - dankmemes
    news:
      - worldnews
      - news

# Modify news categories
newsapi:
  categories:
    - general
    - technology
    - business
    - entertainment
```

## Production Deployment

For production with PostgreSQL:

1. Set the active profile to `prod`:
```bash
export SPRING_PROFILES_ACTIVE=prod
```

2. Configure database environment variables:
```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=trendingdb
export DB_USERNAME=postgres
export DB_PASSWORD=your_password
```

3. Run the application:
```bash
java -jar target/trending-api-1.0.0-SNAPSHOT.jar
```

## Testing

Run tests with:
```bash
mvn test
```

## Example Usage

### Fetch trending memes with curl
```bash
curl http://localhost:8080/api/v1/memes/trending
```

### Fetch trending news from Reddit only
```bash
curl "http://localhost:8080/api/v1/news/trending?source=reddit"
```

### Get technology news
```bash
curl http://localhost:8080/api/v1/news/category/technology
```

## Troubleshooting

**Problem**: No data appearing in the API

**Solution**:
- Check that environment variables are set correctly
- Verify API credentials are valid
- Check application logs for errors
- Wait for the first scheduled job to run (or trigger manually by restarting)

**Problem**: Reddit authentication fails

**Solution**:
- Verify `REDDIT_CLIENT_ID` and `REDDIT_CLIENT_SECRET` are correct
- Ensure the Reddit app is configured as "script" type

**Problem**: News API rate limit exceeded

**Solution**:
- Free tier has 100 requests/day limit
- Reduce fetch frequency or upgrade to paid plan
- Check logs to see current request count

## Future Enhancements

- [ ] Add caching for frequently accessed endpoints
- [ ] Implement full-text search
- [ ] Add content filtering (NSFW flags)
- [ ] WebSocket support for real-time updates
- [ ] User favorites and bookmarking
- [ ] Swagger/OpenAPI documentation
- [ ] Docker support
- [ ] GraphQL API

## License

This project is for educational purposes.

## Contributing

Feel free to submit issues and enhancement requests!
