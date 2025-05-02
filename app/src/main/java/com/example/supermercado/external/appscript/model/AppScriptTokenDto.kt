package com.example.supermercado.external.appscript.model

import kotlinx.serialization.Serializable

@Serializable
data class AppScriptTokenDto(
    val token: String,
    val expire_in: Int,
)
