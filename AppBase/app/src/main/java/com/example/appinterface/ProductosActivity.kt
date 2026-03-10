package com.example.appinterface

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.Api.Models.DataResponse
import com.example.appinterface.Api.Models.DataResponseWarranty
import com.example.appinterface.Api.RetrofitInstance
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductosActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_productos)
        }

    fun crearGarantia(v: View) {
        val serialEditText = findViewById<EditText>(R.id.serial)
        val customerEditText = findViewById<EditText>(R.id.customer)
        val cityEditText = findViewById<EditText>(R.id.city)
        val phoneEditText = findViewById<EditText>(R.id.phone)
        val adressEditText = findViewById<EditText>(R.id.adress)
        val desriptionEditText = findViewById<EditText>(R.id.description)

        val serialStr = serialEditText.text.toString()
        val customerStr = customerEditText.text.toString()
        var editingId: Int? = null
        val WarrantyObject = DataResponseWarranty(
            warranty_incidents_id = editingId, // Usamos el ID si estamos editando
            product_serial = serialStr,
            warranty_customer = customerStr,
            warranty_phone = phoneEditText.text.toString(),
            warranty_address = adressEditText.text.toString(),
            warranty_description = desriptionEditText.text.toString(),
            warranty_link_attachments = "../warranties/images/WINC0003",
            warranty_city = cityEditText.text.toString(),
            warranty_status = "0"
        )

        if (serialStr.isNotEmpty() && customerStr.isNotEmpty()) {
            // Lógica para decidir si es POST o PUT
            val call = if (editingId == null) {
                RetrofitInstance.api2kotlin.createWarranty(WarrantyObject)
            } else {
                RetrofitInstance.api2kotlin.updateWarranty(editingId!!, WarrantyObject)
            }

            call.enqueue(object : Callback<DataResponseWarranty> {
                override fun onResponse(call: Call<DataResponseWarranty>, response: Response<DataResponseWarranty>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ProductosActivity, "Operación exitosa", Toast.LENGTH_LONG).show()
                        editingId = null // Limpiamos el ID después de guardar
                    }
                }
                override fun onFailure(call: Call<DataResponseWarranty>, t: Throwable) {
                    Toast.makeText(this@ProductosActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }
}