package com.example.supermercado.service

import com.example.supermercado.model.Category
import com.example.supermercado.model.Product
import com.example.supermercado.model.Purchase
import com.example.supermercado.model.PurchaseUnit
import com.example.supermercado.external.PurchaseApi
import java.util.UUID

class PurchaseService(private val purchaseApi: PurchaseApi) {

    val purchases = mutableListOf(
        Purchase(
        uuid = UUID.randomUUID(),
        product = Product(1, "Chocolate", Category(null, "Doce")),
        quantity = 2.0,
        unit = PurchaseUnit(1, "Unidade"),
        cart = false
        ),
        Purchase(
            uuid = UUID.randomUUID(),
            product = Product(1, "Queijo", Category(null, "Comida")),
            quantity = 0.2,
            unit = PurchaseUnit(2, "KG"),
            cart = true
        ),
        Purchase(
            uuid = UUID.randomUUID(),
            product = Product(1, "Refrigerante 2L", Category(null, "Bebida")),
            quantity = 3.0,
            unit = PurchaseUnit(1, "Unidade"),
            cart = false
        ),
        Purchase(
            uuid = UUID.randomUUID(),
            product = Product(1, "Refrigerante lata", Category(null, "Bebida")),
            quantity = 5.0,
            unit = null,
            cart = false
        ),
    )



    suspend fun getAll(): List<Purchase> {
        return purchaseApi.getPurchasePending()
    }

    fun insert(purchase: Purchase) {
        purchase.uuid = UUID.randomUUID()
        purchases.add(purchase)
    }

    fun delete(purchase: Purchase) {
        purchases.remove(purchase)
    }

    fun update(purchase: Purchase) {
        val index = purchases.indexOfFirst { it.uuid == purchase.uuid }
        if (index != -1) {
            purchases[index] = purchase
        }
    }
}