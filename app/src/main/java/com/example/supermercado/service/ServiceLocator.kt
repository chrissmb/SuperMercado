package com.example.supermercado.service

object ServiceLocator {

    val purchaseService by lazy { PurchaseService() }
}