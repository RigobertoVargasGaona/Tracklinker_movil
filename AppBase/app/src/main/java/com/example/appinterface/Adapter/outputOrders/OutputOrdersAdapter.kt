package com.example.appinterface.Adapter.outputOrders

import android.content.res.ColorStateList
import android.support.annotation.DrawableRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Api.Models.OutputOrder
import com.example.appinterface.R
import com.google.android.material.bottomsheet.BottomSheetDialog


class OutputOrdersAdapter(
    private val outputOrders: List<OutputOrder>,
    private val listener: OutputOrdersListener
) : RecyclerView.Adapter<OutputOrdersAdapter.OutputOrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutputOrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.output_order_field, parent, false)
        return OutputOrderViewHolder(view)
    }

        private fun idToStatus(id: Int?): String {
        return when (id) {
            1 -> "Habilitada"
            else -> "Deshabilitada"
        }
    }

    override fun onBindViewHolder(holder: OutputOrderViewHolder, position: Int) {
        val item = outputOrders[position]
        val context = holder.itemView.context

        val statusText = idToStatus(item.out_order_status)
        holder.serial.text = item.product_serial
        holder.id.text = item.out_order_id.toString()
        holder.status.text = statusText

        val (colorTxt, colorBg) = when (statusText) {
            "Habilitada" -> R.color.status_green_text to R.color.status_green_bg
            else -> R.color.status_red_text to R.color.status_red_bg
        }

        holder.status.setTextColor(ContextCompat.getColor(context, colorTxt))
        holder.status.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, colorBg))

        holder.itemView.setOnClickListener {
            val bottomSheet = BottomSheetDialog(holder.itemView.context)
            val modalView = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.output_info_bottom_dialog, null)

            modalView.findViewById<TextView>(R.id.info_modal_output_date).text = " ${item.out_order_date}"
            modalView.findViewById<TextView>(R.id.info_modal_output_serial).text = " ${item.product_serial}"
            modalView.findViewById<TextView>(R.id.info_modal_output_model).text = " ${item.product_detail_model}"
            modalView.findViewById<TextView>(R.id.info_modal_output_garanty).text = " ${item.out_product_garanty}"
            modalView.findViewById<TextView>(R.id.info_modal_output_transformation).text = " ${item.product_transformation}"
            modalView.findViewById<TextView>(R.id.info_modal_output_description).text = " ${item.product_detail_description}"
            modalView.findViewById<TextView>(R.id.info_modal_output_brand).text = " ${item.product_brand_name}"
            modalView.findViewById<TextView>(R.id.info_modal_output_status).text = idToStatus(item.out_order_status)

            bottomSheet.setContentView(modalView)
            bottomSheet.show()
        }

        holder.itemView.findViewById<ImageButton>(R.id.editOutputButton).setOnClickListener {
            listener.onEditOutputOrder(item)
        }

        val deleteIcon = holder.itemView.findViewById<ImageView>(R.id.deleteOutputButton)

        if (item.out_order_status == 1) {
            deleteIcon.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_visibility))
            holder.itemView.findViewById<ImageButton>(R.id.deleteOutputButton).setOnClickListener {
                listener.onDisable(item)
            }
        } else {
            deleteIcon.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_visibility_lock))
            holder.itemView.findViewById<ImageButton>(R.id.deleteOutputButton).setOnClickListener {
                listener.onEnable(item)
            }
        }

    }

    override fun getItemCount(): Int = outputOrders.size

    class OutputOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serial = itemView.findViewById<TextView>(R.id.serialTv)
        val id = itemView.findViewById<TextView>(R.id.idTv)
        val status = itemView.findViewById<TextView>(R.id.statusTv)
    }
}