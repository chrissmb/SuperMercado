package com.example.supermercado.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.supermercado.R
import com.example.supermercado.model.Product
import com.example.supermercado.model.Purchase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdapterPurchase : RecyclerView.Adapter<AdapterPurchase.PurchaseViewHolder>() {

    private var purchaseList = emptyList<Purchase>()

    private var onEditPurchaseListener: ((Purchase) -> Unit) = {}
    private var onCheckedChangeListener: ((Purchase) -> Unit) = {}
    private var onLoadList: suspend (() -> List<Purchase>) = { emptyList() }
//    private var onLoadItem: (() -> Purchase) = {}

    inner class PurchaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.tv_product_name)
        val quantity: TextView = itemView.findViewById(R.id.tv_quantity)
        val unit: TextView = itemView.findViewById(R.id.tv_unit)
        var category : TextView = itemView.findViewById(R.id.tv_category)

        val cart: CheckBox = itemView.findViewById(R.id.cb_cart)
        val btnEdit: TextView = itemView.findViewById(R.id.btn_edit_purchase)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.purchase_list_item, parent, false)
        return PurchaseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PurchaseViewHolder, position: Int) {
        val currentItem = purchaseList[position]
        holder.productName.text = currentItem.product.name
        holder.quantity.text = currentItem.quantity?.toString()
        holder.unit.text = currentItem.unit?.name
        holder.category.text = currentItem.product.category?.name

        holder.cart.setOnCheckedChangeListener(null)
        holder.cart.isChecked = currentItem.cart
        holder.cart.setOnCheckedChangeListener { _, isChecked ->
            currentItem.cart = isChecked
            onCheckedChangeListener.invoke(currentItem)
            notifyItemChanged(position)
        }

        holder.btnEdit.setOnClickListener {
            onEditPurchaseListener.invoke(currentItem)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = purchaseList.size


    @SuppressLint("NotifyDataSetChanged")
    fun refresh() {
        purchaseList = listOf(loadingDummyPurchase())
        notifyDataSetChanged()
        CoroutineScope(Dispatchers.Main).launch {
            purchaseList = onLoadList.invoke()
            notifyDataSetChanged()
        }
    }

    fun setOnEditPurchaseListener(listener: (Purchase) -> Unit) {
        onEditPurchaseListener = listener
    }

    fun setOnCheckedChangeListener(listener: (Purchase) -> Unit) {
        onCheckedChangeListener = listener
    }

    fun setOnLoadListListener(listener: suspend () -> List<Purchase>) {
        onLoadList = listener
    }

    private fun loadingDummyPurchase(): Purchase {
        return Purchase(
            null,
            Product(null, "Carregando...", null),
            null,
            null,
            false)
    }
}
