package com.example.mediturnopty.modelos

data class Hospital(
    val idHospital: Int = 0,
    val nombre: String,
    val tipo: String,
    val direccion: String,
    val telefono: String,
    val horario: String,
    val especialidades: String,
    val descripcion: String,
    val estado: String = "ACTIVO"
)
