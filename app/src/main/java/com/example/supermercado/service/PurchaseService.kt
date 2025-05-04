package com.example.supermercado.service

import com.example.supermercado.model.Category
import com.example.supermercado.model.Product
import com.example.supermercado.model.Purchase
import com.example.supermercado.model.PurchaseUnit
import com.example.supermercado.external.PurchaseApi
import com.example.supermercado.service.exception.BusinessException
import java.util.UUID

class PurchaseService(private val purchaseApi: PurchaseApi) {

    suspend fun getPurchasePending(): List<Purchase> {
        return purchaseApi.getPurchasePending()
    }

    suspend fun getPurchaseCart(): List<Purchase> {
        return purchaseApi.getPurchaseCart()
    }

    suspend fun insert(purchase: Purchase): Purchase {
        if (purchase.product.name.isBlank()) {
            throw BusinessException("Produto não preenchido")
        }
        return purchaseApi.insert(purchase)
    }

    suspend fun update(purchase: Purchase): Purchase {
        if (purchase.product.name.isBlank()) {
            throw BusinessException("Produto não preenchido")
        }
        return purchaseApi.update(purchase)
    }

    suspend fun delete(uuid: UUID) {
        purchaseApi.delete(uuid)
    }
}