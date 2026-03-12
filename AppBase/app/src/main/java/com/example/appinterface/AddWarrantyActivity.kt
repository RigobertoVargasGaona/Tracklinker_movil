package com.example.appinterface

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.Api.Models.DataResponseWarranty
import com.example.appinterface.Api.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddWarrantyActivity : AppCompatActivity() {

    private var editingId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_warranties)

        // Verificamos si viene de la MainActivity para editar
        if (intent.hasExtra("EDITING_ID")) {
            editingId = intent.getIntExtra("EDITING_ID", -1)

            // Llenar los campos con los datos recibidos
            findViewById<EditText>(R.id.serial).setText(intent.getStringExtra("SERIAL"))
            findViewById<EditText>(R.id.customer).setText(intent.getStringExtra("CUSTOMER"))
            findViewById<EditText>(R.id.city).setText(intent.getStringExtra("CITY"))
            findViewById<EditText>(R.id.phone).setText(intent.getStringExtra("PHONE"))
            findViewById<EditText>(R.id.adress).setText(intent.getStringExtra("ADDRESS"))
            findViewById<EditText>(R.id.description).setText(intent.getStringExtra("DESCRIPTION"))
        }
        val btnHome = findViewById<ImageButton>(R.id.buttonVolver)
        btnHome.setOnClickListener {
        val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun crearGarantia(v: View) {
        val serialStr = findViewById<EditText>(R.id.serial).text.toString()
        val customerStr = findViewById<EditText>(R.id.customer).text.toString()
        val phoneStr = findViewById<EditText>(R.id.phone).text.toString()
        val addressStr = findViewById<EditText>(R.id.adress).text.toString()
        val cityStr = findViewById<EditText>(R.id.city).text.toString()
        val descStr = findViewById<EditText>(R.id.description).text.toString()

        if (serialStr.isEmpty() || customerStr.isEmpty()) {
            Toast.makeText(this, "Serial y Cliente son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val warrantyObject = DataResponseWarranty(
            warranty_incidents_id = editingId, // Será null si es nuevo, o el ID si es edición
            product_serial = serialStr,
            warranty_customer = customerStr,
            warranty_phone = phoneStr,
            warranty_address = addressStr,
            warranty_description = descStr,
            warranty_link_attachments = "../warranties/images/WINC0003",
            warranty_city = cityStr,
            warranty_status = "0"
        )

        val call = if (editingId == null) {
            RetrofitInstance.api2kotlin.createWarranty(warrantyObject)
        } else {
            RetrofitInstance.api2kotlin.updateWarranty(editingId!!, warrantyObject)
        }

        call.enqueue(object : Callback<DataResponseWarranty> {
            override fun onResponse(call: Call<DataResponseWarranty>, response: Response<DataResponseWarranty>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddWarrantyActivity, "Operación exitosa", Toast.LENGTH_LONG).show()


                } else {
                    Toast.makeText(this@AddWarrantyActivity, "Error en el servidor", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DataResponseWarranty>, t: Throwable) {
                Toast.makeText(this@AddWarrantyActivity, "Fallo de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}