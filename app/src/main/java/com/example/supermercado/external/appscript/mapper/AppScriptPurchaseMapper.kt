package com.example.supermercado.external.appscript.mapper

import com.example.supermercado.model.Category
import com.example.supermercado.model.Product
import com.example.supermercado.model.Purchase
import com.example.supermercado.model.PurchaseUnit
import com.example.supermercado.external.appscript.model.AppScriptPurchaseDto
import java.util.UUID

object AppScriptPurchaseMapper {

    fun map(appScriptPurchaseDto: AppScriptPurchaseDto): Purchase {
        return Purchase(
            uuid = UUID.fromString(appScriptPurchaseDto.uuid),
            product = Product(
                null,
                appScriptPurchaseDto.product,
                if (appScriptPurchaseDto.category.isNullOrBlank()) null else Category(null, appScriptPurchaseDto.category),
            ),
            quantity = appScriptPurchaseDto.quantity,
            unit = if (appScriptPurchaseDto.unit.isNullOrBlank()) null else PurchaseUnit(null, appScriptPurchaseDto.unit),
            cart = appScriptPurchaseDto.cart
        )
    }

    fun map(purchase: Purchase): AppScriptPurchaseDto {
        return AppScriptPurchaseDto(
            uuid = purchase.uuid?.toString(),
            product = purchase.product.name,
            quantity = purchase.quantity,
            unit = purchase.unit?.name,
            category = purchase.product.category?.name,
            cart = purchase.cart
        )
    }

    fun map(appScriptPurchaseDtoList: List<AppScriptPurchaseDto>): List<Purchase> {
        return appScriptPurchaseDtoList.map { map(it) }
    }
}