package com.example.appinterface.Adapter.adapterCategories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Api.Models.DataResponseCategory
import com.example.appinterface.R

// INTERFAZ
interface OnCategoryClickListener {
    fun onEditClick(category: DataResponseCategory)
    fun onDeleteClick(id: Int)
    fun onItemClick(category: DataResponseCategory)
}

// ADAPTER
class CategoryAdapter(
    private val categories: List<DataResponseCategory>,
    private val listener: OnCategoryClickListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item_list, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = categories[position]

        // Mostrar solo el nombre de la categoría
        holder.txtName.text = "Categoría: ${item.category_name}"

        // BOTÓN EDITAR
        holder.btnEdit.setOnClickListener {
            listener.onEditClick(item)
        }

        // BOTÓN ELIMINAR
        holder.btnDelete.setOnClickListener {
            item.category_id?.let { id ->
                listener.onDeleteClick(id)
            }
        }

        // CLICK EN ITEM
        holder.itemView.setOnClickListener {
            listener.onItemClick(item)
        }
    }

    override fun getItemCount(): Int = categories.size

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtCategoryName)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }
}