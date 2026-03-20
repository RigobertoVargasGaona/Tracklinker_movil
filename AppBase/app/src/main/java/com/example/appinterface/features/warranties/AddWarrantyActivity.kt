package com.example.appinterface.features.warranties

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.Api.Models.DataResponseWarranty
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddWarrantyActivity : AppCompatActivity() {

    private var editingId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.warranty_save_edit)

        val dropdown = findViewById<AutoCompleteTextView>(R.id.warrantyStatus)


        if (intent.hasExtra("EDITING_ID")) {

            editingId = intent.getIntExtra("EDITING_ID", -1)

            // Cargamos todas las opciones para poder cambiar el estado
            val items = listOf("Recibida", "Pendiente", "Finalizada")
            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)
            dropdown.setAdapter(adapter)

            // Llenar campos con datos recibidos
            findViewById<EditText>(R.id.serial).setText(intent.getStringExtra("SERIAL"))
            findViewById<EditText>(R.id.customer).setText(intent.getStringExtra("CUSTOMER"))
            findViewById<EditText>(R.id.city).setText(intent.getStringExtra("CITY"))
            findViewById<EditText>(R.id.phone).setText(intent.getStringExtra("PHONE"))
            findViewById<EditText>(R.id.adress).setText(intent.getStringExtra("ADDRESS"))
            findViewById<EditText>(R.id.description).setText(intent.getStringExtra("DESCRIPTION"))

            val statusIdFromIntent = intent.getStringExtra("STATUS")
            val statusText = idToStatus(statusIdFromIntent)
            dropdown.setText(statusText, false)

        } else {

            val items = listOf("Recibida")
            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)
            dropdown.setAdapter(adapter)

            dropdown.setText("Recibida", false)
        }

        val btnHome = findViewById<ImageButton>(R.id.buttonVolver)
        btnHome.setOnClickListener {
            val intent = Intent(this, WarrantiesActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun statusToId(statusName: String): String {
        return when (statusName) {
            "Recibida" -> "0"
            "Pendiente" -> "1"
            "Finalizada" -> "2"
            else -> "0"
        }
    }

    private fun idToStatus(id: String?): String {
        return when (id) {
            "0" -> "Recibida"
            "1" -> "Pendiente"
            "2" -> "Finalizada"
            else -> "Recibida"
        }
    }

    fun createWarranty(v: View) {
        val serialStr = findViewById<EditText>(R.id.serial).text.toString()
        val customerStr = findViewById<EditText>(R.id.customer).text.toString()
        val phoneStr = findViewById<EditText>(R.id.phone).text.toString()
        val addressStr = findViewById<EditText>(R.id.adress).text.toString()
        val cityStr = findViewById<EditText>(R.id.city).text.toString()
        val descStr = findViewById<EditText>(R.id.description).text.toString()

        // 3. Capturamos el texto del dropdown y lo convertimos a ID numérico para la DB
        val selectedStatusText = findViewById<AutoCompleteTextView>(R.id.warrantyStatus).text.toString()
        val statusIdForDb = statusToId(selectedStatusText)

        if (serialStr.isEmpty() || customerStr.isEmpty()) {
            Toast.makeText(this, "Serial y Cliente son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val warrantyObject = DataResponseWarranty(
            warranty_incidents_id = if (editingId == -1) null else editingId,
            product_serial = serialStr,
            warranty_customer = customerStr,
            warranty_phone = phoneStr,
            warranty_address = addressStr,
            warranty_description = descStr,
            warranty_link_attachments = "../warranties/images/WINC0003",
            warranty_city = cityStr,
            warranty_status = statusIdForDb // <--- Aquí enviamos "0", "1" o "2"
        )

        val call = if (editingId == null || editingId == -1) {
            RetrofitInstance.api2kotlin.createWarranty(warrantyObject)
        } else {
            RetrofitInstance.api2kotlin.updateWarranty(editingId!!, warrantyObject)
        }

        call.enqueue(object : Callback<DataResponseWarranty> {
            override fun onResponse(call: Call<DataResponseWarranty>, response: Response<DataResponseWarranty>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddWarrantyActivity, "Operación exitosa", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@AddWarrantyActivity, WarrantiesActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@AddWarrantyActivity, "Error en el servidor", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DataResponseWarranty>, t: Throwable) {
                Toast.makeText(this@AddWarrantyActivity, "Fallo de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun clearInputs() {
        findViewById<EditText>(R.id.serial).setText("")
        findViewById<EditText>(R.id.customer).setText("")
        findViewById<EditText>(R.id.city).setText("")
        findViewById<EditText>(R.id.phone).setText("")
        findViewById<EditText>(R.id.adress).setText("")
        findViewById<EditText>(R.id.description).setText("")
        findViewById<AutoCompleteTextView>(R.id.warrantyStatus).setText("", false)
        findViewById<EditText>(R.id.serial).requestFocus()
    }
}