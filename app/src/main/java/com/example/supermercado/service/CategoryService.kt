package com.example.supermercado.service

import com.example.supermercado.external.CategoryApi
import com.example.supermercado.model.Category

class CategoryService(private val categoryApi: CategoryApi) {

    suspend fun getCategories(): List<Category> {
        return categoryApi.getCategories()
    }
}