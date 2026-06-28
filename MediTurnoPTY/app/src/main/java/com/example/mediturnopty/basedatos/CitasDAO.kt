package com.example.mediturnopty.basedatos

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.mediturnopty.modelos.Cita

class CitasDAO(context: Context) {

    private val helper = BaseDatosHelper(context)

    fun registrarCita(cita: Cita): Long {
        val db = helper.writableDatabase
        val valores = ContentValues().apply {
            put("id_usuario", cita.idUsuario)
            put("id_hospital", cita.idHospital)
            put("especialidad", cita.especialidad.trim())
            put("doctor", cita.doctor.trim())
            put("fecha", cita.fecha.trim())
            put("hora", cita.hora.trim())
            put("notas", cita.notas.trim())
        }
        return db.insert(BaseDatosHelper.TABLA_CITAS, null, valores)
    }

    fun listarCitasPorUsuario(idUsuario: Int): ArrayList<Cita> {
        val citas = ArrayList<Cita>()
        val db = helper.readableDatabase
        val cursor = db.rawQuery(
            """
            SELECT c.*, h.nombre AS nombre_hospital
            FROM ${BaseDatosHelper.TABLA_CITAS} c
            LEFT JOIN ${BaseDatosHelper.TABLA_HOSPITALES} h ON c.id_hospital = h.id_hospital
            WHERE c.id_usuario = ?
            ORDER BY c.fecha, c.hora
            """.trimIndent(),
            arrayOf(idUsuario.toString())
        )

        cursor.use {
            while (it.moveToNext()) {
                citas.add(convertirCursorACita(it))
            }
        }
        return citas
    }

    fun actualizarCita(cita: Cita): Int {
        val db = helper.writableDatabase
        val valores = ContentValues().apply {
            put("id_hospital", cita.idHospital)
            put("especialidad", cita.especialidad.trim())
            put("doctor", cita.doctor.trim())
            put("fecha", cita.fecha.trim())
            put("hora", cita.hora.trim())
            put("notas", cita.notas.trim())
        }
        return db.update(
            BaseDatosHelper.TABLA_CITAS,
            valores,
            "id_cita = ? AND id_usuario = ?",
            arrayOf(cita.idCita.toString(), cita.idUsuario.toString())
        )
    }

    fun eliminarCita(idCita: Int, idUsuario: Int): Int {
        val db = helper.writableDatabase
        return db.delete(
            BaseDatosHelper.TABLA_CITAS,
            "id_cita = ? AND id_usuario = ?",
            arrayOf(idCita.toString(), idUsuario.toString())
        )
    }

    fun contarCitasPorUsuario(idUsuario: Int): Int {
        val db = helper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT COUNT(*) AS total FROM ${BaseDatosHelper.TABLA_CITAS} WHERE id_usuario = ?",
            arrayOf(idUsuario.toString())
        )

        cursor.use {
            return if (it.moveToFirst()) it.getInt(it.getColumnIndexOrThrow("total")) else 0
        }
    }

    private fun convertirCursorACita(cursor: Cursor): Cita {
        val indiceHospital = cursor.getColumnIndex("nombre_hospital")
        val nombreHospital = if (indiceHospital >= 0 && !cursor.isNull(indiceHospital)) {
            cursor.getString(indiceHospital)
        } else {
            "Hospital no disponible"
        }

        return Cita(
            idCita = cursor.getInt(cursor.getColumnIndexOrThrow("id_cita")),
            idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow("id_usuario")),
            idHospital = cursor.getInt(cursor.getColumnIndexOrThrow("id_hospital")),
            especialidad = cursor.getString(cursor.getColumnIndexOrThrow("especialidad")),
            doctor = cursor.getString(cursor.getColumnIndexOrThrow("doctor")),
            fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
            hora = cursor.getString(cursor.getColumnIndexOrThrow("hora")),
            notas = cursor.getString(cursor.getColumnIndexOrThrow("notas")),
            nombreHospital = nombreHospital
        )
    }
}
