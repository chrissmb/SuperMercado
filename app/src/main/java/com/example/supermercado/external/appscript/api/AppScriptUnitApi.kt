package com.example.supermercado.external.appscript.api

import com.example.supermercado.external.UnitApi
import com.example.supermercado.external.appscript.mapper.AppScriptUnitMapper
import com.example.supermercado.model.PurchaseUnit
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class AppScriptUnitApi(private val httpClient: HttpClient): AppScriptAbstractApi(), UnitApi {

    override suspend fun getUnits(): List<PurchaseUnit> {
        val response = httpClient.get(getBaseUrl()) {
            parameter("query", "unit")
            parameter("token", getToken())
        }
        val units: List<String> = treatResponse(response, httpClient).body()
        return AppScriptUnitMapper.map(units)
    }
}