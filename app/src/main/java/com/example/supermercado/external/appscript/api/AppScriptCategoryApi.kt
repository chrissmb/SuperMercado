package com.example.supermercado.external.appscript.api

import com.example.supermercado.external.CategoryApi
import com.example.supermercado.external.appscript.mapper.AppScriptCategoryMapper
import com.example.supermercado.model.Category
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class AppScriptCategoryApi(private val httpClient: HttpClient): AppScriptAbstractApi(), CategoryApi {

    override suspend fun getCategories(): List<Category> {
        val response = httpClient.get(getBaseUrl()) {
            parameter("query", "category")
            parameter("token", getToken())
        }
        val categories: List<String> = treatResponse(response, httpClient).body()
        return AppScriptCategoryMapper.map(categories)
    }
}