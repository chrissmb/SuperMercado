package com.example.supermercado.util

import android.content.Context
import com.example.supermercado.R
import com.example.supermercado.service.exception.BusinessException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ServiceCallUtil {

    fun treatServiceCall(context: Context, callback: suspend () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                callback.invoke()
            } catch (e: BusinessException) {
                val unknownErrorMessage = context.getString(R.string.unknwon_error)
                MessageUtil.showErrorMessage(e.message ?: unknownErrorMessage, context)
            } catch (e: Exception) {
                val unknownErrorMessage = context. getString(R.string.unknwon_error)
                MessageUtil.showErrorMessage(unknownErrorMessage, context)
            }
        }
    }
}