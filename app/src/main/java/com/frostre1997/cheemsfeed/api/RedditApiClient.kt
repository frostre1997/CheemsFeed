package com.frostre1997.cheemsfeed.api

import com.squareup.okhttp3.OkHttpClient
import com.squareup.okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RedditApiClient {
    private const val REDDIT_API_BASE_URL = "https://oauth.reddit.com/"
    private const val REDDIT_AUTH_BASE_URL = "https://www.reddit.com/"

    fun createAuthService(): RedditApiService {
        val okHttpClient = createOkHttpClient()
        
        return Retrofit.Builder()
            .baseUrl(REDDIT_AUTH_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RedditApiService::class.java)
    }

    fun createApiService(): RedditApiService {
        val okHttpClient = createOkHttpClient()
        
        return Retrofit.Builder()
            .baseUrl(REDDIT_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RedditApiService::class.java)
    }

    private fun createOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}
