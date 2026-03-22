package com.example.appinterface.features.outputOrders

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.outputOrders.OutputOrdersAdapter
import com.example.appinterface.Adapter.outputOrders.OutputOrdersListener
import com.example.appinterface.Adapter.users.UsersAdapter
import com.example.appinterface.Api.Models.CreateOutputOrder
import com.example.appinterface.Api.Models.CreateUser
import com.example.appinterface.Api.Models.DataResponseCategory
import com.example.appinterface.Api.Models.OutputOrder
import com.example.appinterface.Api.Models.User
import com.example.appinterface.Api.Models.UsersResponse
import com.example.appinterface.Api.RetrofitInstance.RetrofitInstance
import com.example.appinterface.R
import com.example.appinterface.helpers.BottomNavHelper
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OutputOrdersActivity : AppCompatActivity() {
    /*
    private var outputsList: MutableList<OutputOrder> = mutableListOf()
    private lateinit var recycler: RecyclerView

    private lateinit var  outputAdapter: OutputOrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        setContentView(R.layout.output_orders_activity)
        BottomNavHelper.setup(this, R.id.nav_outputs)

        loadOutputOrders()

        recycler = findViewById(R.id.outputs_recycler_view)
        recycler.layoutManager = LinearLayoutManager(this)

        outputAdapter = OutputOrdersAdapter(outputsList, object: OutputOrdersListener {
            override fun onShowInfo(outputOrder: OutputOrder) {}
            override fun onDelete(outputOrder: OutputOrder) {
                val bottomSheet = BottomSheetDialog(this@OutputOrdersActivity)
                val modalView = layoutInflater.inflate(R.layout.delete_user_bottom_dialog, null)
                bottomSheet.setContentView(modalView)

                bottomSheet.show()

                modalView.findViewById<TextView>(R.id.delete_user_message_name).setText(" ${outputOrder.product_serial}?")

                val deleteButton = modalView.findViewById<Button>(R.id.delete_user_button)

                deleteButton.setOnClickListener {
                    RetrofitInstance.usersApi.deleteUser(outputOrder.out_order_id).enqueue(object : Callback<UsersResponse> {
                        override fun onResponse(call: Call<UsersResponse>, response: Response<UsersResponse>) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@OutputOrdersActivity, "Orden eliminada", Toast.LENGTH_SHORT).show()
                                bottomSheet.dismiss()
                                loadOutputOrders()
                            }
                        }
                        override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                            Toast.makeText(this@OutputOrdersActivity, "Error al intentar eliminar la orden", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
            override fun onEditOutputOrder(outputOrder: OutputOrder) {
                val bottomSheet = BottomSheetDialog(this@OutputOrdersActivity)
                val modalView = layoutInflater.inflate(R.layout.edit_output_bottom_dialog, null)
                bottomSheet.setContentView(modalView)

                bottomSheet.show()

                val autoComplete = modalView.findViewById<AutoCompleteTextView>(R.id.model_select)
                val options = listOf("Admin", "Almacen", "Tecnico")
                val adapter = ArrayAdapter(this@OutputOrdersActivity, android.R.layout.simple_dropdown_item_1line, options)
                autoComplete.setAdapter(adapter)
                autoComplete.setText(outputOrder.product_detail_model, false)
                autoComplete.setOnClickListener { autoComplete.showDropDown() }

                modalView.findViewById<AutoCompleteTextView>(R.id.model_select).setText(outputOrder.product_detail_model)
                modalView.findViewById<EditText>(R.id.edit_user_name).setText(user.user_name)
                modalView.findViewById<EditText>(R.id.edit_user_first_surname).setText(user.user_first_surname)
                modalView.findViewById<EditText>(R.id.edit_user_second_surname).setText(user.user_second_surname)
                modalView.findViewById<EditText>(R.id.edit_user_email).setText(user.user_email)
                modalView.findViewById<EditText>(R.id.edit_user_phone).setText(user.user_phone)
                modalView.findViewById<EditText>(R.id.edit_user_city).setText(user.user_city)
                modalView.findViewById<EditText>(R.id.edit_user_address).setText(user.user_address)

                val editButton = modalView.findViewById<Button>(R.id.edit_user_button)
                editButton.setOnClickListener {
                    val modelText = autoComplete.text.toString()
                    val product_details_id = when(modelText) {
                        "a" -> 1
                        "a" -> 2
                        "a" -> 3
                        else -> 0
                    }
                    val serial = modalView.findViewById<EditText>(R.id.edit_output_serial).text.toString()
                    val brand = modalView.findViewById<EditText>(R.id.edit_output_brand).text.toString()
                    val transfomation = modalView.findViewById<EditText>(R.id.edit_output_transformation).text.toString()
                    val garanty = modalView.findViewById<EditText>(R.id.edit_output_garanty).text.toString()

                    RetrofitInstance.usersApi.updateUser(
                        CreateOutputOrder(
                            serial,
                            brand,
                            transfomation,
                            garanty,
                            product_details_id,
                            "1"
                        ),
                        outputOrder.out_order_id,
                    ).enqueue(object : Callback<UsersResponse> {
                        override fun onResponse(call: Call<UsersResponse>, response: Response<UsersResponse>) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@OutputOrdersActivity, "Orden actualizada", Toast.LENGTH_SHORT).show()
                                bottomSheet.dismiss()
                                loadOutputOrders()
                            }
                        }
                        override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                            Toast.makeText(this@OutputOrdersActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        })
    }
    fun loadOutputOrders() {
        val recyclerView = findViewById<RecyclerView>(R.id.outputs_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        RetrofitInstance.outputOrderApi.getOutputs().enqueue(object : Callback<List<OutputOrder>> {
            override fun onResponse(call: Call<List<OutputOrder>>, response: Response<List<OutputOrder>>) {
                if (response.isSuccessful) {
                    outputsList.clear()
                    outputsList.addAll(response.body() ?: emptyList())
                    outputsList
                } else {
                    Toast.makeText(this@OutputOrdersActivity, "Error en la respuesta de la API", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<OutputOrder>>, t: Throwable) {
                Toast.makeText(this@OutputOrdersActivity, "Error en la conexión con la API", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun createOutput(v: View) {
        val bottomSheet = BottomSheetDialog(this)
        val modalView = layoutInflater.inflate(R.layout.add_output_bottom_dialog, null)
        bottomSheet.setContentView(modalView)

        val options = listOf("Seleccionar", "Admin", "Almacen", "Tecnico")
        val modelAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, options)

        val modelAutoComplete = modalView.findViewById<AutoCompleteTextView>(R.id.add_model_Select)
        modelAutoComplete.setAdapter(modelAdapter)

        modelAutoComplete.setOnClickListener {
            modelAutoComplete.showDropDown()
        }

        val createButton = modalView.findViewById<Button>(R.id.create_output_button)

        createButton.setOnClickListener {
            val modelText = modelAutoComplete.text.toString()
            val brand =
            val product_details__id = when(modelText) {
                "Admin" -> 1
                "Almacen" -> 2
                "Tecnico" -> 3
                else -> 0
            }

            val serial = modalView.findViewById<EditText>(R.id.add_out_serial).text.toString()
            val brand = modalView.findViewById<EditText>(R.id.edit_output_brand).text.toString()
            val transfomation = modalView.findViewById<EditText>(R.id.edit_output_transformation).text.toString()
            val garanty = modalView.findViewById<EditText>(R.id.edit_output_garanty).text.toString()

            RetrofitInstance.outputOrderApi.createOutput(CreateOutputOrder(
                product_details__id,
                name,
                first_surname,
                second_surname,
                phone,
                "123",
                email,
                address,
                city
            )).enqueue(object : Callback<UsersResponse> {
                override fun onResponse(call: Call<UsersResponse>, response: Response<UsersResponse>) {
                    val responseBody = response.body()
                    if (responseBody?.success == true) {
                        Toast.makeText(this@OutputOrdersActivity, "Orden creada con exito", Toast.LENGTH_SHORT).show()
                        bottomSheet.dismiss()
                        loadOutputOrders()
                    } else {
                        Toast.makeText(this@OutputOrdersActivity, "No se pudo crear la orden", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                    Toast.makeText(this@OutputOrdersActivity, "Error en la conexión con la API", Toast.LENGTH_SHORT).show()
                }
            })
        }

        bottomSheet.show()
    }
     */
}
