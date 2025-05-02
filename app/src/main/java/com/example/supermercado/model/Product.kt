package com.example.supermercado.model

import java.io.Serializable

data class Product(
    var id: Int?,
    var name: String,
    var category: Category?
): Serializable