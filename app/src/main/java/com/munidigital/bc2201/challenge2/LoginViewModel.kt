package com.munidigital.bc2201.challenge2

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel(app : Application) : AndroidViewModel(app) {

    // Obtiene el Firebase auth. Docs: https://firebase.google.com/docs/auth/android/start
    private var auth: FirebaseAuth = Firebase.auth

    // Inicializo el estado en (login = false, user = null)
    private val _state = MutableLiveData(State(false, null))
    // Encapsulo el estado en un inmutable para los que acceden desde afuera.
    val state : LiveData<State> = _state

    /**
     * Define el tipo de estado
     */
    data class State(
        val loginError: Boolean,
        val user: FirebaseUser?
    )

    /**
     * Constructor
     * Chequea si hay una sesión existente, en ese caso cambia el estado.
     * */
    init {
        val user = auth.currentUser
        if (user != null){
            _state.value = (State(
                loginError = false,
                user = user
            ))
        }
    }

    /**
     * Inicia sesión en Firebase.
     * Si no hay ningún problema de conexión, setea el estado en logueado o en login error
     * de acuerdo a la respuesta de Firebase.
     * @return false si mail y pass están en blanco.
     * @return true de lo contrario
     */
    fun login(mail: String, pass: String) : Boolean {
        if (mail.isNotBlank() && pass.isNotBlank()) {
            Log.d("login", "sending")
            auth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Logueo correcto, cambio el estado
                        _state.value = State(
                            false,
                            auth.currentUser
                        )
                        Log.d("login", "success")
                    } else {
                        // Logueo incorrecto, cambio el estado
                        _state.value = State(
                            true,
                            null
                        )
                        Log.d("login", "failure", task.exception)
                    }
                }
            return true
        } else {
            return false
        }
    }

    /**
     * Cierra sesión y actualiza el estado
     */
    fun logout() {
        auth.signOut()
        _state.value = State(
            false,
            null
        )
    }

}