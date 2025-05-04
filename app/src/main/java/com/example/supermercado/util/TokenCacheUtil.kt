package com.example.supermercado.util

object TokenCacheUtil {

    var token: String? = null
    var expiresIn: Long? = null

    fun isTokenExpired(): Boolean {
        val currentTime = System.currentTimeMillis() / 1000L
        return expiresIn?.let { currentTime >= it } ?: true
    }
}