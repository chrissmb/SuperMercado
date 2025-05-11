package com.example.supermercado.util

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.example.supermercado.R
import com.example.supermercado.service.exception.BusinessException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ServiceCallUtil {

    fun treatServiceCall(appCompatActivity: AppCompatActivity, callback: suspend () -> Unit) {
        treatServiceCall(appCompatActivity, appCompatActivity.supportFragmentManager, appCompatActivity.lifecycleScope, callback)
    }

    fun treatServiceCall(fragment: Fragment, callback: suspend () -> Unit) {
        treatServiceCall(fragment.requireContext(), fragment.parentFragmentManager, fragment.lifecycleScope, callback)
    }

    private fun treatServiceCall(context: Context, manager: FragmentManager, coroutineScope: CoroutineScope, callback: suspend () -> Unit) {
        coroutineScope.launch {
            val loadingDialog = LoadingDialogFragment()
            try {
                loadingDialog.show(manager, "Loading")
                callback()
            } catch (e: BusinessException) {
                val unknownErrorMessage = context.getString(R.string.unknwon_error)
                MessageUtil.showErrorMessage(e.message ?: unknownErrorMessage, context)
            } catch (e: Exception) {
                val unknownErrorMessage = context. getString(R.string.unknwon_error)
                MessageUtil.showErrorMessage(unknownErrorMessage, context)
            } finally {
                loadingDialog.dismiss()
            }
        }
    }
}