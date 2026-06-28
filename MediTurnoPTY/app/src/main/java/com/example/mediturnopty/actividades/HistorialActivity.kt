package com.example.mediturnopty.actividades

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mediturnopty.R
import com.example.mediturnopty.basedatos.CitasDAO
import com.example.mediturnopty.basedatos.MedicamentosDAO
import com.example.mediturnopty.utilidades.SesionUsuario

class HistorialActivity : AppCompatActivity() {

    private lateinit var contenedorHistorial: LinearLayout
    private lateinit var citasDAO: CitasDAO
    private lateinit var medicamentosDAO: MedicamentosDAO
    private lateinit var sesion: SesionUsuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        contenedorHistorial = findViewById(R.id.contenedorHistorial)
        citasDAO = CitasDAO(this)
        medicamentosDAO = MedicamentosDAO(this)
        sesion = SesionUsuario(this)

        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            finish()
        }

        mostrarHistorial()
    }

    private fun mostrarHistorial() {
        contenedorHistorial.removeAllViews()
        val idUsuario = sesion.obtenerIdUsuario()
        val citas = citasDAO.listarCitasPorUsuario(idUsuario)
        val medicamentos = medicamentosDAO.listarMedicamentosPorUsuario(idUsuario)

        contenedorHistorial.addView(crearTitulo("Citas medicas"))
        if (citas.isEmpty()) {
            contenedorHistorial.addView(crearTexto("No hay citas registradas."))
        } else {
            for (cita in citas) {
                contenedorHistorial.addView(
                    crearTexto("${cita.fecha} ${cita.hora} - ${cita.especialidad} - ${cita.nombreHospital}")
                )
            }
        }

        contenedorHistorial.addView(crearTitulo("Medicamentos"))
        if (medicamentos.isEmpty()) {
            contenedorHistorial.addView(crearTexto("No hay medicamentos registrados."))
        } else {
            for (medicamento in medicamentos) {
                contenedorHistorial.addView(
                    crearTexto("${medicamento.nombre} - ${medicamento.dosis} - ${medicamento.frecuencia} - ${medicamento.hora}")
                )
            }
        }
    }

    private fun crearTitulo(texto: String): TextView {
        return TextView(this).apply {
            this.text = texto
            textSize = 18f
            setPadding(0, 20, 0, 8)
        }
    }

    private fun crearTexto(texto: String): TextView {
        return TextView(this).apply {
            this.text = texto
            textSize = 15f
            setPadding(0, 8, 0, 8)
        }
    }
}
