package com.munidigital.bc2201.challenge2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.munidigital.bc2201.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseUser
import com.munidigital.bc2201.R

class LoginActivity : AppCompatActivity() {

    lateinit var viewModel: LoginViewModel

    /**
     * Inicializa viewmodel, observer de estado y click listeners.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // VM
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        // Views References
        val etMail = binding.lgEditEmail
        val etPass = binding.lgEditPassword
        val btnLogin = binding.lgButtonLogin

        // Observar el estado
        viewModel.state.observe(this) { state ->
            // Este when equivale a una estructura if/else-if
            when {
                (!state.loginError && state.user != null) -> { // Usuario logueado
                    onLogged(state.user) // Loguea la info del usuario...
                    /*etMail.visibility = View.GONE
                    etPass.visibility = View.GONE
                    btnLogin.text = "LOGOUT"*/
                    startActivity(Intent(this, MainActivity::class.java))
                }
                (!state.loginError && state.user == null) -> { // Usuario sin loguear o recien deslogueado
                    Toast.makeText(this, "Loged out.", Toast.LENGTH_SHORT).show()
                    etMail.visibility = View.VISIBLE
                    etPass.visibility = View.VISIBLE
                    btnLogin.text = "LOGIN"
                }
                (state.loginError) -> { // El usuario intentó iniciar sesión y falló
                    Toast.makeText(this, "Invalid user credentials.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Setear click listener (esto se puede hacer en el xml o con binding, decisión de cada uno)
        btnLogin.setOnClickListener {
            // En esta demo, el botón tiene 2 comportamientos dependiendo del estado de la sesión.
            if (viewModel.state.value?.user == null) {
                val mail = etMail.text.toString()
                val pass = etPass.text.toString()
                val enviado = viewModel.login(mail, pass)
                if (!enviado) Toast.makeText(
                    this,
                    "Ingrese usuario y contraseña",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.logout()
            }
        }

    }

    /**
     * Loguea la info del usuario que inició sesión.
     */
    private fun onLogged(user: FirebaseUser) {
        user.apply {
            Toast.makeText(this@LoginActivity, "Logged!", Toast.LENGTH_SHORT).show()
            email?.let { Log.d("login", it) }
            isEmailVerified.let { Log.d("login", it.toString()) }
            uid.let { Log.d("login", it) }
        }
    }

}