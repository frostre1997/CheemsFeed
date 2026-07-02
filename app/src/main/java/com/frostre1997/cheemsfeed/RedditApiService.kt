package com.frostre1997.cheemsfeed

import retrofit2.http.GET
import retrofit2.Call

interface RedditApiService {
    @GET("r/all/hot.json")
    fun getHotPosts(): Call<Any>
}

