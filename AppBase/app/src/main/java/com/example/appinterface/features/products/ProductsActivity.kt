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
import com.example.appinterface.Api.Models.Product
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.RESPONSE.ApiResponse
import com.example.appinterface.R
import com.example.appinterface.helpers.BottomNavHelper
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsActivity : AppCompatActivity(), OnProductClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mainView: View

    private val subcategoryMap = mapOf(
        "Portatiles" to 1,
        "All in one" to 2,
        "Torres" to 3,
        "LCD" to 4,
        "LED" to 5,
        "OLED" to 6,
        "IPS" to 10,
        "Impresora a color" to 12,
        "Impresora a laser" to 13,
        "HDD" to 37,
        "SDD" to 38,
        "Memorias RAM DDR4" to 40,
        "Memorias RAM DDR5" to 41
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product)
        BottomNavHelper.setup(this, R.id.nav_products)

        recyclerView = findViewById(R.id.RecyProducts)
        mainView = findViewById(R.id.main)
        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<ImageButton>(R.id.buttonAgregarProducto).setOnClickListener {
            showAddProductStep1()
        }

        mostrarProductos()
    }

    private fun showAddProductStep1() {
        val dialog = BottomSheetDialog(this)

        val view = layoutInflater.inflate(R.layout.dialog_select_subcategory, null)
        dialog.setContentView(view)

        val autoComplete = view.findViewById<AutoCompleteTextView>(R.id.productCategorySelect)
        val btnContinuar = view.findViewById<Button>(R.id.btnNextToDetails)

        val options = subcategoryMap.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, options)
        autoComplete.setAdapter(adapter)

        autoComplete.setOnClickListener { autoComplete.showDropDown() }

        btnContinuar.setOnClickListener {
            val selection = autoComplete.text.toString()
            val subId = subcategoryMap[selection]

            if (subId != null) {
                val intent = Intent(this, AddProductsActivity::class.java).apply {
                    putExtra("SELECTED_SUBCATEGORY_ID", subId)
                    putExtra("SELECTED_SUBCATEGORY_NAME", selection)
                }
                startActivityForResult(intent, 100)
                dialog.dismiss()
            } else {
                autoComplete.error = "Seleccione una categoría válida"
            }
        }
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data?.getBooleanExtra("PRODUCT_UPDATED", false) == true) {
            mostrarProductos()
            Snackbar.make(mainView, "Producto actualizado correctamente", Snackbar.LENGTH_SHORT)
                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                .show()
        }
    }

    override fun onItemClick(product: Product) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.product_modal_details, null)

        view.findViewById<TextView>(R.id.txtProductSerial).text = "Serial: ${product.product_serial}"
        view.findViewById<TextView>(R.id.txtProductModel).text = "Modelo: ${product.product_model}"
        view.findViewById<TextView>(R.id.txtCategory).text = "Categoría: ${product.category_name}"
        view.findViewById<TextView>(R.id.txtProductBill).text = "Orden entrada: ${product.input_order_bill}"

        dialog.setContentView(view)
        val bottomSheet = dialog.findViewById<FrameLayout>(
            dialog.context.resources.getIdentifier("design_bottom_sheet", "id", "com.google.android.material")
        )

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
            putExtra("NAME", product.subcategory_name)
            putExtra("DETAILS_ID", product.product_details_id)
            putExtra("ORDER_ID", product.input_order_id)
        }
        startActivityForResult(intent, 100)
    }

    override fun onDeleteClick(id: Int) {
        RetrofitInstance.apiProducts.deleteProduct(id).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful || response.body()?.success == false) {
                    mostrarProductos()
                    Snackbar.make(mainView, "Producto eliminado", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Snackbar.make(mainView, "Error de red: ${t.message}", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    fun mostrarProductos() {
        RetrofitInstance.apiProducts.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    recyclerView.adapter = ProductAdapter(response.body() ?: emptyList(), this@ProductsActivity)
                }
            }
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Snackbar.make(mainView, "Error de red: ${t.message}", Snackbar.LENGTH_SHORT).show()
            }
        })
    }
}