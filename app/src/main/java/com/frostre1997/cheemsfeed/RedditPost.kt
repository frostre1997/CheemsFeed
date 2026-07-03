package com.frostre1997.cheemsfeed

import com.google.gson.annotations.SerializedName

data class RedditResponse(
    @SerializedName("data") val data: ListingData
)

data class ListingData(
    @SerializedName("children") val children: List<PostContainer>
)

data class PostContainer(
    @SerializedName("data") val data: RedditPost
)

data class RedditPost(
    @SerializedName("title") val title: String,
    @SerializedName("author") val author: String
)

