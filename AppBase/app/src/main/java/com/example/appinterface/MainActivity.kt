package com.example.appinterface

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.features.users.UsersActivity
import com.example.appinterface.features.warranties.WarrantiesActivity

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        findViewById<TextView>(R.id.top_user_gretting).setText("Hola, Agustín")

        val cardUsers = findViewById<androidx.cardview.widget.CardView>(R.id.cardUsers)
        val cardProducts = findViewById<androidx.cardview.widget.CardView>(R.id.cardProducts)
        val cardWarranties = findViewById<androidx.cardview.widget.CardView>(R.id.cardWarranties)

        cardUsers.setOnClickListener {
            val intent = Intent(this, UsersActivity::class.java)
            startActivity(intent)
        }

        cardWarranties.setOnClickListener {
            val intent = Intent(this, WarrantiesActivity::class.java)
            startActivity(intent)
        }
    }

}