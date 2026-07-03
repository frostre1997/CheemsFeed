package com.frostre1997.cheemsfeed

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://www.reddit.com/"

    // Creiamo il client con l'header necessario per evitare il 403
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("User-Agent", "android:com.frostre1997.cheemsfeed:v1.0 (by /u/frostre1997)")
                .build()
            chain.proceed(request)
        }
        .build()

    val instance: RedditApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Colleghiamo il client configurato
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RedditApi::class.java)
    }
}
