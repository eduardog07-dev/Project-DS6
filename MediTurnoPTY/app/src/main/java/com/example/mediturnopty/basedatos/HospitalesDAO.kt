package com.example.mediturnopty.basedatos

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.mediturnopty.modelos.Hospital

class HospitalesDAO(context: Context) {

    private val helper = BaseDatosHelper(context)

    fun registrarHospital(hospital: Hospital): Long {
        val db = helper.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", hospital.nombre.trim())
            put("tipo", hospital.tipo.trim())
            put("direccion", hospital.direccion.trim())
            put("telefono", hospital.telefono.trim())
            put("horario", hospital.horario.trim())
            put("especialidades", hospital.especialidades.trim())
            put("descripcion", hospital.descripcion.trim())
            put("estado", hospital.estado)
        }
        return db.insert(BaseDatosHelper.TABLA_HOSPITALES, null, valores)
    }

    fun listarHospitalesActivos(): ArrayList<Hospital> {
        val hospitales = ArrayList<Hospital>()
        val db = helper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${BaseDatosHelper.TABLA_HOSPITALES} WHERE estado = 'ACTIVO' ORDER BY nombre",
            null
        )

        cursor.use {
            while (it.moveToNext()) {
                hospitales.add(convertirCursorAHospital(it))
            }
        }
        return hospitales
    }

    fun buscarHospitales(texto: String): ArrayList<Hospital> {
        if (texto.trim().isEmpty()) {
            return listarHospitalesActivos()
        }

        val hospitales = ArrayList<Hospital>()
        val db = helper.readableDatabase
        val filtro = "%${texto.trim()}%"
        val cursor = db.rawQuery(
            """
            SELECT * FROM ${BaseDatosHelper.TABLA_HOSPITALES}
            WHERE estado = 'ACTIVO'
            AND (nombre LIKE ? OR tipo LIKE ? OR especialidades LIKE ? OR direccion LIKE ?)
            ORDER BY nombre
            """.trimIndent(),
            arrayOf(filtro, filtro, filtro, filtro)
        )

        cursor.use {
            while (it.moveToNext()) {
                hospitales.add(convertirCursorAHospital(it))
            }
        }
        return hospitales
    }

    fun obtenerHospitalPorId(idHospital: Int): Hospital? {
        val db = helper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${BaseDatosHelper.TABLA_HOSPITALES} WHERE id_hospital = ?",
            arrayOf(idHospital.toString())
        )

        cursor.use {
            return if (it.moveToFirst()) convertirCursorAHospital(it) else null
        }
    }

    fun actualizarHospital(hospital: Hospital): Int {
        val db = helper.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", hospital.nombre.trim())
            put("tipo", hospital.tipo.trim())
            put("direccion", hospital.direccion.trim())
            put("telefono", hospital.telefono.trim())
            put("horario", hospital.horario.trim())
            put("especialidades", hospital.especialidades.trim())
            put("descripcion", hospital.descripcion.trim())
            put("estado", hospital.estado)
        }
        return db.update(
            BaseDatosHelper.TABLA_HOSPITALES,
            valores,
            "id_hospital = ?",
            arrayOf(hospital.idHospital.toString())
        )
    }

    fun eliminarHospital(idHospital: Int): Int {
        val db = helper.writableDatabase
        val valores = ContentValues().apply {
            put("estado", "INACTIVO")
        }
        return db.update(
            BaseDatosHelper.TABLA_HOSPITALES,
            valores,
            "id_hospital = ?",
            arrayOf(idHospital.toString())
        )
    }

    fun contarHospitalesActivos(): Int {
        val db = helper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT COUNT(*) AS total FROM ${BaseDatosHelper.TABLA_HOSPITALES} WHERE estado = 'ACTIVO'",
            null
        )

        cursor.use {
            return if (it.moveToFirst()) it.getInt(it.getColumnIndexOrThrow("total")) else 0
        }
    }

    private fun convertirCursorAHospital(cursor: Cursor): Hospital {
        return Hospital(
            idHospital = cursor.getInt(cursor.getColumnIndexOrThrow("id_hospital")),
            nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
            tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo")),
            direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion")),
            telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
            horario = cursor.getString(cursor.getColumnIndexOrThrow("horario")),
            especialidades = cursor.getString(cursor.getColumnIndexOrThrow("especialidades")),
            descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
            estado = cursor.getString(cursor.getColumnIndexOrThrow("estado"))
        )
    }
}
