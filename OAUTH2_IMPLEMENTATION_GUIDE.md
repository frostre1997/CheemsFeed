# CheemsFeed OAuth2 & Data Storage Implementation Guide

## Overview
This guide explains how to implement Reddit OAuth2 authentication and data storage for your CheemsFeed Reddit client.

## ✅ What's Been Added

### 1. **Dependencies (build.gradle)**
- **Retrofit 2.11.0** - API calls
- **OkHttp3** - HTTP client with logging
- **Gson** - JSON serialization
- **Room** - Local database
- **Security Crypto** - Encrypted token storage
- **Coroutines** - Async operations
- **Picasso** - Image loading

### 2. **Authentication Files**
- `RedditAuthManager.kt` - OAuth2 token management with encrypted storage
- `LoginActivity.kt` - OAuth2 flow UI

### 3. **API Files**
- `RedditApiService.kt` - Retrofit API endpoints
- `RedditApiClient.kt` - Retrofit client configuration

### 4. **Data Storage Files**
- `AppDatabase.kt` - Room database setup
- `Entities.kt` - SavedPost & UserProfile models
- `Daos.kt` - Database access objects

---

## 🔐 Setup Instructions

### Step 1: Register Your Reddit Application

1. Go to https://www.reddit.com/prefs/apps
2. Click "Create App" or "Create Another App"
3. Fill in:
   - **Name**: CheemsFeed
   - **App type**: Installed app
   - **Redirect URI**: `cheemsfeed://auth`
4. Copy your **Client ID** and **Client Secret**

### Step 2: Update OAuth Credentials

Edit `app/src/main/java/com/frostre1997/cheemsfeed/auth/RedditAuthManager.kt`:

```kotlin
companion object {
    private const val REDDIT_CLIENT_ID = "YOUR_ACTUAL_CLIENT_ID"
    private const val REDDIT_CLIENT_SECRET = "YOUR_ACTUAL_CLIENT_SECRET"
    // ... rest of code
}
```

### Step 3: Configure Deep Linking (AndroidManifest.xml)

Add this intent filter to your LoginActivity in `AndroidManifest.xml`:

```xml
<activity android:name=".auth.LoginActivity">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data
            android:scheme="cheemsfeed"
            android:host="auth" />
    </intent-filter>
</activity>
```

### Step 4: Initialize in Your MainActivity

```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var authManager: RedditAuthManager
    private lateinit var apiService: RedditApiService
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize API and Auth
        val authApiService = RedditApiClient.createAuthService()
        val mainApiService = RedditApiClient.createApiService()
        
        authManager = RedditAuthManager(this, authApiService)
        
        // Initialize Database
        database = AppDatabase.getDatabase(this)
        
        // Check if already logged in
        if (!authManager.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
```

---

## 📱 OAuth2 Flow Explanation

### 1. **User clicks Login**
- `LoginActivity.openRedditAuth()` generates authorization URL with state parameter
- Opens Reddit in browser

### 2. **User Authorizes**
- Reddit redirects to `cheemsfeed://auth?code=...&state=...`
- Your app receives the code

### 3. **Exchange Code for Token**
- `LoginActivity.exchangeCodeForToken()` sends code to Reddit API
- Reddit returns access token (expires in ~1 hour)
- Token is encrypted and stored locally

### 4. **Access Protected Resources**
- Use `authManager.getValidAccessToken()` before API calls
- Returns valid token, refreshing if needed

---

## 💾 Database Usage Examples

### Save a Post
```kotlin
lifecycleScope.launch {
    val post = SavedPost(
        id = "post123",
        title = "Great Post",
        content = "...",
        author = "username",
        subreddit = "funny",
        score = 1500,
        commentCount = 42,
        createdAt = System.currentTimeMillis(),
        postUrl = "https://reddit.com/...",
        thumbnailUrl = "https://...",
        isSelfPost = false,
        permalink = "/r/funny/comments/..."
    )
    database.savedPostDao().insert(post)
}
```

### Retrieve Saved Posts
```kotlin
lifecycleScope.launch {
    database.savedPostDao().getAllSavedPosts().collect { posts ->
        // Update UI with posts
        adapter.submitList(posts)
    }
}
```

### Save User Profile
```kotlin
lifecycleScope.launch {
    val profile = UserProfile(
        username = "your_username",
        userId = "user123",
        linkKarma = 5000,
        commentKarma = 15000,
        isGold = false
    )
    database.userProfileDao().insert(profile)
}
```

---

## 🌐 Making API Calls

### Get Hot Posts
```kotlin
lifecycleScope.launch {
    try {
        val token = authManager.getValidAccessToken() ?: return@launch
        val response = apiService.getHotPosts(
            subreddit = "funny",
            limit = 25,
            token = "Bearer $token"
        )
        // Handle response
    } catch (e: Exception) {
        Log.e("API Error", e.message ?: "Unknown error")
    }
}
```

### Get Current User
```kotlin
lifecycleScope.launch {
    try {
        val token = authManager.getValidAccessToken() ?: return@launch
        val user = apiService.getCurrentUser("Bearer $token")
        // Update UI with user info
    } catch (e: Exception) {
        Log.e("API Error", e.message ?: "Unknown error")
    }
}
```

---

## 🔒 Security Best Practices

✅ **What's Already Implemented:**
- Encrypted SharedPreferences for token storage
- HTTPS for all API calls
- State parameter validation for OAuth
- Automatic token refresh
- Secure Base64 encoding of credentials

⚠️ **Additional Recommendations:**
- Don't hardcode credentials - use BuildConfig variants
- Implement token expiry checks
- Clear tokens on logout
- Use ProGuard/R8 to obfuscate sensitive code
- Implement certificate pinning for production

---

## 🐛 Common Issues & Solutions

| Issue | Solution |
|-------|----------|
| "Invalid client ID" | Verify CLIENT_ID and CLIENT_SECRET are correct |
| "Redirect URI mismatch" | Ensure `cheemsfeed://auth` matches Reddit app settings |
| "State mismatch error" | Clear app cache and try again |
| "Token expired" | `getValidAccessToken()` handles refresh automatically |
| "Database locked" | Room handles concurrency - use coroutines |

---

## 📚 Reddit API Resources

- **OAuth Documentation**: https://github.com/reddit-archive/reddit/wiki/OAuth2
- **API Endpoints**: https://www.reddit.com/dev/api
- **Rate Limits**: 60 requests per minute per OAuth token

---

## ✨ Next Steps

1. **Implement LoginActivity UI** - Add login button and progress indicator
2. **Create FeedFragment** - Display posts in RecyclerView
3. **Add subreddit browsing** - Allow user to select subreddits
4. **Implement offline mode** - Cache posts locally
5. **Add user profile view** - Display karma, profile picture, etc.

---

## 📞 Support

For issues with:
- **Reddit API**: Check official docs at https://www.reddit.com/dev/api
- **Room Database**: https://developer.android.com/training/data-storage/room
- **Retrofit**: https://square.github.io/retrofit/
- **OAuth2**: https://oauth.net/2/

