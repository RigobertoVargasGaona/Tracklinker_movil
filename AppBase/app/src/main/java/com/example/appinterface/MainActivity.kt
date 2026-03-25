package com.example.appinterface

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.home.HomeAdapter
import com.example.appinterface.Adapter.home.HomeItem
import com.example.appinterface.Api.Models.DataResponseCharts
import com.example.appinterface.Api.Models.DataResponseKpis
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.helpers.BottomNavHelper
import com.github.mikephil.charting.data.Entry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        findViewById<TextView>(R.id.top_user_gretting).setText("Hola, Agustín")

        BottomNavHelper.setup(this, R.id.nav_home)

        // Charts
        val chartsRecycler = findViewById<RecyclerView>(R.id.chartRecycler)
        chartsRecycler.layoutManager = LinearLayoutManager(this@MainActivity)
        val chartsAdapter = HomeAdapter(mutableListOf())
        chartsRecycler.adapter = chartsAdapter

        RetrofitInstance.reportsApi.getChartInfo().enqueue(object : Callback<DataResponseCharts> {
            override fun onResponse(call: Call<DataResponseCharts>, response: Response<DataResponseCharts>) {
                if (response.isSuccessful) {
                    val body = response.body()?.data ?: return

                    val entries = body.mapIndexed { index, item ->
                        Entry(index.toFloat(), item.products.toFloat())
                    }

                    val charts = listOf(
                        HomeItem.Chart(
                            title = "Productos Agregados por mes del año 2024",
                            entries = entries
                        )
                    )

                    runOnUiThread {
                        chartsAdapter.updateData(charts)
                    }
                }
            }
            override fun onFailure(call: Call<DataResponseCharts>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error al intentar cargar la informacion", Toast.LENGTH_SHORT).show()
            }
        })

        // Kpis
        val kpisRecycler = findViewById<RecyclerView>(R.id.KpisRecycler)
        kpisRecycler.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        val kpisAdapter = HomeAdapter(mutableListOf())
        kpisRecycler.adapter = kpisAdapter

        RetrofitInstance.reportsApi.getKpisInfo().enqueue(object : Callback<DataResponseKpis> {
            override fun onResponse(call: Call<DataResponseKpis>, response: Response<DataResponseKpis>) {
                if (response.isSuccessful) {
                    val data = response.body()?.data ?: return

                    val kpis = data.flatMap { item ->
                        mutableListOf<HomeItem>(
                            HomeItem.Kpi("Productos", item.products.toString(), "+0"),
                            HomeItem.Kpi("Clientes", item.clients.toString(), "+0"),
                            HomeItem.Kpi("Usuarios", item.users.toString(), "+0"),
                        )
                    }

                    runOnUiThread {
                        kpisAdapter.updateData(kpis)
                    }
                }
            }
            override fun onFailure(call: Call<DataResponseKpis>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error al intentar cargar la informacion", Toast.LENGTH_SHORT).show()
            }
        })
    }
}