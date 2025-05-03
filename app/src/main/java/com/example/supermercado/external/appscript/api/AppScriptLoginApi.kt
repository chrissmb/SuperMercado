package com.example.supermercado.external.appscript.api

import android.util.Base64
import android.util.Log
import com.example.supermercado.BuildConfig
import com.example.supermercado.external.LoginApi
import com.example.supermercado.external.appscript.mapper.AppScriptTokenMapper
import com.example.supermercado.external.appscript.model.AppScriptLoginRequest
import com.example.supermercado.external.appscript.model.AppScriptTokenDto
import com.example.supermercado.model.Token
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AppScriptLoginApi(private val httpClient: HttpClient) : AppScriptAbstractApi(), LoginApi {

    override suspend fun login(username: String, password: String): Token {
        val rawCredentials = "$username:$password"
        val credentials = Base64.encodeToString(rawCredentials.toByteArray(), Base64.DEFAULT)
        val response = httpClient.post(getBaseUrl()) {
            parameter("query", "login")
            contentType(ContentType.Application.Json)
            setBody(AppScriptLoginRequest(credentials))
        }
        val appScriptTokenDto: AppScriptTokenDto = treatResponse(response, httpClient).body()
        return AppScriptTokenMapper.map(appScriptTokenDto)
    }
}