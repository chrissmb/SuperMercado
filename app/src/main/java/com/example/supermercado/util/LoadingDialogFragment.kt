package com.example.supermercado.util

import android.app.Dialog
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class LoadingDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val progressBar = ProgressBar(requireContext())
        return AlertDialog.Builder(requireContext())
            .setView(progressBar)
            .setCancelable(false)
            .create()
    }
}