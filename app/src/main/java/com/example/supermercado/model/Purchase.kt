package com.example.supermercado.model

import java.io.Serializable
import java.util.UUID


data class Purchase(
    var uuid: UUID?,
    var product: Product,
    var quantity: Double,
    var unit: PurchaseUnit?,
    var cart: Boolean,
): Serializable
