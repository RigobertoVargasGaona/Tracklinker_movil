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
import com.example.appinterface.Api.Models.Product
import com.example.appinterface.Api.RetrofitInstance.RetrofitInstance
import com.example.appinterface.R
import com.example.appinterface.RESPONSE.ProductResponse
import com.example.appinterface.features.products.ProductsActivity
import com.example.appinterface.features.warranties.WarrantiesActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddProductsActivity : AppCompatActivity() {

    private var editingId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_save_edit)


        if (intent.hasExtra("EDITING_ID")) {

            editingId = intent.getIntExtra("EDITING_ID", -1)

            findViewById<EditText>(R.id.categoryName)
                .setText(intent.getStringExtra("NAME"))

            findViewById<EditText>(R.id.categoryDescription)
                .setText(intent.getStringExtra("DESCRIPTION"))
        }


        val btnHome = findViewById<ImageButton>(R.id.buttonVolver)
        btnHome.setOnClickListener {
            startActivity(Intent(this, CategoriesActivity::class.java))
        }


        val btnGuardar = findViewById<Button>(R.id.btnGuardarProducto)
        btnGuardar.setOnClickListener { crearProduct(it) }
    }

    private fun crearProduct(v: View) {
        val name = findViewById<EditText>(R.id.product_name).text.toString()
        val description = findViewById<EditText>(R.id.txtProductBill).text.toString()

        if (name.isEmpty()) {
            Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
            return
        }

        val product = Product(

        )

        val call: Call<Product> = if (editingId == null) {
            RetrofitInstance.apiProducts.createProduct(product)
        } else {
            RetrofitInstance.apiProducts.updateProduct(editingId!!, product)
        }

        call.enqueue(object : Callback<Product> {
            override fun onResponse(
                call: Call<Product>,
                response: Response<Product>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddProductsActivity, "Operación exitosa", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@AddProductsActivity, ProductsActivity::class.java)
                } else {
                    Toast.makeText(this@AddProductsActivity, "Error en el servidor", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Toast.makeText(this@AddProductsActivity, "Fallo de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}