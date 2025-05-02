package com.example.supermercado.external.appscript.api

import com.example.supermercado.BuildConfig
import com.example.supermercado.external.PurchaseApi
import com.example.supermercado.model.Purchase
import com.example.supermercado.external.appscript.mapper.AppScriptPurchaseMapper
import com.example.supermercado.external.appscript.model.AppScriptPurchaseDto
import com.example.supermercado.util.TokenCacheUtil
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.runBlocking

class AppScriptPurchaseApi(private val httpClient: HttpClient) : PurchaseApi {

    private val baseUrl: String = BuildConfig.appscriptBaseUrl

    override suspend fun getPurchasePending(): List<Purchase> {
        val token = TokenCacheUtil.token
        val appScriptPurchaseDtoList: List<AppScriptPurchaseDto> = httpClient.get("$baseUrl?query=purchase&token=$token").body()
        return AppScriptPurchaseMapper.map(appScriptPurchaseDtoList)
    }

//    override fun getPurchaseCart(): List<Purchase> {
//        TODO("Not yet implemented")
//    }
//
//    override fun getByUuid(uuid: String): Purchase {
//        TODO("Not yet implemented")
//    }
//
//    override fun insert(purchase: Purchase) {
//        TODO("Not yet implemented")
//    }
//
//    override fun delete(purchase: Purchase) {
//        TODO("Not yet implemented")
//    }
//
//    override fun update(purchase: Purchase) {
//        TODO("Not yet implemented")
//    }
}