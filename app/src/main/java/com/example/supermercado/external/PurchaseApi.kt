package com.example.supermercado.external

import com.example.supermercado.model.Purchase

interface PurchaseApi {
    suspend fun getPurchasePending(): List<Purchase>
//    suspend fun getPurchaseCart(): List<Purchase>
//    suspend fun getByUuid(uuid: String): Purchase
//    suspend fun insert(purchase: Purchase)
//    suspend fun delete(purchase: Purchase)
//    suspend fun update(purchase: Purchase)
}