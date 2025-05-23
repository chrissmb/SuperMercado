package com.example.supermercado.view.ui.purchase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.supermercado.R
import com.example.supermercado.adapter.AdapterPurchase
import com.example.supermercado.model.Purchase
import com.example.supermercado.service.PurchaseService
import com.example.supermercado.service.ServiceLocator
import com.example.supermercado.util.MessageUtil
import com.example.supermercado.util.SharedViewModel
import com.example.supermercado.view.PurchaseItemActivity

abstract class AbstractPurchaseFragment : Fragment() {

    private val purchaseService = ServiceLocator.purchaseService
    private lateinit var adapterProduct: AdapterPurchase
    private lateinit var purchaseItemLancher: ActivityResultLauncher<Intent>;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerViewProducts = view.findViewById<RecyclerView>(R.id.recyclerview_products)
        recyclerViewProducts.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewProducts.setHasFixedSize(true)

        adapterProduct = AdapterPurchase(this)
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

        val sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel.refreshAction.observe(viewLifecycleOwner) { action ->
            if (action) {
                adapterProduct.refresh()
            }
        }

        definePurchaseItemLancher()
    }

    protected fun getAdapterProduct(): AdapterPurchase {
        return adapterProduct
    }

    protected fun getPurchaseItemLancher(): ActivityResultLauncher<Intent> {
        return purchaseItemLancher
    }

    protected fun getPurchaseService(): PurchaseService {
        return purchaseService
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

    protected abstract suspend fun getPurchaseList(): List<Purchase>;
}