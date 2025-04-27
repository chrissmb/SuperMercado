package com.example.supermercado.service

import com.example.supermercado.model.Product
import com.example.supermercado.model.Purchase
import com.example.supermercado.model.PurchaseUnit
import java.util.UUID

class PurchaseService {

    val purchases = mutableListOf(Purchase(
        uuid = UUID.randomUUID(),
        product = Product(1, "Chocolate"),
        quantity = 2.0,
        unit = PurchaseUnit(1, "Unidade"),
        cart = false
    ),
        Purchase(
            uuid = UUID.randomUUID(),
            product = Product(1, "Queijo"),
            quantity = 0.2,
            unit = PurchaseUnit(2, "KG"),
            cart = true
        ),
        Purchase(
            uuid = UUID.randomUUID(),
            product = Product(1, "Refrigerante 2L"),
            quantity = 3.0,
            unit = PurchaseUnit(1, "Unidade"),
            cart = false
        ),
        Purchase(
            uuid = UUID.randomUUID(),
            product = Product(1, "Refrigerante lata"),
            quantity = 5.0,
            unit = null,
            cart = false
        ),)


    fun getAll(): List<Purchase> {
        return purchases
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