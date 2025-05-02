package com.example.supermercado.external.appscript.api.util

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

object AppScriptApiUtil {

    suspend fun treatResponse(response: HttpResponse, httpClient: HttpClient): String {
        var responseRedirect = response
        if (response.status.value == 302) {
            val location = response.headers["Location"]
            Log.i("AppScriptLoginApi", "Redirecting to: $location")
            if (location == null) {
                throw RuntimeException("Redirect location not found")
            }
            responseRedirect = httpClient.get(response.headers["Location"]!!)
        }
        val bodyString = responseRedirect.body<String>()
        Log.i("AppScriptLoginApi", "Status: ${response.status.value} Response: $bodyString")
        if (responseRedirect.status.value != 200 || bodyString.isNullOrBlank() || bodyString.contains("errorMessage")) {
            throw RuntimeException("Login failed")
        }
        return bodyString
    }
}