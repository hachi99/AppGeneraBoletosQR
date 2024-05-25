package com.example.generarboletos

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlin.random.Random



class Inicio() : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database
    private val myRef = database.getReference("QR Ticket")
    private lateinit var QRS: ArrayList<CodigoQR>


    @SuppressLint("WrongViewCast", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        auth = FirebaseAuth.getInstance()
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.salir -> {
                        auth.signOut()
                        startActivity(Intent(this@Inicio, Login::class.java))
                        finish()
                        Toast.makeText(applicationContext, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
        })

        //Read from the database
        //FUNCION LLENA LISTA SOLO CON GENERADO Y USADO
        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                QRS = ArrayList<CodigoQR>()


                val value = snapshot.value.toString()
                Log.d(ContentValues.TAG, "Value is: " + value)

                snapshot.children.forEach { item ->
                    val estado = item.child("estado").value.toString()
                    if (estado == "GENERADO" || estado == "USADO") {
                        val codigo = CodigoQR(
                            estado,
                            item.child("folio").value.toString(),
                            item.child("id").value.toString(),
                            item.key.toString()
                        )
                    QRS.add(codigo)
                }}
                llenaLista()
            }
        /*
        // FUNCION LLENA LISTA CON TODOS LOS ELEMENTOS
        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                QRS = ArrayList<CodigoQR>()


                val value = snapshot.value.toString()
                Log.d(ContentValues.TAG, "Value is: " + value)

                snapshot.children.forEach { item ->
                    var codigo = CodigoQR(
                        item.child("estado").value.toString(),
                        item.child("folio").value.toString(),
                        item.child("id").value.toString(),
                        item.key.toString()
                    )
                    QRS.add(codigo)
                }
                llenaLista()
            }*/


            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())

            }
        })

        val lista = findViewById<ListView>(R.id.ListaDeQR)
        lista.setOnItemClickListener { _, _, position, _ ->
            Toast.makeText(this, "Boleto con folio: " + QRS[position].folio.toString(), Toast.LENGTH_LONG).show()
            startActivity(
                Intent(this, NuevoQR::class.java)
                    .putExtra("folio", QRS[position].folio.toString())
                    .putExtra("estado", QRS[position].estado.toString())
                    .putExtra("id", QRS[position].id)
            )
        }


        val btnNuevoQR = findViewById<FloatingActionButton>(R.id.GeneraNvoQR)
        btnNuevoQR.setOnClickListener {
            generarFolio()
        }
    }
    //Funcion para leer de la base de datos y compararla con el estado que buscamos

    private fun generarFolio() {
        myRef.orderByChild("estado").equalTo("NO GENERADO").limitToFirst(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val foliogen = Random.nextInt(10000, 100000).toString()
                        val boletoId = snapshot.children.first().key
                        myRef.child(boletoId.toString()).child("estado").setValue("GENERADO")
                        myRef.child(boletoId.toString()).child("folio").setValue(foliogen)
                        Toast.makeText(applicationContext, "Boleto generado con éxito!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(applicationContext, "No hay boletos disponibles", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                }
            })
    }

    fun llenaLista() {
        var adaptador = AdaptadorQR(this, QRS)
        var lista = findViewById<ListView>(R.id.ListaDeQR)
        lista.adapter = adaptador
    }
}
