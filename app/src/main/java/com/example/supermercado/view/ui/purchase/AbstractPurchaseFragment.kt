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

abstract class AbstractPurchaseFragment : Fragment()  {

    private val purchaseService = ServiceLocator.purchaseService
    private lateinit var adapterProduct: AdapterPurchase
    private lateinit var purchaseItemLancher: ActivityResultLauncher<Intent>;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerViewProducts = view.findViewById<RecyclerView>(R.id.recyclerview_products)
        recyclerViewProducts.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewProducts.setHasFixedSize(true)

        adapterProduct = AdapterPurchase(requireContext(), parentFragmentManager)
        recyclerViewProducts.adapter = adapterProduct
        adapterProduct.setOnLoadListListener {
            try {
                getPurchaseList()
            } catch (e: Exception) {
                Log.e("AdapterPurchase", "Error loading purchase list", e)
                val errorMessage = getString(R.string.purchase_get_error)
                MessageUtil.showErrorMessage(errorMessage, requireContext())
                emptyList()
            }
        }

        adapterProduct.setOnEditPurchaseListener {
            val intent = Intent(requireContext(), PurchaseItemActivity::class.java)
            intent.putExtra("purchase", it)
            purchaseItemLancher.launch(intent)
        }

        adapterProduct.setOnButtonCartClickedListener {
            purchaseService.update(it)
        }

        val btnAddPurchase = view.findViewById<FloatingActionButton>(R.id.btn_add_purchase)
        btnAddPurchase.setOnClickListener {
            val intent = Intent(requireContext(), PurchaseItemActivity::class.java)
            purchaseItemLancher.launch(intent)
        }

        definePurchaseItemLancher()
        adapterProduct.refresh()
    }

    protected fun getAdapterProduct(): AdapterPurchase {
        return adapterProduct
    }

    private fun definePurchaseItemLancher() {
        purchaseItemLancher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data = result.data
                if (data != null && data.getBooleanExtra("updated", false)) {
                    adapterProduct.refresh()
                }
            }
        }
    }

    protected abstract fun getPurchaseList(): List<Purchase>;
}