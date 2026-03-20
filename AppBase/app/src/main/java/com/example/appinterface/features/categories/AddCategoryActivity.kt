package com.example.appinterface.features.categories

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.Api.CategoryRetrofitInstance
import com.example.appinterface.Api.Models.DataResponseCategory
import com.example.appinterface.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCategoryActivity : AppCompatActivity() {

    private var editingId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_save_edit)

        // Verificamos si viene para editar
        if (intent.hasExtra("EDITING_ID")) {

            editingId = intent.getIntExtra("EDITING_ID", -1)

            findViewById<EditText>(R.id.categoryName)
                .setText(intent.getStringExtra("NAME"))

            findViewById<EditText>(R.id.categoryDescription)
                .setText(intent.getStringExtra("DESCRIPTION"))
        }

        // Botón para volver a la lista de categorías
        val btnHome = findViewById<ImageButton>(R.id.buttonVolver)
        btnHome.setOnClickListener {
            startActivity(Intent(this, CategoriesActivity::class.java))
        }

        // Botón para guardar
        val btnGuardar = findViewById<Button>(R.id.btnGuardarCategoria)
        btnGuardar.setOnClickListener { crearCategoria(it) }
    }

    private fun crearCategoria(v: View) {
        val name = findViewById<EditText>(R.id.categoryName).text.toString()
        val description = findViewById<EditText>(R.id.categoryDescription).text.toString()

        if (name.isEmpty()) {
            Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
            return
        }

        val category = DataResponseCategory(
            category_name = name,
            category_description = description
        )

        val call: Call<DataResponseCategory> = if (editingId == null) {
            CategoryRetrofitInstance.api2kotlin.createCategory(category)
        } else {
            CategoryRetrofitInstance.api2kotlin.updateCategory(editingId!!, category)
        }

        call.enqueue(object : Callback<DataResponseCategory> {
            override fun onResponse(
                call: Call<DataResponseCategory>,
                response: Response<DataResponseCategory>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddCategoryActivity, "Operación exitosa", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@AddCategoryActivity, CategoriesActivity::class.java))
                } else {
                    Toast.makeText(this@AddCategoryActivity, "Error en el servidor", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DataResponseCategory>, t: Throwable) {
                Toast.makeText(this@AddCategoryActivity, "Fallo de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}