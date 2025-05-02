package com.example.supermercado.external.appscript.model

import kotlinx.serialization.Serializable

@Serializable
data class AppScriptLoginRequest(
    val credential: String,
)
