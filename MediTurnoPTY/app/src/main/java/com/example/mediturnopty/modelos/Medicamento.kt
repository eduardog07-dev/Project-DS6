package com.example.mediturnopty.modelos

data class Medicamento(
    val idMedicamento: Int = 0,
    val idUsuario: Int,
    val nombre: String,
    val dosis: String,
    val frecuencia: String,
    val hora: String,
    val observaciones: String
)
