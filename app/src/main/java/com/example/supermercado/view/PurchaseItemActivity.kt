package com.example.supermercado.view

import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.supermercado.R
import com.example.supermercado.model.Category
import com.example.supermercado.model.Product
import com.example.supermercado.service.ServiceLocator
import com.example.supermercado.model.Purchase
import com.example.supermercado.model.PurchaseUnit
import com.example.supermercado.util.MessageUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PurchaseItemActivity : AppCompatActivity() {

    private val purchaseService = ServiceLocator.purchaseService

    var purchase: Purchase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_purchase_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        purchase = intent.getSerializableExtra("purchase") as? Purchase

        val etProductName = findViewById<EditText>(R.id.et_product_name)
        val etQuantity = findViewById<EditText>(R.id.et_quantity)
        val etUnit = findViewById<EditText>(R.id.et_unit)
        val etCategory = findViewById<EditText>(R.id.et_category)

        if (purchase != null) {
            etProductName.setText(purchase?.product?.name)
            etQuantity.setText(purchase?.quantity.toString())
            etUnit.setText(purchase?.unit?.name)
            etCategory.setText(purchase?.product?.category?.name)
        }

        val btnSave = findViewById<FloatingActionButton>(R.id.btn_save_purchase)
        btnSave.setOnClickListener {
            val productName = etProductName.text.toString()
            val quantity = etQuantity.text.toString().toDoubleOrNull()
            val unit = etUnit.text.toString()
            var category = etCategory.text.toString()

            if (purchase == null) {
                purchase = Purchase(
                    null,
                    Product(
                        null,
                        productName,
                        Category(null, category)
                    ),
                    quantity,
                    PurchaseUnit(1, unit),
                    false
                )
                CoroutineScope(Dispatchers.Main).launch {
                    purchaseService.insert(purchase!!)
                    finish()
                }
            } else {
                purchase?.product = Product(null, productName, Category(null, category))
                purchase?.quantity = quantity ?: 0.0
                purchase?.unit = PurchaseUnit(null, unit)
                CoroutineScope(Dispatchers.Main).launch {
                    purchaseService.update(purchase!!)
                    finish()
                }
            }
        }

        val btnDelete = findViewById<FloatingActionButton>(R.id.btn_delete_purchase)
        btnDelete.setOnClickListener {
            if (purchase?.uuid == null) {
                throw IllegalStateException("Não é possível excluir um item que não existe.");
            }
            val message = getString(R.string.message_confirm_purchase_delete)
            MessageUtil.showConfirmMessage(message, this) {
                CoroutineScope(Dispatchers.Main).launch {
                    purchaseService.delete(purchase!!.uuid!!)
                    finish()
                }
            }
        }
    }
}