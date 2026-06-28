package com.example.mediturnopty.basedatos

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.mediturnopty.modelos.Medicamento

class MedicamentosDAO(context: Context) {

    private val helper = BaseDatosHelper(context)

    fun registrarMedicamento(medicamento: Medicamento): Long {
        val db = helper.writableDatabase
        val valores = ContentValues().apply {
            put("id_usuario", medicamento.idUsuario)
            put("nombre", medicamento.nombre.trim())
            put("dosis", medicamento.dosis.trim())
            put("frecuencia", medicamento.frecuencia.trim())
            put("hora", medicamento.hora.trim())
            put("observaciones", medicamento.observaciones.trim())
        }
        return db.insert(BaseDatosHelper.TABLA_MEDICAMENTOS, null, valores)
    }

    fun listarMedicamentosPorUsuario(idUsuario: Int): ArrayList<Medicamento> {
        val medicamentos = ArrayList<Medicamento>()
        val db = helper.readableDatabase
        val cursor = db.rawQuery(
            """
            SELECT * FROM ${BaseDatosHelper.TABLA_MEDICAMENTOS}
            WHERE id_usuario = ?
            ORDER BY nombre
            """.trimIndent(),
            arrayOf(idUsuario.toString())
        )

        cursor.use {
            while (it.moveToNext()) {
                medicamentos.add(convertirCursorAMedicamento(it))
            }
        }
        return medicamentos
    }

    fun actualizarMedicamento(medicamento: Medicamento): Int {
        val db = helper.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", medicamento.nombre.trim())
            put("dosis", medicamento.dosis.trim())
            put("frecuencia", medicamento.frecuencia.trim())
            put("hora", medicamento.hora.trim())
            put("observaciones", medicamento.observaciones.trim())
        }
        return db.update(
            BaseDatosHelper.TABLA_MEDICAMENTOS,
            valores,
            "id_medicamento = ? AND id_usuario = ?",
            arrayOf(medicamento.idMedicamento.toString(), medicamento.idUsuario.toString())
        )
    }

    fun eliminarMedicamento(idMedicamento: Int, idUsuario: Int): Int {
        val db = helper.writableDatabase
        return db.delete(
            BaseDatosHelper.TABLA_MEDICAMENTOS,
            "id_medicamento = ? AND id_usuario = ?",
            arrayOf(idMedicamento.toString(), idUsuario.toString())
        )
    }

    fun contarMedicamentosPorUsuario(idUsuario: Int): Int {
        val db = helper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT COUNT(*) AS total FROM ${BaseDatosHelper.TABLA_MEDICAMENTOS} WHERE id_usuario = ?",
            arrayOf(idUsuario.toString())
        )

        cursor.use {
            return if (it.moveToFirst()) it.getInt(it.getColumnIndexOrThrow("total")) else 0
        }
    }

    private fun convertirCursorAMedicamento(cursor: Cursor): Medicamento {
        return Medicamento(
            idMedicamento = cursor.getInt(cursor.getColumnIndexOrThrow("id_medicamento")),
            idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow("id_usuario")),
            nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
            dosis = cursor.getString(cursor.getColumnIndexOrThrow("dosis")),
            frecuencia = cursor.getString(cursor.getColumnIndexOrThrow("frecuencia")),
            hora = cursor.getString(cursor.getColumnIndexOrThrow("hora")),
            observaciones = cursor.getString(cursor.getColumnIndexOrThrow("observaciones"))
        )
    }
}
