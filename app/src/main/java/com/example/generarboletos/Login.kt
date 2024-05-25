package com.example.generarboletos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var login: Button
    lateinit var crearCuenta: Button
    lateinit var recuperarContra: Button
    lateinit var email: EditText
    lateinit var pass: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        login = findViewById(R.id.IniciarSesionBtn)
        crearCuenta = findViewById(R.id.RegistrarseBtn)
        recuperarContra = findViewById(R.id.RecuperaBtn)
        email = findViewById(R.id.UserInput)
        pass = findViewById(R.id.UserPassword)

        login.setOnClickListener {
            val emailText = email.text.toString().trim()
            val passText = pass.text.toString().trim()

            if (emailText.isNotEmpty() && passText.isNotEmpty()) {
                auth.signInWithEmailAndPassword(emailText, passText).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this, Inicio::class.java).putExtra("saludo", "Menu principal"))
                        Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                        finish()
                    } else if (task != null && task.exception != null) {
                        val mensajeError = when (task.exception) {
                            is FirebaseAuthInvalidCredentialsException -> "Credenciales inválidas"
                            is FirebaseAuthInvalidUserException -> "Usuario no válido"
                            is FirebaseAuthEmailException -> "El formato del correo No es valido"
                            else -> "Error: " + task.exception?.message ?: "Error desconocido"
                        }
                        Toast.makeText(this, mensajeError, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Error: Ha ocurrido un problema durante el inicio de sesión", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Ingresar correo y password", Toast.LENGTH_LONG).show()
            }
        }

        recuperarContra.setOnClickListener {
            startActivity(Intent(this, RecuperarContra::class.java).putExtra("saludo", "Recuperar contraseña"))
        }

        crearCuenta.setOnClickListener {
            startActivity(Intent(this, Registrarse::class.java).putExtra("saludo", "Registrar nuevo usuario"))
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser == null) {
            //Toast.makeText(this,"No hay usuarios autenticados", Toast.LENGTH_LONG).show()
        } else {
            startActivity(Intent(this, Inicio::class.java))
            finish()
        }
    }
}
