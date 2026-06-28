package com.example.mediturnopty.utilidades

import android.content.Context
import com.example.mediturnopty.modelos.Usuario

class SesionUsuario(context: Context) {

    private val prefs = context.getSharedPreferences(Constantes.PREFS_SESION, Context.MODE_PRIVATE)

    fun guardarSesion(usuario: Usuario) {
        prefs.edit()
            .putBoolean(Constantes.KEY_SESION_INICIADA, true)
            .putInt(Constantes.KEY_ID_USUARIO, usuario.idUsuario)
            .putString(Constantes.KEY_NOMBRE_USUARIO, usuario.nombre)
            .putString(Constantes.KEY_CORREO_USUARIO, usuario.correo)
            .putString(Constantes.KEY_ROL_USUARIO, usuario.rol)
            .apply()
    }

    fun haySesionIniciada(): Boolean {
        return prefs.getBoolean(Constantes.KEY_SESION_INICIADA, false)
    }

    fun obtenerIdUsuario(): Int {
        return prefs.getInt(Constantes.KEY_ID_USUARIO, 0)
    }

    fun obtenerNombreUsuario(): String {
        return prefs.getString(Constantes.KEY_NOMBRE_USUARIO, "") ?: ""
    }

    fun obtenerCorreoUsuario(): String {
        return prefs.getString(Constantes.KEY_CORREO_USUARIO, "") ?: ""
    }

    fun obtenerRolUsuario(): String {
        return prefs.getString(Constantes.KEY_ROL_USUARIO, "") ?: ""
    }

    fun guardarModoOscuro(activo: Boolean) {
        prefs.edit().putBoolean(Constantes.KEY_MODO_OSCURO, activo).apply()
    }

    fun obtenerModoOscuro(): Boolean {
        return prefs.getBoolean(Constantes.KEY_MODO_OSCURO, false)
    }

    fun cerrarSesion() {
        val modoOscuro = obtenerModoOscuro()
        prefs.edit().clear().putBoolean(Constantes.KEY_MODO_OSCURO, modoOscuro).apply()
    }
}
