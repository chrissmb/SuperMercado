package com.example.supermercado.view.ui.purchase

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.supermercado.R
import com.example.supermercado.adapter.AdapterPurchase
import com.example.supermercado.service.ServiceLocator
import com.example.supermercado.view.PurchaseItemActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class PurchasePendingFragment : Fragment() {

    private val purchaseService = ServiceLocator.purchaseService
    private lateinit var adapterProduct: AdapterPurchase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_purchase_pending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerViewProducts = view.findViewById<RecyclerView>(R.id.recyclerview_products)
        recyclerViewProducts.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewProducts.setHasFixedSize(true)

        adapterProduct = AdapterPurchase()
        recyclerViewProducts.adapter = adapterProduct
        adapterProduct.setProducts(purchaseService.getAll())

        adapterProduct.setOnEditPurchaseListener {
            val intent = Intent(requireContext(), PurchaseItemActivity::class.java)
            intent.putExtra("purchase", it)
            startActivity(intent)
        }

        adapterProduct.setOnDeletePurchaseListener {
            purchaseService.delete(it)
        }

        val btnAddPurchase = view.findViewById<FloatingActionButton>(R.id.btn_add_purchase)
        btnAddPurchase.setOnClickListener {
            val intent = Intent(requireContext(), PurchaseItemActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        adapterProduct.setProducts(purchaseService.getAll())
    }
}