package com.example.supermercado.external.appscript.api

import android.util.Log
import com.example.supermercado.external.PurchaseApi
import com.example.supermercado.external.appscript.mapper.AppScriptPurchaseMapper
import com.example.supermercado.external.appscript.model.AppScriptPurchaseDto
import com.example.supermercado.model.Purchase
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import java.lang.reflect.Type
import java.util.UUID


class AppScriptPurchaseApi(private val httpClient: HttpClient) : AppScriptAbstractApi(), PurchaseApi {

    override suspend fun getPurchasePending(): List<Purchase> {
        val response = httpClient.get(getBaseUrl()) {
            parameter("query", "purchase_pending")
            parameter("token", getToken())
        }
        val appScriptPurchaseDtoList: List<AppScriptPurchaseDto> = treatResponse(response, httpClient).body()
        return AppScriptPurchaseMapper.map(appScriptPurchaseDtoList)
    }

    override suspend fun getPurchaseCart(): List<Purchase> {
        val response = httpClient.get(getBaseUrl()) {
            parameter("query", "purchase_cart")
            parameter("token", getToken())
        }
        val appScriptPurchaseDtoList = treatResponse(response, httpClient).body<List<AppScriptPurchaseDto>>()
        return AppScriptPurchaseMapper.map(appScriptPurchaseDtoList)
    }

    override suspend fun getByUuid(uuid: UUID): Purchase {
        val response = httpClient.get(getBaseUrl()) {
            parameter("query", "purchase_uuid")
            parameter("token", getToken())
            parameter("uuid", uuid.toString())
        }
        val appScriptPurchaseDto: AppScriptPurchaseDto = treatResponse(response, httpClient).body()
        return AppScriptPurchaseMapper.map(appScriptPurchaseDto)
    }

    override suspend fun insert(purchase: Purchase): Purchase {
        purchase.uuid = null
        return save(purchase)
    }

    override suspend fun update(purchase: Purchase): Purchase {
        return save(purchase)
    }

    private suspend fun save(purchase: Purchase): Purchase {
        val body: AppScriptPurchaseDto = AppScriptPurchaseMapper.map(purchase)
        Log.i("AppScriptPurchaseApi", "Saving purchase: $body")
        val response = httpClient.post(getBaseUrl()) {
            parameter("query", "purchase")
            parameter("token", getToken())
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        val appScriptPurchaseDto: AppScriptPurchaseDto = treatResponse(response, httpClient).body()
        return AppScriptPurchaseMapper.map(appScriptPurchaseDto)
    }

    override suspend fun delete(uuid: UUID) {
        val response = httpClient.get(getBaseUrl()) {
            parameter("query", "purchase_delete")
            parameter("token", getToken())
            parameter("uuid", uuid.toString())
        }
        val body: Map<String, Boolean> = treatResponse(response, httpClient).body()
        if (!body.containsKey("success") || body["success"] != true) {
            throw RuntimeException("Error deleting purchase")
        }
    }
}