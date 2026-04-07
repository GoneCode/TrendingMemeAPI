# Setup Guide for Trending Memes & News API

## Step 1: Install Java 17

Since Java is not currently installed on your system, you need to install it first.

### For Windows (WSL):

```bash
# Update package list
sudo apt update

# Install OpenJDK 17
sudo apt install openjdk-17-jdk -y

# Verify installation
java -version
javac -version
```

### Set JAVA_HOME environment variable:

Add these lines to your `~/.bashrc`:

```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
```

Then reload:
```bash
source ~/.bashrc
```

## Step 2: Install Maven (if not already installed)

```bash
# Check if Maven is installed
mvn -version

# If not installed:
sudo apt install maven -y
```

## Step 3: Set Up API Credentials

### Get Reddit API Credentials:

1. Go to https://www.reddit.com/prefs/apps
2. Scroll down and click **"Create App"** or **"Create Another App"**
3. Fill in the form:
   - **name**: TrendingMemeAPI (or any name you want)
   - **type**: Select **"script"**
   - **description**: (optional)
   - **about url**: (optional)
   - **redirect uri**: http://localhost:8080
4. Click **"Create app"**
5. Note the values:
   - The string under your app name is your **client_id**
   - The string next to "secret" is your **client_secret**

### Get News API Key:

1. Go to https://newsapi.org/
2. Click **"Get API Key"**
3. Sign up for a free account
4. Get your API key from the dashboard

### Create .env file:

Create a file named `.env` in the project root directory:

```bash
cd /mnt/c/Users/mais/TrendingMemeAPI
nano .env
```

Add the following content (replace with your actual credentials):

```bash
# Reddit API Credentials
export REDDIT_CLIENT_ID=your_actual_client_id_here
export REDDIT_CLIENT_SECRET=your_actual_client_secret_here

# News API Key
export NEWSAPI_KEY=your_actual_newsapi_key_here
```

Save and exit (Ctrl+X, then Y, then Enter).

### Load environment variables:

```bash
source .env
```

## Step 4: Build the Project

```bash
mvn clean install
```

This will:
- Download all dependencies
- Compile the code
- Run tests
- Create the JAR file

## Step 5: Run the Application

```bash
mvn spring-boot:run
```

The application will start and be available at `http://localhost:8080`

## Step 6: Test the API

### Check health:
```bash
curl http://localhost:8080/actuator/health
```

### Wait for data to be collected:
The scheduled jobs will run automatically. The first fetch happens:
- Memes: Shortly after startup, then every 30 minutes
- News: Shortly after startup, then every 15-20 minutes

### Test the endpoints:

```bash
# Get trending memes
curl http://localhost:8080/api/v1/memes/trending

# Get trending news
curl http://localhost:8080/api/v1/news/trending

# Get news from a specific category
curl http://localhost:8080/api/v1/news/category/technology

# Get memes from a specific subreddit
curl http://localhost:8080/api/v1/memes/subreddit/memes
```

## Step 7: Access H2 Console (Optional)

1. Open your browser
2. Go to: `http://localhost:8080/h2-console`
3. Use these settings:
   - **JDBC URL**: `jdbc:h2:mem:trendingdb`
   - **Username**: `sa`
   - **Password**: (leave blank)
4. Click **Connect**

You can run SQL queries to see the data:

```sql
SELECT * FROM MEMES ORDER BY SCORE DESC LIMIT 10;
SELECT * FROM NEWS ORDER BY PUBLISHED_AT DESC LIMIT 10;
```

## Common Issues

### Issue 1: "JAVA_HOME environment variable is not defined"
**Solution**: Follow Step 1 above to install Java and set JAVA_HOME

### Issue 2: "No data returned from API"
**Solution**:
- Wait a few minutes for the first scheduled job to run
- Check logs for any errors
- Verify your API credentials are correct

### Issue 3: "Reddit authentication failed"
**Solution**:
- Double-check your REDDIT_CLIENT_ID and REDDIT_CLIENT_SECRET
- Ensure you created a "script" type app on Reddit
- Make sure environment variables are loaded: `echo $REDDIT_CLIENT_ID`

### Issue 4: "News API rate limit exceeded"
**Solution**:
- Free tier allows 100 requests/day
- Wait 24 hours for the limit to reset
- Or upgrade to a paid News API plan

## Stopping the Application

Press `Ctrl + C` in the terminal where the application is running.

## Next Steps

Once everything is running:
1. Explore the API endpoints
2. Check the H2 console to see stored data
3. Review logs to understand the scheduled job execution
4. Customize configuration in `application.yml` if needed

## Production Deployment

For production, see the README.md file for PostgreSQL setup and deployment instructions.
