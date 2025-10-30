package com.cibertec.productapp.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cibertec.productapp.MainActivity

/**
 * LoginActivity: Clase deshabilitada - redirige directamente a MainActivity
 * El sistema de login ha sido eliminado del proyecto
 */
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Redirigir inmediatamente a MainActivity
        // No hay interfaz de login - el sistema ha sido eliminado
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
