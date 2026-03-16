package com.example.appinterface

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.features.warranties.WarrantiesActivity

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val cardUsers = findViewById<androidx.cardview.widget.CardView>(R.id.cardUsers)
        val cardProducts = findViewById<androidx.cardview.widget.CardView>(R.id.cardProducts)
        val cardWarranties = findViewById<androidx.cardview.widget.CardView>(R.id.cardWarranties)


        cardWarranties.setOnClickListener {
            val intent = Intent(this, WarrantiesActivity::class.java)
            startActivity(intent)
        }
    }

}