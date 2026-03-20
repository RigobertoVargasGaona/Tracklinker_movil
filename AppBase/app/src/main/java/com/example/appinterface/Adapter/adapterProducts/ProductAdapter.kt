package com.example.appinterface.Adapter.adapterProducts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Api.Models.Product
import com.example.appinterface.R

interface OnProductClickListener {
    fun onItemClick(product: Product)
    fun onEditClick(product: Product)
    fun onDeleteClick(id: Int)
}

class ProductAdapter(
    private val productList: List<Product>,
    private val itemClickListener: OnProductClickListener
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product, itemClickListener)
    }

    override fun getItemCount(): Int = productList.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtName = itemView.findViewById<TextView>(R.id.txtProductName)
        private val txtSerial = itemView.findViewById<TextView>(R.id.txtProductSerial)
        private val btnEdit = itemView.findViewById<ImageButton>(R.id.btnEdit)
        private val btnDelete = itemView.findViewById<ImageButton>(R.id.btnDelete)

        fun bind(product: Product, clickListener: OnProductClickListener) {
            txtName.text = product.product_model
            txtSerial.text = "S/N: ${product.product_serial}"

            itemView.setOnClickListener { clickListener.onItemClick(product) }

            btnEdit.setOnClickListener { clickListener.onEditClick(product) }

            btnDelete.setOnClickListener {
                product.product_id?.let { id -> clickListener.onDeleteClick(id) }
            }
        }
    }
}