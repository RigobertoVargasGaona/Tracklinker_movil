package com.example.appinterface.Adapter.adapterWarranties

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Api.Models.DataResponseWarranty
import com.example.appinterface.R

// 1. Definimos la interfaz aquí mismo o en un archivo aparte
interface OnWarrantyClickListener {
    fun onEditClick(warranty: DataResponseWarranty)
    fun onDeleteClick(id: Int)

    fun onItemClick(warranty: DataResponseWarranty)
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
        // 5. Configurar el click para mostrar los datos en la modal
        holder.itemView.setOnClickListener { item
            listener.onItemClick(warranties[position])
        }

        val garantia = warranties[position]
        val contexto = holder.itemView.context

        holder.txtStatus.text = "Estado: ${garantia.warranty_status}"

        // 1. Obtenemos ambos colores en una sola sentencia 'when'
        val (colorTxt, colorBg) = when (garantia.warranty_status) {
            "2"    -> R.color.status_green_text to R.color.status_green_bg
            "0"    -> R.color.status_red_text to R.color.status_red_bg
            else         -> R.color.status_orange_text to R.color.status_orange_bg
        }

        // 2. Aplicamos ambos colores
        holder.txtStatus.setTextColor(ContextCompat.getColor(contexto, colorTxt))
        holder.txtStatus.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(contexto, colorBg))
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