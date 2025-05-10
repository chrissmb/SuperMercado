package com.example.supermercado.view.ui.purchase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.supermercado.R
import com.example.supermercado.adapter.AdapterPurchase
import com.example.supermercado.model.Purchase
import com.example.supermercado.service.ServiceLocator
import com.example.supermercado.util.MessageUtil
import com.example.supermercado.view.PurchaseItemActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class PurchasePendingFragment : AbstractPurchaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_purchase_pending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAddPurchase = view.findViewById<FloatingActionButton>(R.id.btn_add_purchase)
        btnAddPurchase.setOnClickListener {
            val intent = Intent(requireContext(), PurchaseItemActivity::class.java)
            getPurchaseItemLancher().launch(intent)
        }

        getAdapterProduct().refresh()
    }

    override suspend fun getPurchaseList(): List<Purchase> {
        return getPurchaseService().getPurchasePending()
    }
}