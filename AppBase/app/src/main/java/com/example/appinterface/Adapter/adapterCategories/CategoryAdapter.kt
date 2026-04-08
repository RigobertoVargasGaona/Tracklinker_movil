package com.example.appinterface.Adapter.adapterCategories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Api.Models.Category
import com.example.appinterface.R

// INTERFAZ
interface OnCategoryClickListener {
    fun onEditClick(category: Category)
    fun onDeleteClick(id: Int)
    fun onItemClick(category: Category)
}

// ADAPTER
class CategoryAdapter(
    private var categories: MutableList<Category>,
    private val listener: OnCategoryClickListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    // 🔥 LISTA ORIGINAL PARA FILTRO
    private var originalList: MutableList<Category> = ArrayList(categories)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item_list, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position], listener)
    }

    override fun getItemCount(): Int = categories.size

    // 🔍 FILTRO
    fun filter(query: String) {
        val filtered = if (query.isEmpty()) {
            originalList
        } else {
            originalList.filter {
                it.category_name.lowercase().contains(query.lowercase())
            }
        }

        categories.clear()
        categories.addAll(filtered)
        notifyDataSetChanged()
    }

    // 🔄 ACTUALIZAR DATA DESDE API
    fun updateData(newData: List<Category>) {
        originalList.clear()
        originalList.addAll(newData)

        categories.clear()
        categories.addAll(newData)

        notifyDataSetChanged()
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtName: TextView = itemView.findViewById(R.id.txtCategory_name)
        private val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

        fun bind(category: Category, listener: OnCategoryClickListener) {
            txtName.text = category.category_name

            btnEdit.setOnClickListener {
                listener.onEditClick(category)
            }

            btnDelete.setOnClickListener {
                listener.onDeleteClick(category.category_id)
            }

            itemView.setOnClickListener {
                listener.onItemClick(category)
            }
        }
    }
}