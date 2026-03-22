package com.example.appinterface.Adapter.adapterCategories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Api.Models.Category
import com.example.appinterface.Api.Models.DataResponseCategory
import com.example.appinterface.R

// INTERFAZ
interface OnCategoryClickListener {
    fun onEditClick(category: Category)
    fun onDeleteClick(id: Int)
    fun onItemClick(category: Category)
}

// ADAPTER
class CategoryAdapter(
    private val categories: List<Category>,
    private val listener: OnCategoryClickListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item_list, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position], listener)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtName: TextView = itemView.findViewById(R.id.txtCategory_name)
        private val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

        fun bind(category: Category, listener: OnCategoryClickListener) {
            // Display category name
            txtName.text = category.category_name

            // Edit button
            btnEdit.setOnClickListener {
                listener.onEditClick(category)
            }

            // Delete button
            btnDelete.setOnClickListener {
                listener.onDeleteClick(category.category_id)
            }

            // Item click
            itemView.setOnClickListener {
                listener.onItemClick(category)
            }
        }
    }
}