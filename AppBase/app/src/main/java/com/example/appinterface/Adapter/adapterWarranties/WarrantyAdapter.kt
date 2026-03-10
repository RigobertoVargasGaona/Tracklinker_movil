package com.example.appinterface.Adapter.adapterWarranties

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Api.Models.DataResponseWarranty
import com.example.appinterface.R

// 1. Definimos la interfaz aquí mismo o en un archivo aparte
interface OnWarrantyClickListener {
    fun onEditClick(warranty: DataResponseWarranty)
    fun onDeleteClick(id: Int)
}

// 2. Agregamos el listener al constructor del Adapter
class WarrantyAdapter(
    private val warranties: List<DataResponseWarranty>,
    private val listener: OnWarrantyClickListener
) : RecyclerView.Adapter<WarrantyAdapter.PersonaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_warranty, parent, false)
        return PersonaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonaViewHolder, position: Int) {
        val item = warranties[position]

        // Llenado de datos
        holder.txtCustomer.text = "Cliente: ${item.warranty_customer}"
        holder.txtSerial.text = "Serial: ${item.product_serial}"
        holder.txtStatus.text = "Estado: ${item.warranty_status}"

        // 3. Configurar el clic para EDITAR
        holder.btnEdit.setOnClickListener {
            listener.onEditClick(item)
        }

        // 4. Configurar el clic para ELIMINAR
        holder.btnDelete.setOnClickListener {
            // Verificamos que el ID no sea nulo antes de avisar al listener
            item.warranty_incidents_id?.let { id ->
                listener.onDeleteClick(id)
            }
        }
    }

    override fun getItemCount(): Int = warranties.size

    class PersonaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCustomer: TextView = itemView.findViewById(R.id.txtCustomer)
        val txtSerial: TextView = itemView.findViewById(R.id.txtSerial)
        val txtStatus: TextView = itemView.findViewById(R.id.txtStatus)

        // Referencias a los botones nuevos del XML
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }
}