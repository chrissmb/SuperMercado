package com.example.supermercado.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.supermercado.R
import com.example.supermercado.model.Purchase

class AdapterPurchase : RecyclerView.Adapter<AdapterPurchase.PurchaseViewHolder>() {

    private var purchaseList = emptyList<Purchase>()

    private var onEditPurchaseListener: ((Purchase) -> Unit) = {}
    private var onDeletePurchaseListener: ((Purchase) -> Unit) = {}

    inner class PurchaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.tv_product_name)
        val quantity: TextView = itemView.findViewById(R.id.tv_quantity)
        val unit: TextView = itemView.findViewById(R.id.tv_unit)
        val btnEdit: TextView = itemView.findViewById(R.id.btn_edit_purchase)
        val btnDelete: TextView = itemView.findViewById(R.id.btn_delete_purchase)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.purchase_list_item, parent, false)
        return PurchaseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PurchaseViewHolder, position: Int) {
        val currentItem = purchaseList[position]
        holder.productName.text = currentItem.product.name
        holder.quantity.text = currentItem.quantity.toString()
        holder.unit.text = currentItem.unit?.name

        holder.btnEdit.setOnClickListener {
            onEditPurchaseListener.invoke(currentItem)
        }

        holder.btnDelete.setOnClickListener {
            onDeletePurchaseListener.invoke(currentItem)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int = purchaseList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setProducts(purchases: List<Purchase>) {
        this.purchaseList = purchases
        notifyDataSetChanged()
    }

    fun setOnEditPurchaseListener(listener: (Purchase) -> Unit) {
        onEditPurchaseListener = listener
    }

    fun setOnDeletePurchaseListener(listener: (Purchase) -> Unit) {
        onDeletePurchaseListener = listener
    }
}
