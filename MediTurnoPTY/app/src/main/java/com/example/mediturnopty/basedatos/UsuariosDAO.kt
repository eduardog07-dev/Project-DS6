package com.example.mediturnopty.basedatos

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.mediturnopty.modelos.Usuario
import com.example.mediturnopty.utilidades.Constantes

class UsuariosDAO(context: Context) {

    private val helper = BaseDatosHelper(context)

    fun registrarUsuario(usuario: Usuario): Long {
        val db = helper.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", usuario.nombre.trim())
            put("correo", usuario.correo.trim().lowercase())
            put("telefono", usuario.telefono.trim())
            put("contrasena", usuario.contrasena.trim())
            put("rol", usuario.rol)
            put("estado", usuario.estado)
        }
        return db.insert(BaseDatosHelper.TABLA_USUARIOS, null, valores)
    }

    fun validarIngreso(correo: String, contrasena: String, rolEsperado: String): Usuario? {
        val db = helper.readableDatabase
        val cursor = db.rawQuery(
            """
            SELECT * FROM ${BaseDatosHelper.TABLA_USUARIOS}
            WHERE correo = ? AND contrasena = ? AND rol = ? AND estado = 'ACTIVO'
            """.trimIndent(),
            arrayOf(correo.trim().lowercase(), contrasena.trim(), rolEsperado)
        )

        cursor.use {
            return if (it.moveToFirst()) convertirCursorAUsuario(it) else null
        }
    }

    fun validarIngreso(correo: String, contrasena: String): Usuario? {
        val db = helper.readableDatabase
        val cursor = db.rawQuery(
            """
            SELECT * FROM ${BaseDatosHelper.TABLA_USUARIOS}
            WHERE correo = ? AND contrasena = ? AND estado = 'ACTIVO'
            """.trimIndent(),
            arrayOf(correo.trim().lowercase(), contrasena.trim())
        )

        cursor.use {
            return if (it.moveToFirst()) convertirCursorAUsuario(it) else null
        }
    }

    fun existeCorreo(correo: String): Boolean {
        val db = helper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT id_usuario FROM ${BaseDatosHelper.TABLA_USUARIOS} WHERE correo = ?",
            arrayOf(correo.trim().lowercase())
        )

        cursor.use {
            return it.moveToFirst()
        }
    }

    fun obtenerUsuarioPorId(idUsuario: Int): Usuario? {
        val db = helper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${BaseDatosHelper.TABLA_USUARIOS} WHERE id_usuario = ?",
            arrayOf(idUsuario.toString())
        )

        cursor.use {
            return if (it.moveToFirst()) convertirCursorAUsuario(it) else null
        }
    }

    fun actualizarPerfil(idUsuario: Int, nombre: String, telefono: String): Int {
        val db = helper.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", nombre.trim())
            put("telefono", telefono.trim())
        }
        return db.update(
            BaseDatosHelper.TABLA_USUARIOS,
            valores,
            "id_usuario = ?",
            arrayOf(idUsuario.toString())
        )
    }

    fun contarClientes(): Int {
        val db = helper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT COUNT(*) AS total FROM ${BaseDatosHelper.TABLA_USUARIOS} WHERE rol = ? AND estado = 'ACTIVO'",
            arrayOf(Constantes.ROL_CLIENTE)
        )

        cursor.use {
            return if (it.moveToFirst()) it.getInt(it.getColumnIndexOrThrow("total")) else 0
        }
    }

    private fun convertirCursorAUsuario(cursor: Cursor): Usuario {
        return Usuario(
            idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow("id_usuario")),
            nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
            correo = cursor.getString(cursor.getColumnIndexOrThrow("correo")),
            telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
            contrasena = cursor.getString(cursor.getColumnIndexOrThrow("contrasena")),
            rol = cursor.getString(cursor.getColumnIndexOrThrow("rol")),
            estado = cursor.getString(cursor.getColumnIndexOrThrow("estado"))
        )
    }
}
