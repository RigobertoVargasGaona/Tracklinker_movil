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

// Definimos la interfaz aquí mismo o en un archivo aparte
interface OnWarrantyClickListener {
    fun onEditClick(warranty: DataResponseWarranty)
    fun onDeleteClick(warranty: DataResponseWarranty)
    fun onItemClick(warranty: DataResponseWarranty)
}

//  Agregamos el listener al constructor del Adapter
class WarrantyAdapter(
    private val warranties: List<DataResponseWarranty>,
    private val listener: OnWarrantyClickListener) : RecyclerView.Adapter<WarrantyAdapter.PersonaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.warranty_item_list, parent, false)

        return PersonaViewHolder(view)

    }

    private fun idToStatus(id: String?): String {
        return when (id) {
            "0" -> "Recibida"
            "1" -> "Pendiente"
            "2" -> "Finalizada"
            else -> "Desconocido"
        }
    }

    override fun onBindViewHolder(holder: PersonaViewHolder, position: Int) {
        val item = warranties[position]
        val contexto = holder.itemView.context

        //  Llenado de datos básicos
        holder.txtCustomer.text = "Cliente: ${item.warranty_customer}"
        holder.txtSerial.text = "Serial: ${item.product_serial}"


        // Guardamos el texto convertido en una variable para usarlo abajo
        val statusName = idToStatus(item.warranty_status)
        holder.txtStatus.text = "Estado: $statusName"


        // Usamos el ID original (item.warranty_status) para decidir los colores
        val (colorTxt, colorBg) = when (item.warranty_status) {
            "2" -> R.color.status_green_text to R.color.status_green_bg
            "0" -> R.color.status_red_text to R.color.status_red_bg
            else -> R.color.status_orange_text to R.color.status_orange_bg
        }

        // Aplicamos los colores al TextView del estado
        holder.txtStatus.setTextColor(ContextCompat.getColor(contexto, colorTxt))
        holder.txtStatus.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(contexto, colorBg))

        holder.btnEdit.setOnClickListener {
            listener.onEditClick(item)
        }

        holder.btnDelete.setOnClickListener {
            item.warranty_incidents_id?.let { id ->
                listener.onDeleteClick(item)
            }
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(item)
        }
    }

    override fun getItemCount(): Int = warranties.size

    class PersonaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCustomer: TextView = itemView.findViewById(R.id.txtCustomer)
        val txtSerial: TextView = itemView.findViewById(R.id.txtSerial)
        val txtStatus: TextView = itemView.findViewById(R.id.txtStatus)


        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

    }
}