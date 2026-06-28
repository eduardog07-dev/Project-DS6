package com.example.mediturnopty.modelos

data class Usuario(
    val idUsuario: Int = 0,
    val nombre: String,
    val correo: String,
    val telefono: String,
    val contrasena: String,
    val rol: String,
    val estado: String = "ACTIVO"
)
