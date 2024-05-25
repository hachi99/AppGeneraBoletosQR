package com.example.generarboletos

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RecuperarContra : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_contra)

        val btnRecupera = findViewById<Button>(R.id.recuperaboton)
        btnRecupera.setOnClickListener{
            val handler = Handler()
            handler.postDelayed({
                finish()
            }, 2000) // 2000 milisegundos = 2 segundos
            Toast.makeText(applicationContext, "FUNCION NO IMPLEMENTADA", Toast.LENGTH_LONG).show()


        }
    }






}