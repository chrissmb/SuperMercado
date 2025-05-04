package com.example.supermercado.service

import com.example.supermercado.external.appscript.api.AppScriptCategoryApi
import com.example.supermercado.external.appscript.api.AppScriptLoginApi
import com.example.supermercado.external.appscript.api.AppScriptPurchaseApi
import com.example.supermercado.external.appscript.api.AppScriptUnitApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.gson.gson

object ServiceLocator {

    val loginService by lazy { LoginService(loginApi, localDataService) }
    val purchaseService by lazy { PurchaseService(purchaseApi) }
    val categoryService by lazy { CategoryService(categoryApi) }
    val unitService by lazy { UnitService(unitApi) }

    private val loginApi by lazy { AppScriptLoginApi(httpClient) }
    private val purchaseApi by lazy { AppScriptPurchaseApi(httpClient) }
    private val categoryApi by lazy { AppScriptCategoryApi(httpClient) }
    private val unitApi by lazy { AppScriptUnitApi(httpClient) }
    private val localDataService by lazy { LocalDataService() }

    private val httpClient by lazy {
        HttpClient(CIO) {
            followRedirects = true
            install(ContentNegotiation) {
                gson()
            }
        }
    }
}
