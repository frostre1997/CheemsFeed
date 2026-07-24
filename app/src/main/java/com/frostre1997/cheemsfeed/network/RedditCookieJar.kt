package com.frostre1997.cheemsfeed.network

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class RedditCookieJar : CookieJar {
    private val cookieStore = mutableMapOf<String, MutableList<Cookie>>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val host = url.host
        val list = cookieStore.getOrPut(host) { mutableListOf() }
        list.addAll(cookies)
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url.host] ?: emptyList()
    }

    fun clearCookies() {
        cookieStore.clear()
    }
}
