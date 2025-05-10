package com.example.supermercado.view

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.supermercado.R
import com.example.supermercado.model.Category
import com.example.supermercado.model.Product
import com.example.supermercado.model.Purchase
import com.example.supermercado.model.PurchaseUnit
import com.example.supermercado.service.ServiceLocator
import com.example.supermercado.util.MessageUtil
import com.example.supermercado.util.ServiceCallUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PurchaseItemActivity : AppCompatActivity() {

    private val purchaseService = ServiceLocator.purchaseService
    private val categoryService = ServiceLocator.categoryService
    private val unitService = ServiceLocator.unitService

    private var purchase: Purchase? = null

    private lateinit var etProductName: EditText
    private lateinit var etQuantity: EditText
    private lateinit var autoCompleteUnit: AutoCompleteTextView
    private lateinit var autoCompleteCategory: AutoCompleteTextView

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

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        etProductName = findViewById<EditText>(R.id.et_product_name)
        etQuantity = findViewById<EditText>(R.id.et_quantity)
        autoCompleteUnit = findViewById<AutoCompleteTextView>(R.id.autoComplete_unit)
        autoCompleteCategory = findViewById<AutoCompleteTextView>(R.id.autoComplete_category)

        val btnSave = findViewById<FloatingActionButton>(R.id.btn_save_purchase)
        val btnDelete = findViewById<FloatingActionButton>(R.id.btn_delete_purchase)


        if (purchase != null) {
            fillFields()
        }

        btnSave.setOnClickListener {
            saveProduct()
        }

        btnDelete.setOnClickListener {
            deletePurchase()
        }

        defineCategoryAutoCompleteComponent()
        defineUnitAutoCompleteComponent()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun defineCategoryAutoCompleteComponent() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mutableListOf<String>())
        autoCompleteCategory.setAdapter(adapter)
        CoroutineScope(Dispatchers.Main).launch {
            val categoryNames = categoryService.getCategories().map { it.name }
            adapter.clear()
            adapter.addAll(categoryNames)
            autoCompleteCategory.setAdapter(adapter)
        }
    }

    private fun defineUnitAutoCompleteComponent() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mutableListOf<String>())
        autoCompleteUnit.setAdapter(adapter)
        CoroutineScope(Dispatchers.Main).launch {
            val unitNames = unitService.getUnits().map { it.name }
            adapter.clear()
            adapter.addAll(unitNames)
            autoCompleteUnit.setAdapter(adapter)
        }
    }

    private fun fillFields() {
        etProductName.setText(purchase?.product?.name)
        etQuantity.setText(purchase?.quantity.toString())
        autoCompleteUnit.setText(purchase?.unit?.name)
        autoCompleteCategory.setText(purchase?.product?.category?.name)
    }

    private fun deletePurchase() {
        if (purchase?.uuid == null) {
            throw IllegalStateException("Não é possível excluir um item que não existe.");
        }
        val message = getString(R.string.message_confirm_purchase_delete)
        MessageUtil.showConfirmMessage(message, this) {
            ServiceCallUtil.treatServiceCall(this@PurchaseItemActivity, supportFragmentManager) {
                purchaseService.delete(purchase!!.uuid!!)
                runOnUiThread {
                    val resultIntent = Intent()
                    resultIntent.putExtra("updated", true)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
            }
        }
    }

    private fun saveProduct() {
        val productName = etProductName.text.toString()
        val quantity = etQuantity.text.toString().toDoubleOrNull()
        val unit = autoCompleteUnit.text.toString()
        val category = autoCompleteCategory.text.toString()

        if (purchase == null) {
            purchase = mapPurchase(productName, category, quantity, unit)
            ServiceCallUtil.treatServiceCall(this, supportFragmentManager) {
                purchaseService.insert(purchase!!)
                finish()
            }
        } else {
            purchase?.product = Product(null, productName, Category(null, category))
            purchase?.quantity = quantity ?: 0.0
            purchase?.unit = PurchaseUnit(null, unit)
            ServiceCallUtil.treatServiceCall(this, supportFragmentManager) {
                purchaseService.update(purchase!!)
                finish()
            }
        }
    }

    private fun mapPurchase(
        productName: String,
        category: String,
        quantity: Double?,
        unit: String
    ) = Purchase(
        uuid = null,
        product =  Product(
            id = null,
            name = productName,
            category = Category(null, category)
        ),
        quantity = quantity ?: 0.0,
        unit = PurchaseUnit(1, unit),
        cart = false
    )
}