package com.example.mediturnopty.actividades

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mediturnopty.R
import com.example.mediturnopty.basedatos.HospitalesDAO
import com.example.mediturnopty.modelos.Hospital

class CentrosMedicosActivity : AppCompatActivity() {

    private lateinit var editBuscar: EditText
    private lateinit var contenedorCentros: LinearLayout
    private lateinit var hospitalesDAO: HospitalesDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_centros_medicos)

        hospitalesDAO = HospitalesDAO(this)
        editBuscar = findViewById(R.id.editBuscarCentro)
        contenedorCentros = findViewById(R.id.contenedorCentrosMedicos)
        val btnBuscar = findViewById<Button>(R.id.btnBuscarCentro)

        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            finish()
        }

        btnBuscar.setOnClickListener {
            listarHospitales(editBuscar.text.toString())
        }

        listarHospitales("")
    }

    private fun listarHospitales(filtro: String) {
        contenedorCentros.removeAllViews()
        val hospitales = hospitalesDAO.buscarHospitales(filtro)

        if (hospitales.isEmpty()) {
            contenedorCentros.addView(crearTexto("No se encontraron hospitales."))
            return
        }

        for (hospital in hospitales) {
            val texto = """
                ${hospital.nombre}
                Tipo: ${hospital.tipo}
                Especialidades: ${hospital.especialidades}
                Telefono: ${hospital.telefono}
            """.trimIndent()

            val vista = crearTexto(texto)
            vista.setOnClickListener { mostrarDetalle(hospital) }
            contenedorCentros.addView(vista)
        }
    }

    private fun mostrarDetalle(hospital: Hospital) {
        val mensaje = """
            Tipo: ${hospital.tipo}
            Direccion: ${hospital.direccion}
            Telefono: ${hospital.telefono}
            Horario: ${hospital.horario}
            Especialidades: ${hospital.especialidades}

            ${hospital.descripcion}
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle(hospital.nombre)
            .setMessage(mensaje)
            .setPositiveButton("Aceptar", null)
            .show()
    }

    private fun crearTexto(texto: String): TextView {
        return TextView(this).apply {
            this.text = texto
            textSize = 15f
            setPadding(0, 12, 0, 12)
        }
    }
}
