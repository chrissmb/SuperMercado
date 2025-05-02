package com.example.supermercado.external.appscript.mapper

import com.example.supermercado.external.appscript.model.AppScriptTokenDto
import com.example.supermercado.model.Token

object AppScriptTokenMapper {

    fun map(appScriptTokenDto: AppScriptTokenDto): Token {
        return Token(appScriptTokenDto.token, appScriptTokenDto.expire_in)
    }
}