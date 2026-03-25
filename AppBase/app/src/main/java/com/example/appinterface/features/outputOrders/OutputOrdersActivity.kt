package com.example.appinterface.features.outputOrders

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.adapterProducts.ProductAdapter
import com.example.appinterface.Adapter.outputOrders.OutputOrdersAdapter
import com.example.appinterface.Adapter.outputOrders.OutputOrdersListener
import com.example.appinterface.Adapter.users.UsersAdapter
import com.example.appinterface.Api.Models.CreateOutputOrder
import com.example.appinterface.Api.Models.CreateUser
import com.example.appinterface.Api.Models.DataResponseCategory
import com.example.appinterface.Api.Models.DataResponseOutputs
import com.example.appinterface.Api.Models.OutputOrder
import com.example.appinterface.Api.Models.UpdateOutputOrder
import com.example.appinterface.Api.Models.User
import com.example.appinterface.Api.Models.UsersResponse
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.R
import com.example.appinterface.helpers.BottomNavHelper
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.internal.notifyAll
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OutputOrdersActivity : AppCompatActivity(), OutputOrdersListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        setContentView(R.layout.output_orders_activity)
        BottomNavHelper.setup(this, R.id.nav_outputs)

        loadOutputOrders()

    }

    override fun onShowInfo(outputOrder: OutputOrder) {}
    override fun onEditOutputOrder(outputOrder: OutputOrder) {
        val bottomSheet = BottomSheetDialog(this@OutputOrdersActivity)
        val modalView = layoutInflater.inflate(R.layout.edit_output_bottom_dialog, null)
        bottomSheet.setContentView(modalView)

        bottomSheet.show()

        val updateButton = modalView.findViewById<Button>(R.id.edit_output_button)
        val modelSelect = modalView.findViewById<AutoCompleteTextView>(R.id.model_select)
        val statusOptions = listOf("Deshabilitada", "Habilitada")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, statusOptions)
        modelSelect.setAdapter(adapter)
        modelSelect.setText(statusOptions[outputOrder.out_order_status], false)

        modalView.findViewById<TextView>(R.id.edit_output_serial).setText(outputOrder.product_serial)
        modalView.findViewById<TextView>(R.id.edit_output_garanty).setText(outputOrder.out_product_garanty)
        modalView.findViewById<TextView>(R.id.edit_output_transformation).setText(outputOrder.product_transformation)

        var selectedStatus = outputOrder.out_order_status

        modelSelect.setOnItemClickListener { _,  _, position, _ ->
            selectedStatus = position
        }


        updateButton.setOnClickListener {
            val serial = modalView.findViewById<EditText>(R.id.edit_output_serial).text.toString()
            val transformation = modalView.findViewById<EditText>(R.id.edit_output_transformation).text.toString()
            val garanty = modalView.findViewById<EditText>(R.id.edit_output_garanty).text.toString()

            RetrofitInstance.outputOrderApi.updateOutput(
                outputOrder.output_details_id,
                UpdateOutputOrder(
                    outputOrder.out_order_id,
                    serial,
                    transformation,
                    garanty,
                    selectedStatus
                )).enqueue(object : Callback<DataResponseOutputs> {
                override fun onResponse(call: Call<DataResponseOutputs?>, response: Response<DataResponseOutputs?>) {
                    val success = response.body()?.success
                    if (success != false) {
                        Toast.makeText(this@OutputOrdersActivity, "Orden editada con exito", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@OutputOrdersActivity, "Error en la respuesta de la API", Toast.LENGTH_SHORT).show()
                    }
                    bottomSheet.dismiss()
                    loadOutputOrders()
                }
                override fun onFailure(call: Call<DataResponseOutputs?>, t: Throwable) {
                    Toast.makeText(this@OutputOrdersActivity, "No se pudo actualizar la orden", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onEnable(outputOrder: OutputOrder) {
        val bottomSheet = BottomSheetDialog(this@OutputOrdersActivity)
        val modalView = layoutInflater.inflate(R.layout.delete_output_bottom_dialog, null)
        modalView.findViewById<TextView>(R.id.delete_modal_title).setText("Habilitar Orden")
        bottomSheet.setContentView(modalView)
        bottomSheet.show()

        modalView.findViewById<TextView>(R.id.delete_output_message)
            .setText("¿Deseas Habilitar la orden N°${outputOrder.out_order_id}?")

        val deleteButton = modalView.findViewById<Button>(R.id.delete_output_button)
        deleteButton.setText("Habilitar")

        deleteButton.setOnClickListener {
            RetrofitInstance.outputOrderApi.enableOutput(outputOrder.out_order_id).enqueue(object : Callback<DataResponseOutputs> {
                override fun onResponse(call: Call<DataResponseOutputs>, response: Response<DataResponseOutputs>) {
                    if (response.body()?.success == true) {
                        Toast.makeText(this@OutputOrdersActivity, "Orden habilitada", Toast.LENGTH_SHORT).show()
                        bottomSheet.dismiss()
                        loadOutputOrders()
                    } else {
                        Toast.makeText(this@OutputOrdersActivity, "No se pudo habilitar la orden", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<DataResponseOutputs>, t: Throwable) {
                    Toast.makeText(this@OutputOrdersActivity, "Error al intentar habilitar la orden", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onDisable(outputOrder: OutputOrder) {
        val bottomSheet = BottomSheetDialog(this@OutputOrdersActivity)
        val modalView = layoutInflater.inflate(R.layout.delete_output_bottom_dialog, null)
        modalView.findViewById<TextView>(R.id.delete_modal_title).setText("Deshabilitar Orden")
        bottomSheet.setContentView(modalView)

        bottomSheet.show()

        modalView.findViewById<TextView>(R.id.delete_output_message)
            .setText("¿Deseas Deshabilitar la orden N°${outputOrder.out_order_id}?")

        val deleteButton = modalView.findViewById<Button>(R.id.delete_output_button)
        deleteButton.setBackgroundColor(Color.RED)
        deleteButton.setText("Deshabilitar")

        deleteButton.setOnClickListener {
            RetrofitInstance.outputOrderApi.disableOutput(outputOrder.out_order_id).enqueue(object : Callback<DataResponseOutputs> {
                override fun onResponse(call: Call<DataResponseOutputs>, response: Response<DataResponseOutputs>) {
                    if (response.body()?.success == true) {
                        Toast.makeText(this@OutputOrdersActivity, "Orden deshabilitada", Toast.LENGTH_SHORT).show()
                        bottomSheet.dismiss()
                        loadOutputOrders()
                    } else {
                        Toast.makeText(this@OutputOrdersActivity, "No se pudo deshabilitar la orden", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<DataResponseOutputs>, t: Throwable) {
                    Toast.makeText(this@OutputOrdersActivity, "Error al intentar deshabilitar la orden", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun loadOutputOrders() {
        val recyclerView = findViewById<RecyclerView>(R.id.outputs_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        RetrofitInstance.outputOrderApi.getOutputs().enqueue(object : Callback<DataResponseOutputs> {
            override fun onResponse(call: Call<DataResponseOutputs>, response: Response<DataResponseOutputs>) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        recyclerView.adapter = OutputOrdersAdapter(data.data, this@OutputOrdersActivity)
                    }
                } else {
                    Toast.makeText(this@OutputOrdersActivity, "Error en la respuesta de la API", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DataResponseOutputs>, t: Throwable) {
                Toast.makeText(this@OutputOrdersActivity, "Error en la conexión con la API", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun createOutput(v: View) {
        val bottomSheet = BottomSheetDialog(this)
        val modalView = layoutInflater.inflate(R.layout.add_output_bottom_dialog, null)
        bottomSheet.setContentView(modalView)
        bottomSheet.show()

        val createButton = modalView.findViewById<Button>(R.id.create_output_button)

        createButton.setOnClickListener {
            val serial = modalView.findViewById<EditText>(R.id.add_output_serial).text.toString()

            val transfomation = modalView.findViewById<EditText>(R.id.add_output_transformation).text.toString()
            val garanty = modalView.findViewById<EditText>(R.id.add_output_garanty).text.toString()

            RetrofitInstance.outputOrderApi.createOutput(CreateOutputOrder(
                serial,
                transfomation,
                garanty
            )).enqueue(object : Callback<DataResponseOutputs> {
                override fun onResponse(call: Call<DataResponseOutputs>, response: Response<DataResponseOutputs>) {
                    val responseBody = response.body()
                    if (responseBody?.success == true) {
                        Toast.makeText(this@OutputOrdersActivity, "Orden creada con exito", Toast.LENGTH_SHORT).show()
                        bottomSheet.dismiss()
                        loadOutputOrders()
                       } else {
                        Toast.makeText(this@OutputOrdersActivity, "No se pudo crear la orden", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DataResponseOutputs>, t: Throwable) {
                    Toast.makeText(this@OutputOrdersActivity, "Error en la conexión con la API", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
