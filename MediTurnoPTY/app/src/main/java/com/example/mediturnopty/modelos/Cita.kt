package com.example.mediturnopty.modelos

data class Cita(
    val idCita: Int = 0,
    val idUsuario: Int,
    val idHospital: Int,
    val especialidad: String,
    val doctor: String,
    val fecha: String,
    val hora: String,
    val notas: String,
    val nombreHospital: String = ""
)
