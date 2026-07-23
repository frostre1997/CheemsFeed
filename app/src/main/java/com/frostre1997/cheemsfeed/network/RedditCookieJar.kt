package com.frostre1997.cheemsfeed.network

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class RedditCookieJar : CookieJar {
    private val cookieStore = mutableMapOf<String, MutableList<Cookie>>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val host = url.host
        val existing = cookieStore[host] ?: mutableListOf()
        existing.addAll(cookies)
        cookieStore[host] = existing
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val host = url.host
        return cookieStore[host] ?: emptyList()
    }

    fun clearCookies() {
        cookieStore.clear()
    }
}
