package com.frostre1997.cheemsfeed

import com.google.gson.annotations.SerializedName

data class RedditResponse(
    @SerializedName("data") val data: ListingData
)

data class ListingData(
    @SerializedName("children") val children: List<PostContainer>
)

data class PostContainer(
    @SerializedName("data") val data: PostData
)

data class PostData(
    @SerializedName("title") val title: String,
    @SerializedName("author") val author: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("thumbnail") val thumbnail: String?,
    @SerializedName("created_utc") val created_utc: Long?,
    @SerializedName("num_comments") val num_comments: Int?,
    @SerializedName("score") val score: Int?
)
