package com.munidigital.bc2201.challenge2

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.munidigital.bc2201.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DURATION: Long = 2000 // Setea el tiempo del splash en ms

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = Firebase.auth

        displayAppVersion()

        Handler(Looper.getMainLooper()).postDelayed({
            // TODO: Deberian comprobar si existe un usuario cargado. Si no existe deberia cargar
            // la activity de login. Caso contrario la principal del bot

            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Agregar finish para que al volver atras se cierre la app
        }, SPLASH_DURATION)

    }

    private fun displayAppVersion() {
        try {
            // Obtiene el numero de version y lo carga el textview de abajo a la derecha
            val version = this.packageManager.getPackageInfo(this.packageName, 0).versionName
            binding.tvVersioname.text = version
        } catch (e: PackageManager.NameNotFoundException) {
            // Log por si falla
            e.printStackTrace()
        }
    }
}
