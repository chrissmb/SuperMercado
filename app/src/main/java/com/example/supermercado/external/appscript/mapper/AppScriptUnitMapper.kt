package com.example.supermercado.external.appscript.mapper

import com.example.supermercado.model.PurchaseUnit

object AppScriptUnitMapper {

    fun map(units: List<String>): List<PurchaseUnit> {
        return units.map { unit ->
            PurchaseUnit(
                id = null,
                name = unit
            )
        }
    }
}