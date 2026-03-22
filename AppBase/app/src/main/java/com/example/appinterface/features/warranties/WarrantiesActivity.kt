package com.example.appinterface.features.warranties

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.adapterWarranties.WarrantyAdapter
import com.example.appinterface.Adapter.adapterWarranties.OnWarrantyClickListener
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.Api.Models.DataResponseWarranty
import com.example.appinterface.R
import com.example.appinterface.helpers.BottomNavHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WarrantiesActivity : AppCompatActivity(), OnWarrantyClickListener {

    private lateinit var btnPending: Button
    private lateinit var btnReceive: Button
    private lateinit var btnFinished: Button
    private lateinit var btnAll: Button
    private lateinit var filterButtons: List<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        setContentView(R.layout.warranty_activity_main)

        // Configurar Barra de Navegación Inferior
        BottomNavHelper.setup(this, R.id.nav_warranties)

        // Inicializar componentes
        btnPending = findViewById(R.id.pending)
        btnReceive = findViewById(R.id.receive)
        btnFinished = findViewById(R.id.finished)
        btnAll = findViewById(R.id.all)
        filterButtons = listOf(btnPending, btnReceive, btnFinished, btnAll)

        val btnAgregar = findViewById<ImageButton>(R.id.buttonAgregarGarantia)

        // Listeners de Filtros
        btnReceive.setOnClickListener {
            showWarranties("0")
            updateFilterStyle(btnReceive)
        }
        btnPending.setOnClickListener {
            showWarranties("1")
            updateFilterStyle(btnPending)
        }
        btnFinished.setOnClickListener {
            showWarranties("2")
            updateFilterStyle(btnFinished)
        }
        btnAll.setOnClickListener {
            showWarranties("3")
            updateFilterStyle(btnAll)
        }

        btnAgregar.setOnClickListener {
            startActivity(Intent(this, AddWarrantyActivity::class.java))
        }

        // Estado inicial
        updateFilterStyle(btnAll)
        showWarranties("3")
    }

    private fun updateFilterStyle(selected: Button) {
        filterButtons.forEach { btn ->
            if (btn == selected) {
                // Estilo ACTIVO (Azul/Oscuro con texto blanco)
                btn.setBackgroundResource(R.drawable.bg_nav_active)
                btn.setTextColor(Color.WHITE)
            } else {
                // Estilo INACTIVO (Gris con texto oscuro)
                btn.setBackgroundResource(R.drawable.boton_redondeado_gris)
                btn.setTextColor(Color.parseColor("#666666"))
            }
        }
    }

    fun showWarranties(state: String = "3") {
        val recyclerView = findViewById<RecyclerView>(R.id.RecyWarranties)
        recyclerView.layoutManager = LinearLayoutManager(this)

        RetrofitInstance.api2kotlin.getWarranties().enqueue(object : Callback<List<DataResponseWarranty>> {
            override fun onResponse(call: Call<List<DataResponseWarranty>>, response: Response<List<DataResponseWarranty>>) {
                if (response.isSuccessful) {
                    val allWarranties = response.body() ?: listOf()
                    val filteredList = if (state == "3") allWarranties else allWarranties.filter { it.warranty_status == state }
                    recyclerView.adapter = WarrantyAdapter(filteredList, this@WarrantiesActivity)
                }
            }
            override fun onFailure(call: Call<List<DataResponseWarranty>>, t: Throwable) {
                Toast.makeText(this@WarrantiesActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onItemClick(warranty: DataResponseWarranty) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.warranty_modal_details, null)

        view.findViewById<TextView>(R.id.txtSerial).text = "Serial: ${warranty.product_serial}"
        view.findViewById<TextView>(R.id.txtCustomer).text = "Cliente: ${warranty.warranty_customer}"
        view.findViewById<TextView>(R.id.txtDescription).text = warranty.warranty_description
        view.findViewById<TextView>(R.id.txtPhone).text = "Tel: ${warranty.warranty_phone}"
        view.findViewById<TextView>(R.id.txtaddress).text = "Dirección: ${warranty.warranty_address}"
        view.findViewById<TextView>(R.id.txtCity).text = "Ciudad: ${warranty.warranty_city}"
        view.findViewById<TextView>(R.id.txtDate).text = "Fecha:${warranty.warranty_date}"

        val statusText = when(warranty.warranty_status) {
            "0" -> "Recibida"
            "1" -> "En Proceso"
            "2" -> "Finalizada"
            else -> "Pendiente"
        }
        view.findViewById<TextView>(R.id.txtStatus).text = "Estatus: $statusText"

        dialog.setContentView(view)
        dialog.show()
    }

    override fun onEditClick(warranty: DataResponseWarranty) {
        val intent = Intent(this, AddWarrantyActivity::class.java).apply {
            putExtra("EDITING_ID", warranty.warranty_incidents_id)
            putExtra("SERIAL", warranty.product_serial)
            putExtra("CUSTOMER", warranty.warranty_customer)
            putExtra("CITY", warranty.warranty_city)
            putExtra("PHONE", warranty.warranty_phone)
            putExtra("ADDRESS", warranty.warranty_address)
            putExtra("DESCRIPTION", warranty.warranty_description)
            putExtra("STATUS", warranty.warranty_status)
        }
        startActivity(intent)
    }

    override fun onDeleteClick(id: Int) {
        RetrofitInstance.api2kotlin.deleteWarranty(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@WarrantiesActivity, "Garantía eliminada", Toast.LENGTH_SHORT).show()
                    showWarranties("3")
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {}
        })
    }
}