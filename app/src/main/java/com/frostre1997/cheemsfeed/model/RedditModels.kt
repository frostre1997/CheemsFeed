package com.frostre1997.cheemsfeed.model

import com.google.gson.annotations.SerializedName

data class RedditResponse(
    @SerializedName("data") val data: ListingData
)

data class ListingData(
    @SerializedName("children") val children: List<PostContainer>,
    @SerializedName("after") val after: String?,
    @SerializedName("before") val before: String?
)

data class PostContainer(
    @SerializedName("data") val data: PostData
)

data class PostData(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String,
    @SerializedName("author") val author: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("thumbnail") val thumbnail: String?,
    @SerializedName("created_utc") val created_utc: Long?,
    @SerializedName("num_comments") val num_comments: Int?,
    @SerializedName("score") val score: Int?,
    @SerializedName("selftext") val selftext: String?,
    @SerializedName("subreddit") val subreddit: String?,
    @SerializedName("is_self") val is_self: Boolean?,
    @SerializedName("permalink") val permalink: String?
)

data class TokenResponse(
    @SerializedName("access_token") val access_token: String,
    @SerializedName("token_type") val token_type: String,
    @SerializedName("expires_in") val expires_in: Int,
    @SerializedName("scope") val scope: String,
    @SerializedName("refresh_token") val refresh_token: String?
)

data class UserResponse(
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: String,
    @SerializedName("link_karma") val link_karma: Long,
    @SerializedName("comment_karma") val comment_karma: Long,
    @SerializedName("is_gold") val is_gold: Boolean
)
