package com.example.appinterface.features.warranties
import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.example.appinterface.MainActivity
import com.example.appinterface.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WarrantiesActivity : AppCompatActivity(), OnWarrantyClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        setContentView(R.layout.warranty_activity_main)



        val btnAgregar = findViewById<ImageButton>(R.id.buttonAgregarGarantia)
        btnAgregar.setOnClickListener {
            val intent = Intent(this, AddWarrantyActivity::class.java)
            startActivity(intent)
        }

        val btnVolver = findViewById<ImageButton>(R.id.buttonVolver)
        btnVolver.setOnClickListener{
            val intent =Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        mostrarGarantias()

    }
    override fun onItemClick(warranty: DataResponseWarranty) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.warranty_modal_details, null)

        // 1. Vincular componentes del XML del modal
        val txtSerial = view.findViewById<TextView>(R.id.txtSerial)
        val txtCustomer = view.findViewById<TextView>(R.id.txtCustomer)
        val txtPhone = view.findViewById<TextView>(R.id.txtPhone)
        val txtAddress = view.findViewById<TextView>(R.id.txtaddress)
        val txtCity = view.findViewById<TextView>(R.id.txtCity)
        val txtDescription = view.findViewById<TextView>(R.id.txtDescription)
        val txtStatus = view.findViewById<TextView>(R.id.txtStatus)


        // 2. Asignar los valores del modelo
        txtSerial.text = "Serial: ${warranty.product_serial}"
        txtCustomer.text = "Cliente: ${warranty.warranty_customer}"
        txtDescription.text = warranty.warranty_description
        txtPhone.text = "Telefono: ${warranty.warranty_phone}"
        txtAddress.text= "Dirección: ${warranty.warranty_address}"
        txtCity.text = "Ciudad: ${warranty.warranty_city}"
        txtStatus.text = "Estatus: ${warranty.warranty_status}"



        // Lógica simple para el estado
        txtStatus.text = if (warranty.warranty_status == "1") "Activo" else "Pendiente"

        dialog.setContentView(view)

        // 3. Control de altura (600px como ejemplo de altura específica)
        val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.peekHeight = 1200 // Ajusta este valor según prefieras
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        dialog.show()
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
                        recyclerView.adapter = WarrantyAdapter(data, this@WarrantiesActivity)
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
                    Toast.makeText(this@WarrantiesActivity, "Garantía eliminada", Toast.LENGTH_SHORT).show()
                    mostrarGarantias()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {}
        })
    }
}