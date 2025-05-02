package com.example.supermercado.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.supermercado.R

object MessageUtil {

    fun showMessage(message: String, context: Context) {
        val dialog = AlertDialog.Builder(context)
        val title = context.getString(R.string.title_message_information)
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setPositiveButton("Ok") { _, _ -> }
        dialog.show()
    }

    fun showErrorMessage(message: String, context: Context) {
        val dialog = AlertDialog.Builder(context)
        val title = context.getString(R.string.title_message_error)
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setPositiveButton("Ok") { _, _ -> }
        dialog.show()
    }

    fun showConfirmMessage(message: String, context: Context, onYes: () -> Unit) {
        val dialog = AlertDialog.Builder(context)
        val title = context.getString(R.string.title_message_confirmation)
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setPositiveButton("Sim") { _, _ ->
            onYes.invoke()
        }
        dialog.setNegativeButton("Não", null)
        dialog.show()
    }
}