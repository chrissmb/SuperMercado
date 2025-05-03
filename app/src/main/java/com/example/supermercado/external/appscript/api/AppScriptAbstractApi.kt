package com.example.supermercado.external.appscript.api

import android.util.Log
import com.example.supermercado.BuildConfig
import com.example.supermercado.util.TokenCacheUtil
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

abstract class AppScriptAbstractApi {

    fun getBaseUrl(): String {
        return BuildConfig.appscriptBaseUrl
    }

    fun getToken(): String {
        if (TokenCacheUtil.token == null) {
            throw RuntimeException("Token not found")
        }
        return TokenCacheUtil.token!!
    }

    suspend fun treatResponse(response: HttpResponse, httpClient: HttpClient): HttpResponse {
        var responseRedirect = response
        if (response.status.value == 302) {
            val location = response.headers["Location"]
                ?: throw RuntimeException("Redirect location not found")
            Log.i("AppScriptAbstractApi", "Redirecting to: $location")
            responseRedirect = httpClient.get(response.headers["Location"]!!)
        }
        val bodyString = responseRedirect.body<String>()
        Log.i("AppScriptAbstractApi", "Status: ${response.status.value} Response: $bodyString")
        if (responseRedirect.status.value != 200 || bodyString.contains("errorMessage")) {
            throw RuntimeException("Response error")
        }
        return responseRedirect
    }
}