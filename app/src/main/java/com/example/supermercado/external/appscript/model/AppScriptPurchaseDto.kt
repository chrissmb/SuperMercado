package com.example.supermercado.external.appscript.model

import kotlinx.serialization.Serializable

@Serializable
data class AppScriptPurchaseDto(
    val uuid: String?,
    val product: String,
    val quantity: Double?,
    val unit: String?,
    val category: String?,
    val cart: Boolean,
)