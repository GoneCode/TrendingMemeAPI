# Quick Start Guide

## Prerequisites Checklist

- [ ] Java 17 installed
- [ ] Maven installed
- [ ] Reddit API credentials obtained
- [ ] News API key obtained

## Quick Setup (5 minutes)

### 1. Install Java 17 (if needed)
```bash
sudo apt update
sudo apt install openjdk-17-jdk maven -y
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
```

### 2. Set Environment Variables
```bash
export REDDIT_CLIENT_ID="your_reddit_client_id"
export REDDIT_CLIENT_SECRET="your_reddit_client_secret"
export NEWSAPI_KEY="your_newsapi_key"
```

### 3. Build & Run
```bash
cd /mnt/c/Users/mais/TrendingMemeAPI
mvn clean install
mvn spring-boot:run
```

### 4. Test
```bash
# Wait 1-2 minutes for first data fetch, then:
curl http://localhost:8080/api/v1/memes/trending
curl http://localhost:8080/api/v1/news/trending
```

## API Endpoints Summary

| Endpoint | Description |
|----------|-------------|
| `GET /api/v1/memes/trending` | Get trending memes |
| `GET /api/v1/memes/{id}` | Get specific meme |
| `GET /api/v1/memes/subreddit/{name}` | Filter by subreddit |
| `GET /api/v1/news/trending` | Get trending news |
| `GET /api/v1/news/{id}` | Get specific news |
| `GET /api/v1/news/category/{category}` | Filter by category |
| `GET /actuator/health` | Health check |

## H2 Database Console

- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:trendingdb`
- Username: `sa`
- Password: (blank)

## Get API Credentials

### Reddit API:
1. Visit: https://www.reddit.com/prefs/apps
2. Click "Create App"
3. Select "script" type
4. Copy client_id and client_secret

### News API:
1. Visit: https://newsapi.org/
2. Sign up
3. Copy API key

## Scheduled Jobs

- **Memes**: Every 30 min from r/memes, r/dankmemes
- **Reddit News**: Every 15 min from r/worldnews, r/news
- **News API**: Every 20 min (general, tech, business, entertainment)
- **Cleanup**: Daily at 2 AM (old data removal)

## File Structure

```
TrendingMemeAPI/
├── src/main/java/com/trending/api/
│   ├── TrendingApiApplication.java       # Main entry point
│   ├── config/                           # Configuration
│   ├── controller/                       # REST endpoints
│   ├── service/                          # Business logic
│   ├── repository/                       # Data access
│   ├── model/                            # Entities & DTOs
│   └── exception/                        # Error handling
├── src/main/resources/
│   ├── application.yml                   # Configuration
│   └── application-prod.yml              # Production config
├── pom.xml                               # Maven dependencies
└── README.md                             # Full documentation
```

## Troubleshooting

**No data?**
→ Wait 2-3 minutes for first scheduled fetch
→ Check logs for errors
→ Verify API credentials: `echo $REDDIT_CLIENT_ID`

**Authentication failed?**
→ Ensure Reddit app is "script" type
→ Double-check credentials
→ Reload environment variables

**Maven build fails?**
→ Verify Java 17: `java -version`
→ Check JAVA_HOME: `echo $JAVA_HOME`

## Example Requests

```bash
# Get first page of memes
curl "http://localhost:8080/api/v1/memes/trending?page=0&size=10"

# Get news from Reddit only
curl "http://localhost:8080/api/v1/news/trending?source=reddit"

# Get technology news
curl "http://localhost:8080/api/v1/news/category/technology"

# Get memes from dankmemes
curl "http://localhost:8080/api/v1/memes/subreddit/dankmemes"
```

## For More Details

- See `SETUP_GUIDE.md` for detailed installation steps
- See `README.md` for complete API documentation
- Check application logs for debugging
