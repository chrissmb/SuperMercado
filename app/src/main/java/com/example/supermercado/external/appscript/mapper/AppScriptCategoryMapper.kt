package com.example.supermercado.external.appscript.mapper

import com.example.supermercado.model.Category

object AppScriptCategoryMapper {

    fun map(categories: List<String>): List<Category> {
        return categories.map { category ->
            Category(
                id = null,
                name = category
            )
        }
    }
}