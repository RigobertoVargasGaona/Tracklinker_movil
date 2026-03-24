package com.example.appinterface.Adapter.outputOrders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
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

    override fun onBindViewHolder(holder: OutputOrderViewHolder, position: Int) {
        holder.bind(outputOrders[position], listener)
    }

    override fun getItemCount(): Int = outputOrders.size

    class OutputOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(outputOrder: OutputOrder, listener: OutputOrdersListener) {
            itemView.setOnClickListener {
                val bottomSheet = BottomSheetDialog(itemView.context)
                val modalView = LayoutInflater.from(itemView.context)
                    .inflate(R.layout.output_info_bottom_dialog, null)

                modalView.findViewById<TextView>(R.id.info_modal_output_date).text = " ${outputOrder.out_order_date}"
                modalView.findViewById<TextView>(R.id.info_modal_output_serial).text = " ${outputOrder.product_serial}"
                modalView.findViewById<TextView>(R.id.info_modal_output_model).text = " ${outputOrder.product_detail_model}"
                modalView.findViewById<TextView>(R.id.info_modal_output_garanty).text = " ${outputOrder.out_product_garanty}"
                modalView.findViewById<TextView>(R.id.info_modal_output_transformation).text = " ${outputOrder.product_transformation}"
                modalView.findViewById<TextView>(R.id.info_modal_output_description).text = " ${outputOrder.product_detail_description}"
                modalView.findViewById<TextView>(R.id.info_modal_output_brand).text = " ${outputOrder.product_brand_name}"

                bottomSheet.setContentView(modalView)
                bottomSheet.show()
            }

            itemView.findViewById<ImageButton>(R.id.editOutputButton).setOnClickListener {
                listener.onEditOutputOrder(outputOrder)
            }

            itemView.findViewById<ImageButton>(R.id.deleteOutputButton).setOnClickListener {
                listener.onDelete(outputOrder)
            }
        }
    }
}