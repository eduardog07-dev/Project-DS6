package com.example.mediturnopty.utilidades

import android.util.Patterns

object Validaciones {

    fun estaVacio(valor: String): Boolean {
        return valor.trim().isEmpty()
    }

    fun correoValido(correo: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()
    }

    fun contrasenaValida(contrasena: String): Boolean {
        return contrasena.trim().length >= 4
    }

    fun camposCompletos(vararg campos: String): Boolean {
        return campos.all { it.trim().isNotEmpty() }
    }
}
