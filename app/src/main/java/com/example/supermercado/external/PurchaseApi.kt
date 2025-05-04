package com.example.supermercado.external

import com.example.supermercado.model.Purchase
import java.util.UUID

interface PurchaseApi {
    suspend fun getPurchasePending(): List<Purchase>
    suspend fun getPurchaseCart(): List<Purchase>
    suspend fun getByUuid(uuid: UUID): Purchase
    suspend fun insert(purchase: Purchase): Purchase
    suspend fun update(purchase: Purchase): Purchase
    suspend fun delete(uuid: UUID)
    suspend fun completePurchase()
}