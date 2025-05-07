package com.example.supermercado.view.ui.purchase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.supermercado.R
import com.example.supermercado.adapter.AdapterPurchase
import com.example.supermercado.service.ServiceLocator
import com.example.supermercado.util.MessageUtil
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

        adapterProduct = AdapterPurchase(requireContext(), parentFragmentManager)
        recyclerViewProducts.adapter = adapterProduct
        adapterProduct.setOnLoadListListener {
            try {
                purchaseService.getPurchasePending()
            } catch (e: Exception) {
                Log.e("AdapterPurchase", "Error loading purchase list", e)
                val errorMessage = getString(R.string.purchase_get_error)
                MessageUtil.showErrorMessage(errorMessage, requireContext())
                emptyList()
            }
        }
        adapterProduct.refresh()

        adapterProduct.setOnEditPurchaseListener {
            val intent = Intent(requireContext(), PurchaseItemActivity::class.java)
            intent.putExtra("purchase", it)
            startActivity(intent)
        }

        adapterProduct.setOnButtonCartClickedListener {
            purchaseService.update(it)
        }

        val btnAddPurchase = view.findViewById<FloatingActionButton>(R.id.btn_add_purchase)
        btnAddPurchase.setOnClickListener {
            val intent = Intent(requireContext(), PurchaseItemActivity::class.java)
            startActivity(intent)
        }

        adapterProduct.refresh()
    }

    override fun onResume() {
        super.onResume()
        adapterProduct.refresh()
    }
}