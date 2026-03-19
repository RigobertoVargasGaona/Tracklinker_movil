package com.example.appinterface

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.home.HomeAdapter
import com.example.appinterface.Adapter.home.HomeItem
import com.example.appinterface.helpers.BottomNavHelper
import com.github.mikephil.charting.data.Entry

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        findViewById<TextView>(R.id.top_user_gretting).setText("Hola, Agustín")

        val kpis = listOf(
            HomeItem.Kpi("Productos", "$ 900,000", "+12%"),
            HomeItem.Kpi("Ventas", "$ 12,400", "+8%"),
            HomeItem.Kpi("Clientes", "14", "+3%"),
        )

        val charts = listOf(
            HomeItem.Chart(
                title = "Ventas por semana",
                entries = listOf(
                    Entry(0f, 200f),
                    Entry(1f, 450f),
                    Entry(2f, 300f),
                    Entry(3f, 700f),
                    Entry(4f, 500f),
                )
            )
        )

        // Kpis
        val kpisRecycler = findViewById<RecyclerView>(R.id.KpisRecycler)
        kpisRecycler.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false
        )

        kpisRecycler.adapter = HomeAdapter(kpis)

        // Charts
        val chartsRecycler = findViewById<RecyclerView>(R.id.chartRecycler)
        chartsRecycler.layoutManager = LinearLayoutManager(this)
        chartsRecycler.adapter = HomeAdapter(charts)

        BottomNavHelper.setup(this, R.id.nav_home)
    }
}