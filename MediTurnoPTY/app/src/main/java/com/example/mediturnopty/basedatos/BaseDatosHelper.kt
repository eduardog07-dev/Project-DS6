package com.example.mediturnopty.basedatos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mediturnopty.utilidades.Constantes

class BaseDatosHelper(context: Context) :
    SQLiteOpenHelper(context, NOMBRE_BASE_DATOS, null, VERSION_BASE_DATOS) {

    companion object {
        private const val NOMBRE_BASE_DATOS = "mediturno_pty.db"
        private const val VERSION_BASE_DATOS = 1

        const val TABLA_USUARIOS = "usuarios"
        const val TABLA_HOSPITALES = "hospitales"
        const val TABLA_CITAS = "citas"
        const val TABLA_MEDICAMENTOS = "medicamentos"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLA_USUARIOS (
                id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                correo TEXT NOT NULL UNIQUE,
                telefono TEXT,
                contrasena TEXT NOT NULL,
                rol TEXT NOT NULL,
                estado TEXT NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLA_HOSPITALES (
                id_hospital INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                tipo TEXT NOT NULL,
                direccion TEXT NOT NULL,
                telefono TEXT NOT NULL,
                horario TEXT NOT NULL,
                especialidades TEXT NOT NULL,
                descripcion TEXT,
                estado TEXT NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLA_CITAS (
                id_cita INTEGER PRIMARY KEY AUTOINCREMENT,
                id_usuario INTEGER NOT NULL,
                id_hospital INTEGER NOT NULL,
                especialidad TEXT NOT NULL,
                doctor TEXT,
                fecha TEXT NOT NULL,
                hora TEXT NOT NULL,
                notas TEXT
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLA_MEDICAMENTOS (
                id_medicamento INTEGER PRIMARY KEY AUTOINCREMENT,
                id_usuario INTEGER NOT NULL,
                nombre TEXT NOT NULL,
                dosis TEXT NOT NULL,
                frecuencia TEXT NOT NULL,
                hora TEXT NOT NULL,
                observaciones TEXT
            )
            """.trimIndent()
        )

        insertarDatosIniciales(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLA_MEDICAMENTOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLA_CITAS")
        db.execSQL("DROP TABLE IF EXISTS $TABLA_HOSPITALES")
        db.execSQL("DROP TABLE IF EXISTS $TABLA_USUARIOS")
        onCreate(db)
    }

    private fun insertarDatosIniciales(db: SQLiteDatabase) {
        val admin = ContentValues().apply {
            put("nombre", "Administrador MediTurno")
            put("correo", "admin@mediturno.com")
            put("telefono", "6000-0000")
            put("contrasena", "1234")
            put("rol", Constantes.ROL_ADMIN)
            put("estado", "ACTIVO")
        }
        db.insert(TABLA_USUARIOS, null, admin)

        insertarHospitalInicial(
            db,
            "Hospital Santo Tomas",
            "Publico",
            "Avenida Balboa, Panama",
            "507-5600",
            "24 horas",
            "Medicina general, Pediatria, Urgencias",
            "Hospital publico de referencia en la ciudad."
        )
        insertarHospitalInicial(
            db,
            "Policlinica CSS",
            "Publico",
            "Via Transistmica, Panama",
            "503-0000",
            "7:00 a.m. - 3:00 p.m.",
            "Medicina general, Laboratorio, Farmacia",
            "Centro de atencion primaria para asegurados."
        )
        insertarHospitalInicial(
            db,
            "Centro Medico Paitilla",
            "Privado",
            "Punta Paitilla, Panama",
            "265-8800",
            "24 horas",
            "Cardiologia, Pediatria, Imagenes, Urgencias",
            "Centro medico privado con varias especialidades."
        )
    }

    private fun insertarHospitalInicial(
        db: SQLiteDatabase,
        nombre: String,
        tipo: String,
        direccion: String,
        telefono: String,
        horario: String,
        especialidades: String,
        descripcion: String
    ) {
        val valores = ContentValues().apply {
            put("nombre", nombre)
            put("tipo", tipo)
            put("direccion", direccion)
            put("telefono", telefono)
            put("horario", horario)
            put("especialidades", especialidades)
            put("descripcion", descripcion)
            put("estado", "ACTIVO")
        }
        db.insert(TABLA_HOSPITALES, null, valores)
    }
}
