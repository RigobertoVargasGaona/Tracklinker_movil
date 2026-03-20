package com.example.appinterface.features.categories

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.adapterCategories.CategoryAdapter
import com.example.appinterface.Adapter.adapterCategories.OnCategoryClickListener
import com.example.appinterface.Api.CategoryRetrofitInstance
import com.example.appinterface.Api.Models.DataResponseCategory
import com.example.appinterface.MainActivity
import com.example.appinterface.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriesActivity : AppCompatActivity(), OnCategoryClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        setContentView(R.layout.category_activity_main)

        val btnAgregar = findViewById<ImageButton>(R.id.buttonAgregarCategoria)
        btnAgregar.setOnClickListener {
            val intent = Intent(this, AddCategoryActivity::class.java)
            startActivity(intent)
        }

        val btnVolver = findViewById<ImageButton>(R.id.buttonVolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        mostrarCategorias()
    }

    override fun onItemClick(category: DataResponseCategory) {

        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.category_modal_details, null)

        val txtName = view.findViewById<TextView>(R.id.txtCategoryName)
        val txtDescription = view.findViewById<TextView>(R.id.txtCategoryDescription)

        txtName.text = "Nombre: ${category.category_name}"
        txtDescription.text = category.category_description

        // Eliminado txtStatus porque category_status ya no existe
        // txtStatus.text = ...

        dialog.setContentView(view)

        val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.peekHeight = 1200
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        dialog.show()
    }

    override fun onEditClick(category: DataResponseCategory) {

        val intent = Intent(this, AddCategoryActivity::class.java).apply {
            putExtra("EDITING_ID", category.category_id)
            putExtra("NAME", category.category_name)
            putExtra("DESCRIPTION", category.category_description)
        }

        startActivity(intent)
    }

    override fun onDeleteClick(id: Int) {
        eliminarCategoria(id)
    }

    fun mostrarCategorias() {

        val recyclerView = findViewById<RecyclerView>(R.id.RecyCategories)
        recyclerView.layoutManager = LinearLayoutManager(this)

        CategoryRetrofitInstance.api2kotlin.getCategories()
            .enqueue(object : Callback<List<DataResponseCategory>> {

                override fun onResponse(
                    call: Call<List<DataResponseCategory>>,
                    response: Response<List<DataResponseCategory>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { data ->
                            recyclerView.adapter = CategoryAdapter(data, this@CategoriesActivity)
                        }
                    }
                }

                override fun onFailure(call: Call<List<DataResponseCategory>>, t: Throwable) {}
            })
    }

    fun eliminarCategoria(idParaEliminar: Int) {

        CategoryRetrofitInstance.api2kotlin.deleteCategory(idParaEliminar)
            .enqueue(object : Callback<Void> {

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@CategoriesActivity,
                            "Categoría eliminada",
                            Toast.LENGTH_SHORT
                        ).show()
                        mostrarCategorias()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {}
            })
    }
}