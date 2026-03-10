package com.example.appinterface

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.adapterWarranties.WarrantyAdapter
import com.example.appinterface.Adapter.adapterWarranties.OnWarrantyClickListener // AGREGADO
import com.example.appinterface.Api.Models.DataResponse
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.Api.Models.DataResponseWarranty
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Se agrega la implementación de OnWarrantyClickListener
class MainActivity : AppCompatActivity(), OnWarrantyClickListener {

    private var warranty: DataResponseWarranty? = null

    private var editingId: Int? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonGoToSecondActivity: Button = findViewById(R.id.buttonSegundaActividad)
        buttonGoToSecondActivity.setOnClickListener {
            val intent = Intent(this, ProductosActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onEditClick(warranty: DataResponseWarranty) {
        // Cargamos los datos en los campos para editar
        findViewById<EditText>(R.id.serial).setText(warranty.product_serial)
        findViewById<EditText>(R.id.customer).setText(warranty.warranty_customer)
        findViewById<EditText>(R.id.city).setText(warranty.warranty_city)
        findViewById<EditText>(R.id.phone).setText(warranty.warranty_phone)
        findViewById<EditText>(R.id.adress).setText(warranty.warranty_address)
        findViewById<EditText>(R.id.description).setText(warranty.warranty_description)

        editingId = warranty.warranty_incidents_id
        Toast.makeText(this, "Editando a: ${warranty.warranty_customer}", Toast.LENGTH_SHORT).show()

    }

    override fun onDeleteClick(id: Int) {
        eliminarGarantia(id)
    }
    // ---------------------------------------



    fun mostrarGarantias(v: View) {
        val recyclerView = findViewById<RecyclerView>(R.id.RecyPersonas)
        recyclerView.layoutManager= LinearLayoutManager(this)

        RetrofitInstance.api2kotlin.getWarranties().enqueue(object : Callback<List<DataResponseWarranty>> {
            override fun onResponse(call: Call<List<DataResponseWarranty>>, response: Response<List<DataResponseWarranty>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (!data.isNullOrEmpty()) {
                        // Pasamos 'this' para que el adapter detecte los clics
                        val adapter = WarrantyAdapter(data, this@MainActivity)
                        recyclerView.adapter = adapter
                    }
                }
            }
            override fun onFailure(call: Call<List<DataResponseWarranty>>, t: Throwable) {}
        })
    }

    fun eliminarGarantia(idParaEliminar: Int) {
        RetrofitInstance.api2kotlin.deleteWarranty(idParaEliminar).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Garantía eliminada", Toast.LENGTH_SHORT).show()
                    mostrarGarantias(findViewById(android.R.id.content)) // Refrescar lista
                } else {
                    Toast.makeText(this@MainActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error de red", Toast.LENGTH_SHORT).show()
            }
        })
    }
}


