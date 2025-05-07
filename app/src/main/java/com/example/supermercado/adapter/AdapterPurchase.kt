package com.example.supermercado.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.supermercado.R
import com.example.supermercado.model.Purchase
import com.example.supermercado.util.ServiceCallUtil

class AdapterPurchase(
    private val context: Context,
    private val manager: FragmentManager,
) : RecyclerView.Adapter<AdapterPurchase.PurchaseViewHolder>() {

    private var purchaseList: MutableList<Purchase> = mutableListOf()

    private var onEditPurchaseListener: ((Purchase) -> Unit) = {}
    private var onButtonCartClickedListener: suspend ((Purchase) -> Unit) = {}
    private var onLoadList: suspend (() -> List<Purchase>) = { emptyList() }

    inner class PurchaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.tv_product_name)
        val quantity: TextView = itemView.findViewById(R.id.tv_quantity)
        val unit: TextView = itemView.findViewById(R.id.tv_unit)
        var category : TextView = itemView.findViewById(R.id.tv_category)

        val btnCart: Button = itemView.findViewById(R.id.btn_cart)
        val btnEdit: Button = itemView.findViewById(R.id.btn_edit_purchase)
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

        if (currentItem.cart) {
            holder.btnCart.text = context.getString(R.string.purchase_cart_delete)
            holder.btnCart.setBackgroundColor(Color.RED)
        } else {
            holder.btnCart.text = context.getString(R.string.purchase_cart_add)
            holder.btnCart.setBackgroundColor(Color.rgb(0, 100,0))
        }

        holder.btnCart.setOnClickListener {
            currentItem.cart = !currentItem.cart
            ServiceCallUtil.treatServiceCall(context, manager) {
                onButtonCartClickedListener.invoke(currentItem)
                purchaseList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, purchaseList.size)
            }
        }

        holder.btnEdit.setOnClickListener {
            onEditPurchaseListener.invoke(currentItem)
        }
    }

    override fun getItemCount(): Int = purchaseList.size

    @SuppressLint("NotifyDataSetChanged")
    fun refresh() {
        ServiceCallUtil.treatServiceCall(context, manager) {
            purchaseList = onLoadList().toMutableList()
            notifyDataSetChanged()
        }
    }

    fun setOnEditPurchaseListener(listener: (Purchase) -> Unit) {
        onEditPurchaseListener = listener
    }

    fun setOnButtonCartClickedListener(listener: suspend (Purchase) -> Unit) {
        onButtonCartClickedListener = listener
    }

    fun setOnLoadListListener(listener: suspend () -> List<Purchase>) {
        onLoadList = listener
    }
}
