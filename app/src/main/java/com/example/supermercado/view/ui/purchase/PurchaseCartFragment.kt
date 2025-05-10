package com.example.supermercado.view.ui.purchase

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.supermercado.R
import com.example.supermercado.adapter.AdapterPurchase
import com.example.supermercado.model.Purchase
import com.example.supermercado.service.ServiceLocator
import com.example.supermercado.util.ServiceCallUtil
import com.example.supermercado.view.PurchaseItemActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PurchaseCartFragment : AbstractPurchaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_purchase_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCompletePurchase = view.findViewById<FloatingActionButton>(R.id.btn_complete_purchase)
        btnCompletePurchase.setOnClickListener {
            ServiceCallUtil.treatServiceCall(requireContext(), parentFragmentManager) {
                getPurchaseService().completePurchase()
                getAdapterProduct().refresh()
            }
        }
    }

    override suspend fun getPurchaseList(): List<Purchase> {
        return getPurchaseService().getPurchaseCart()
    }
}