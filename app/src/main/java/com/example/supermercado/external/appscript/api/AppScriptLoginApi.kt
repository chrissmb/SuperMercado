package com.example.supermercado.external.appscript.api

import android.util.Base64
import android.util.Log
import com.example.supermercado.BuildConfig
import com.example.supermercado.external.LoginApi
import com.example.supermercado.external.appscript.api.util.AppScriptApiUtil
import com.example.supermercado.external.appscript.mapper.AppScriptTokenMapper
import com.example.supermercado.external.appscript.model.AppScriptLoginRequest
import com.example.supermercado.external.appscript.model.AppScriptTokenDto
import com.example.supermercado.model.Token
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AppScriptLoginApi(private val httpClient: HttpClient) : LoginApi {

    private val baseUrl: String = BuildConfig.appscriptBaseUrl

    override suspend fun login(username: String, password: String): Token {
        val rawCredentials = "$username:$password"
        val credentials = Base64.encodeToString(rawCredentials.toByteArray(), Base64.DEFAULT)

        httpClient.config {
            followRedirects = true
        }

        var response = httpClient.post("$baseUrl?query=login") {
            contentType(ContentType.Application.Json)
            setBody(AppScriptLoginRequest(credentials))
        }

        val bodyString = AppScriptApiUtil.treatResponse(response, httpClient)
        val appScriptTokenDto = Gson().fromJson(bodyString, AppScriptTokenDto::class.java)
        return AppScriptTokenMapper.map(appScriptTokenDto)
    }
}