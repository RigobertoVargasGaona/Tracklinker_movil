package com.example.appinterface.features.products

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.Api.Models.Product
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.RESPONSE.ApiResponse
import com.example.appinterface.R
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddProductsActivity : AppCompatActivity() {

    private var editingId: Int = -1
    private var selectedSubcategoryId: Int = 1

    private lateinit var etBill: TextInputEditText
    private lateinit var etName: TextInputEditText
    private lateinit var etModel: AutoCompleteTextView
    private lateinit var etSerial: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_save_edit)

        etBill = findViewById(R.id.bill)
        etName = findViewById(R.id.product_name)
        etModel = findViewById(R.id.model)
        etSerial = findViewById(R.id.serial)

        val btnHome = findViewById<ImageButton>(R.id.buttonVolver)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarProducto)

        val subIdRecibido = intent.getIntExtra("SELECTED_SUBCATEGORY_ID", -1)
        val subNombreRecibido = intent.getStringExtra("SELECTED_SUBCATEGORY_NAME")


        if (subIdRecibido != -1) {
            selectedSubcategoryId = subIdRecibido
            etName.setText(subNombreRecibido)
            configurarAutoCompletarModelos(subNombreRecibido ?: "")
            etModel.setText("")
        }


        if (intent.hasExtra("EDITING_ID")) {
            editingId = intent.getIntExtra("EDITING_ID", -1)

            if (editingId != -1) {

                val nombreProducto = intent.getStringExtra("NAME") ?: ""
                configurarAutoCompletarModelos(nombreProducto)


                etSerial.setText(intent.getStringExtra("SERIAL"))
                etBill.setText(intent.getStringExtra("BILL"))
                etName.setText(nombreProducto)


                val modeloExistente = intent.getStringExtra("MODEL")
                etModel.setText(modeloExistente, false)

                Toast.makeText(this, "Modo edición", Toast.LENGTH_SHORT).show()
            }
        }

        btnHome.setOnClickListener { finish() }
        btnGuardar.setOnClickListener { guardarProducto() }
    }

    private fun configurarAutoCompletarModelos(subcategoria: String) {
        val listaModelos = when {
            subcategoria.contains("Portatiles", true) -> listOf("Lenovo ThinkPad X1", "Dell Latitude 5420", "HP EliteBook 840", "MacBook Air M2")
            subcategoria.contains("Impresora", true) -> listOf("Epson L3250", "HP LaserJet Pro", "Zebra ZD420", "Canon G3110")
            subcategoria.contains("Monitor", true) || subcategoria.contains("LCD", true) -> listOf("Dell UltraSharp 24", "Samsung Odyssey G5", "LG Gram View")
            subcategoria.contains("RAM", true) -> listOf("Kingston Fury 8GB", "Corsair Vengeance 16GB", "Crucial Basics")
            subcategoria.contains("HDD", true) || subcategoria.contains("SDD", true) -> listOf("Kingston A400", "Samsung 980 Pro", "Crucial P3", "WD Blue 1TB")
            else -> listOf("Generico", "N/A", "Modelo Estandar")
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaModelos)
        etModel.setAdapter(adapter)


        etModel.setOnClickListener { etModel.showDropDown() }
    }

    private fun guardarProducto() {
        val billString = etBill.text.toString().trim()
        val model = etModel.text.toString().trim()
        val serial = etSerial.text.toString().trim()


        if (serial.isEmpty()) {
            etSerial.error = "Ingrese el serial"
            return
        }
        if (model.isEmpty()) {
            etModel.error = "Por favor escriba o elija un modelo"
            return
        }
        if (billString.isEmpty()) {
            etBill.error = "Ingrese la orden de entrada"
            return
        }

        val product = Product(
            product_id = if (editingId == -1) null else editingId,
            product_serial = serial,
            product_model = model, // Aquí enviamos el valor real del campo
            input_order_bill = billString,
            subcategory_id = selectedSubcategoryId,
            product_details_id = 1,
            input_order_id = 1
        )

        val call: Call<ApiResponse> = if (editingId == -1) {
            RetrofitInstance.apiProducts.createProduct(product)
        } else {
            RetrofitInstance.apiProducts.updateProduct(editingId, product)
        }

        ejecutarLlamada(call)
    }

    private fun ejecutarLlamada(call: Call<ApiResponse>) {
        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(this@AddProductsActivity, "Guardado correctamente", Toast.LENGTH_SHORT).show()
                    val resultIntent = Intent().apply { putExtra("PRODUCT_UPDATED", true) }
                    setResult(RESULT_OK, resultIntent)
                    finish()
                } else {
                    val errorMsg = response.body()?.message ?: "Error en el servidor"
                    Toast.makeText(this@AddProductsActivity, errorMsg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@AddProductsActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}