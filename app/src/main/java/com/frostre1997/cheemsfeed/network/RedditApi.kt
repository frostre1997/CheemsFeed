package com.frostre1997.cheemsfeed.network

import com.frostre1997.cheemsfeed.model.RedditResponse
import com.frostre1997.cheemsfeed.model.TokenResponse
import com.frostre1997.cheemsfeed.model.UserResponse
import com.google.gson.JsonObject
import retrofit2.http.*

interface RedditApi {

    @POST("api/v1/access_token")
    suspend fun getAccessToken(
        @Header("Authorization") auth: String,
        @Body body: JsonObject
    ): TokenResponse

    @GET("api/v1/me")
    suspend fun getCurrentUser(
        @Header("Authorization") token: String
    ): UserResponse

    @GET("r/{subreddit}/hot.json")
    suspend fun getHotPosts(
        @Path("subreddit") subreddit: String,
        @Query("limit") limit: Int = 25,
        @Query("after") after: String? = null,
        @Header("Authorization") token: String
    ): RedditResponse

    @GET("r/{subreddit}/new.json")
    suspend fun getNewPosts(
        @Path("subreddit") subreddit: String,
        @Query("limit") limit: Int = 25,
        @Query("after") after: String? = null,
        @Header("Authorization") token: String
    ): RedditResponse

    @GET("r/{subreddit}/top.json")
    suspend fun getTopPosts(
        @Path("subreddit") subreddit: String,
        @Query("t") timeFilter: String = "day",
        @Query("limit") limit: Int = 25,
        @Query("after") after: String? = null,
        @Header("Authorization") token: String
    ): RedditResponse
}
