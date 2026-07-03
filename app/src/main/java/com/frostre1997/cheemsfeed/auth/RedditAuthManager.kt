package com.frostre1997.cheemsfeed.auth

import android.content.Context
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.frostre1997.cheemsfeed.api.RedditApiService
import com.frostre1997.cheemsfeed.api.TokenResponse
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class RedditAuthManager(
    private val context: Context,
    private val apiService: RedditApiService
) {
    companion object {
        private const val REDDIT_CLIENT_ID = "YOUR_REDDIT_CLIENT_ID"
        private const val REDDIT_CLIENT_SECRET = "YOUR_REDDIT_CLIENT_SECRET"
        private const val REDDIT_REDIRECT_URI = "cheemsfeed://auth"
        private const val REDDIT_AUTH_URL = "https://www.reddit.com/api/v1/authorize"
        private const val REDDIT_TOKEN_URL = "https://oauth.reddit.com/api/v1/access_token"
        
        private const val PREFS_NAME = "reddit_auth"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_TOKEN_EXPIRY = "token_expiry"
        private const val KEY_USERNAME = "username"
    }

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedPrefs = EncryptedSharedPreferences.create(
        context,
        PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val gson = Gson()

    /**
     * Generate OAuth2 authorization URL
     * User should open this URL in browser
     */
    fun getAuthorizationUrl(): String {
        val state = UUID.randomUUID().toString()
        saveState(state)
        
        return "$REDDIT_AUTH_URL?" +
                "client_id=$REDDIT_CLIENT_ID&" +
                "response_type=code&" +
                "state=$state&" +
                "redirect_uri=$REDDIT_REDIRECT_URI&" +
                "duration=permanent&" +
                "scope=identity,read,mysubreddits"
    }

    /**
     * Exchange authorization code for access token
     */
    suspend fun exchangeCodeForToken(code: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            // Create Basic Auth header (base64 encoded client_id:client_secret)
            val credentials = "$REDDIT_CLIENT_ID:$REDDIT_CLIENT_SECRET"
            val encodedCredentials = Base64.encodeToString(
                credentials.toByteArray(),
                Base64.NO_WRAP
            )
            val authHeader = "Basic $encodedCredentials"

            // Build request body
            val body = JsonObject().apply {
                addProperty("grant_type", "authorization_code")
                addProperty("code", code)
                addProperty("redirect_uri", REDDIT_REDIRECT_URI)
            }

            // Get token
            val response = apiService.getAccessToken(authHeader, body)
            
            // Save tokens
            saveTokens(response)
            
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Refresh expired access token
     */
    suspend fun refreshAccessToken(): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val refreshToken = getRefreshToken() ?: return@withContext false

            val credentials = "$REDDIT_CLIENT_ID:$REDDIT_CLIENT_SECRET"
            val encodedCredentials = Base64.encodeToString(
                credentials.toByteArray(),
                Base64.NO_WRAP
            )
            val authHeader = "Basic $encodedCredentials"

            val body = JsonObject().apply {
                addProperty("grant_type", "refresh_token")
                addProperty("refresh_token", refreshToken)
            }

            val response = apiService.getAccessToken(authHeader, body)
            saveTokens(response)
            
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Get valid access token (refresh if expired)
     */
    suspend fun getValidAccessToken(): String? = withContext(Dispatchers.IO) {
        val expiry = encryptedPrefs.getLong(KEY_TOKEN_EXPIRY, 0)
        
        // If token expires within next 60 seconds, refresh
        if (System.currentTimeMillis() + 60000 > expiry) {
            if (!refreshAccessToken()) {
                return@withContext null
            }
        }
        
        return@withContext getAccessToken()
    }

    /**
     * Check if user is logged in
     */
    fun isLoggedIn(): Boolean {
        return getAccessToken() != null
    }

    /**
     * Logout user
     */
    fun logout() {
        encryptedPrefs.edit().apply {
            remove(KEY_ACCESS_TOKEN)
            remove(KEY_REFRESH_TOKEN)
            remove(KEY_TOKEN_EXPIRY)
            remove(KEY_USERNAME)
            apply()
        }
    }

    private fun saveTokens(response: TokenResponse) {
        val expiryTime = System.currentTimeMillis() + (response.expires_in * 1000)
        
        encryptedPrefs.edit().apply {
            putString(KEY_ACCESS_TOKEN, response.access_token)
            putLong(KEY_TOKEN_EXPIRY, expiryTime)
            apply()
        }
    }

    private fun getAccessToken(): String? {
        return encryptedPrefs.getString(KEY_ACCESS_TOKEN, null)
    }

    private fun getRefreshToken(): String? {
        return encryptedPrefs.getString(KEY_REFRESH_TOKEN, null)
    }

    private fun saveState(state: String) {
        encryptedPrefs.edit().putString("oauth_state", state).apply()
    }

    fun getState(): String? {
        return encryptedPrefs.getString("oauth_state", null)
    }

    fun clearState() {
        encryptedPrefs.edit().remove("oauth_state").apply()
    }
}
