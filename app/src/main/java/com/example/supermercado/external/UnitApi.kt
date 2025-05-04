package com.example.supermercado.external

import com.example.supermercado.model.PurchaseUnit

interface UnitApi {

    suspend fun getUnits(): List<PurchaseUnit>
}