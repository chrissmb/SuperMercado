package com.example.supermercado.external

import com.example.supermercado.model.Token

interface LoginApi {
    suspend fun login(username: String, password: String): Token
}