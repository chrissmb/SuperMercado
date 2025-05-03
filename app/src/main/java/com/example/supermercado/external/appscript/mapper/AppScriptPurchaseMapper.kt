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
            UUID.fromString(appScriptPurchaseDto.uuid),
            Product(
                null,
                appScriptPurchaseDto.product,
                if (appScriptPurchaseDto.category.isNullOrBlank()) null else Category(null, appScriptPurchaseDto.category),
            ),
            appScriptPurchaseDto.quantity,
            if (appScriptPurchaseDto.unit.isNullOrBlank()) null else PurchaseUnit(null, appScriptPurchaseDto.unit),
            appScriptPurchaseDto.cart
        )
    }

    fun map(purchase: Purchase): AppScriptPurchaseDto {
        return AppScriptPurchaseDto(
            purchase.uuid?.toString(),
            purchase.product.name,
            purchase.quantity,
            purchase.unit?.name,
            purchase.product.category?.name,
            purchase.cart
        )
    }

    fun map(appScriptPurchaseDtoList: List<AppScriptPurchaseDto>): List<Purchase> {
        return appScriptPurchaseDtoList.map { map(it) }
    }
}