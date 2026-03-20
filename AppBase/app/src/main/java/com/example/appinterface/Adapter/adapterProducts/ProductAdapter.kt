package com.example.appinterface.Adapter.adapterProducts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Api.Models.Product
import com.example.appinterface.R

class ProductAdapter(
    private val productList: List<Product>,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.txtProductName)
        val model: TextView = view.findViewById(R.id.txtProductModel)
        val stock: TextView = view.findViewById(R.id.txtProductStock)
        val btnDelete: Button = view.findViewById(R.id.btnDeleteProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]


        holder.name.text = product.product_name ?: "Producto sin nombre"
        holder.model.text = "Modelo: ${product.product_model ?: "No especificado"}"
        holder.stock.text = "Stock actual: ${product.product_stock}"


        holder.btnDelete.setOnClickListener {
            product.product_id?.let { id ->
                onDeleteClick(id)
            }
        }
    }

    override fun getItemCount(): Int = productList.size
}