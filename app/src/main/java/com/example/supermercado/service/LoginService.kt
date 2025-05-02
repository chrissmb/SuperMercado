package com.example.supermercado.service

import android.content.Context
import android.util.Log
import com.example.supermercado.model.Token
import com.example.supermercado.external.LoginApi
import com.example.supermercado.util.TokenCacheUtil
import com.google.gson.Gson
import kotlinx.coroutines.flow.first

class LoginService(
    private val loginApi: LoginApi,
    private val localDataService: LocalDataService
) {

    suspend fun login(username: String, password: String, context: Context) {
        val token = loginApi.login(username, password)
        Log.i("LoginService", "Token: ${token.value}, Expires In: ${token.expiresIn}")

        TokenCacheUtil.token = token.value
        TokenCacheUtil.expiresIn = token.expiresIn

        val tokenJson = Gson().toJson(token)
        localDataService.saveToken(context, tokenJson)
    }

    suspend fun isTokenExpired(context: Context): Boolean {
        val tokenJson = localDataService.getToken(context).first()
        val token = Gson().fromJson(tokenJson, Token::class.java)
        TokenCacheUtil.token = token?.value
        TokenCacheUtil.expiresIn = token?.expiresIn
        val currentTime = System.currentTimeMillis() / 1000
        return token?.expiresIn == null || currentTime >= token.expiresIn
    }

    suspend fun refreshTokenCache(context: Context) {
        val tokenJson = localDataService.getToken(context).first()
        val token = Gson().fromJson(tokenJson, Token::class.java)
        TokenCacheUtil.token = token?.value
        TokenCacheUtil.expiresIn = token?.expiresIn
    }
}