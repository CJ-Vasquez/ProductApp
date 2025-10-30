package com.cibertec.productapp.utils

import android.content.Context

/**
 * SessionManager: Clase deshabilitada - sin funcionalidad de login
 */
class SessionManager(private val context: Context) {

    // Clase sin funcionalidad - solo para evitar errores de compilación
    // El sistema de login ha sido eliminado

    fun isAdminLoggedIn(): Boolean = false

    fun setAdminLoggedIn(isLoggedIn: Boolean) {
        // Sin funcionalidad
    }

    fun logoutAdmin() {
        // Sin funcionalidad
    }
}
