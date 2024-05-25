package com.example.generarboletos

import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Registrarse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)

        val btnRegistra = findViewById<Button>(R.id.Registro)
        btnRegistra.setOnClickListener{
            val handler = Handler()
            handler.postDelayed({
                finish()
            }, 2000) // 2000 milisegundos = 2 segundos
            Toast.makeText(applicationContext, "FUNCION NO IMPLEMENTADA", Toast.LENGTH_LONG).show()


        }
    }

}