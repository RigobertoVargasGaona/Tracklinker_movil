package com.example.appinterface
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.adapterWarranties.WarrantyAdapter
import com.example.appinterface.Adapter.adapterWarranties.OnWarrantyClickListener
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.Api.Models.DataResponseWarranty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), OnWarrantyClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)



        val btnAgregar = findViewById<ImageButton>(R.id.buttonAgregarGarantia)
        btnAgregar.setOnClickListener {
            val intent = Intent(this, AddWarrantyActivity::class.java)
            startActivity(intent)
        }
        mostrarGarantias()

    }

    override fun onEditClick(warranty: DataResponseWarranty) {
        // Enviar datos a la actividad de Productos para editar
        val intent = Intent(this, AddWarrantyActivity::class.java).apply {
            putExtra("EDITING_ID", warranty.warranty_incidents_id)
            putExtra("SERIAL", warranty.product_serial)
            putExtra("CUSTOMER", warranty.warranty_customer)
            putExtra("CITY", warranty.warranty_city)
            putExtra("PHONE", warranty.warranty_phone)
            putExtra("ADDRESS", warranty.warranty_address)
            putExtra("DESCRIPTION", warranty.warranty_description)
        }
        startActivity(intent)
    }

    override fun onDeleteClick(id: Int) {
        eliminarGarantia(id)
    }

    fun mostrarGarantias() {
        val recyclerView = findViewById<RecyclerView>(R.id.RecyPersonas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        RetrofitInstance.api2kotlin.getWarranties().enqueue(object : Callback<List<DataResponseWarranty>> {
            override fun onResponse(call: Call<List<DataResponseWarranty>>, response: Response<List<DataResponseWarranty>>) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        recyclerView.adapter = WarrantyAdapter(data, this@MainActivity)
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
                    mostrarGarantias()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {}
        })
    }
}