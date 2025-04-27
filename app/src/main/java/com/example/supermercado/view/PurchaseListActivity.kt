package com.example.supermercado.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.supermercado.R
import com.example.supermercado.service.ServiceLocator
import com.example.supermercado.adapter.AdapterPurchase
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PurchaseListActivity : AppCompatActivity() {

    private val purchaseService = ServiceLocator.purchaseService
    private lateinit var adapterProduct: AdapterPurchase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_purchase_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))

        val recyclerViewProducts = findViewById<RecyclerView>(R.id.recyclerview_products)
        recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        recyclerViewProducts.setHasFixedSize(true)

        adapterProduct = AdapterPurchase()
        recyclerViewProducts.adapter = adapterProduct
        adapterProduct.setProducts(purchaseService.getAll())

        adapterProduct.setOnEditPurchaseListener {
            val intent = Intent(this, PurchaseItemActivity::class.java)
            intent.putExtra("purchase", it)
            startActivity(intent)
        }

        adapterProduct.setOnDeletePurchaseListener {
            purchaseService.delete(it)
        }

        val btnAddPurchase = findViewById<FloatingActionButton>(R.id.btn_add_purchase)
        btnAddPurchase.setOnClickListener {
            val intent = Intent(this, PurchaseItemActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        adapterProduct.setProducts(purchaseService.getAll())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                adapterProduct.setProducts(purchaseService.getAll())
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}