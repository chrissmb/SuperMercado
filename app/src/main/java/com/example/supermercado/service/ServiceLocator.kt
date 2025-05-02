package com.example.supermercado.service

import android.content.Context
import com.example.supermercado.external.appscript.api.AppScriptLoginApi
import com.example.supermercado.external.appscript.api.AppScriptPurchaseApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRedirect
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.gson.gson

object ServiceLocator {

    val purchaseService by lazy { PurchaseService(purchaseApi) }
    val loginService by lazy { LoginService(loginApi, localDataService) }
    val localDataService by lazy { LocalDataService() }

    val purchaseApi by lazy { AppScriptPurchaseApi(httpClient) }
    val loginApi by lazy { AppScriptLoginApi(httpClient) }

    val httpClient by lazy {
        HttpClient(CIO) {
            followRedirects = true
            install(ContentNegotiation) {
                gson()
            }
            install(io.ktor.client.plugins.logging.Logging) {
                level = io.ktor.client.plugins.logging.LogLevel.ALL
            }
            install(HttpRedirect) {

            }
        }
    }
}
