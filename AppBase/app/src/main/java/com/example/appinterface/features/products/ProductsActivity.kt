package com.example.appinterface.features.products

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.adapterProducts.ProductAdapter
import com.example.appinterface.Adapter.adapterProducts.OnProductClickListener
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.Api.Models.Product
import com.example.appinterface.MainActivity
import com.example.appinterface.R
import com.example.appinterface.features.categories.AddProductsActivity
import com.example.appinterface.helpers.BottomNavHelper
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsActivity : AppCompatActivity(), OnProductClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product)
        BottomNavHelper.setup(this, R.id.nav_products)

        val btnAgregar = findViewById<ImageButton>(R.id.buttonAgregarProducto)
        btnAgregar.setOnClickListener {
            val intent = Intent(this, AddProductsActivity::class.java)
            startActivity(intent)
        }

        mostrarProductos()
    }

    override fun onItemClick(product: Product) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.product_modal_details, null)

        val txtSerial = view.findViewById<TextView>(R.id.txtProductSerial)
        val txtCategory = view.findViewById<TextView>(R.id.txtCategory)
        val txtModel = view.findViewById<TextView>(R.id.txtProductModel)
        val txtBill = view.findViewById<TextView>(R.id.txtProductBill)


        txtSerial.text = "Serial: ${product.product_serial}"
        txtModel.text = "Modelo: ${product.product_model}"
        txtCategory.text = "Categoría: ${product.category_name}"
        txtBill.text = "Factura: ${product.input_order_bill}"

        dialog.setContentView(view)

        val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.peekHeight = 1200
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        dialog.show()
    }

    override fun onEditClick(product: Product) {
        val intent = Intent(this, AddProductsActivity::class.java).apply {
            putExtra("EDITING_ID", product.product_id)
            putExtra("SERIAL", product.product_serial)
            putExtra("MODEL", product.product_model)
            putExtra("BILL", product.input_order_bill)
        }
        startActivity(intent)
    }

    override fun onDeleteClick(id: Int) {
        eliminarProducto(id)
    }

    fun mostrarProductos() {
        val recyclerView = findViewById<RecyclerView>(R.id.RecyProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)


        RetrofitInstance.apiProducts.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        recyclerView.adapter = ProductAdapter(data, this@ProductsActivity)
                    }
                }
            }
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@ProductsActivity, "Error al cargar productos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun eliminarProducto(idParaEliminar: Int) {
        RetrofitInstance.apiProducts.deleteProduct(idParaEliminar).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ProductsActivity, "Producto eliminado", Toast.LENGTH_SHORT).show()
                    mostrarProductos()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@ProductsActivity, "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
        })
    }
}