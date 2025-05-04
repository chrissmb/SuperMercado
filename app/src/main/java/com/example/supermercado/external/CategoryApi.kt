package com.example.supermercado.external

import com.example.supermercado.model.Category

interface CategoryApi {

    suspend fun getCategories(): List<Category>
}