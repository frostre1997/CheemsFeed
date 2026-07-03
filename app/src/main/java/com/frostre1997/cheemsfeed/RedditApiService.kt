package com.frostre1997.cheemsfeed

import retrofit2.call
import retrofit2.http.GET

interface RedditApiService {
    @GET("r/all/hot.json")
    fun getHotPosts(): Call<RedditResponse>
}

