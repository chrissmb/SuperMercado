package com.example.supermercado.service

import com.example.supermercado.external.UnitApi
import com.example.supermercado.model.PurchaseUnit

class UnitService(private val unitApi: UnitApi) {

    suspend fun getUnits(): List<PurchaseUnit> {
        return unitApi.getUnits()
    }
}