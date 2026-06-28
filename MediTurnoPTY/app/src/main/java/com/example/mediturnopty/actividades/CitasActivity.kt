package com.example.mediturnopty.actividades

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mediturnopty.R
import com.example.mediturnopty.basedatos.CitasDAO
import com.example.mediturnopty.basedatos.HospitalesDAO
import com.example.mediturnopty.modelos.Cita
import com.example.mediturnopty.modelos.Hospital
import com.example.mediturnopty.utilidades.SesionUsuario
import com.example.mediturnopty.utilidades.Validaciones

class CitasActivity : AppCompatActivity() {

    private lateinit var spinnerHospital: Spinner
    private lateinit var editEspecialidad: EditText
    private lateinit var editDoctor: EditText
    private lateinit var editFecha: EditText
    private lateinit var editHora: EditText
    private lateinit var editNotas: EditText
    private lateinit var btnGuardar: Button
    private lateinit var contenedorCitas: LinearLayout

    private lateinit var citasDAO: CitasDAO
    private lateinit var hospitalesDAO: HospitalesDAO
    private lateinit var sesion: SesionUsuario
    private var hospitales = ArrayList<Hospital>()
    private var idCitaEditando = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_citas)

        citasDAO = CitasDAO(this)
        hospitalesDAO = HospitalesDAO(this)
        sesion = SesionUsuario(this)

        spinnerHospital = findViewById(R.id.spinnerHospitalCita)
        editEspecialidad = findViewById(R.id.editEspecialidadCita)
        editDoctor = findViewById(R.id.editDoctorCita)
        editFecha = findViewById(R.id.editFechaCita)
        editHora = findViewById(R.id.editHoraCita)
        editNotas = findViewById(R.id.editNotasCita)
        btnGuardar = findViewById(R.id.btnGuardarCita)
        contenedorCitas = findViewById(R.id.contenedorCitas)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiarCita)

        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            finish()
        }

        cargarHospitales()
        btnGuardar.setOnClickListener { guardarCita() }
        btnLimpiar.setOnClickListener { limpiarFormulario() }
        listarCitas()
    }

    private fun cargarHospitales() {
        hospitales = hospitalesDAO.listarHospitalesActivos()
        val nombres = hospitales.map { it.nombre }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombres)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerHospital.adapter = adapter
    }

    private fun guardarCita() {
        if (hospitales.isEmpty()) {
            Toast.makeText(this, "No hay hospitales disponibles", Toast.LENGTH_SHORT).show()
            return
        }

        val especialidad = editEspecialidad.text.toString()
        val doctor = editDoctor.text.toString()
        val fecha = editFecha.text.toString()
        val hora = editHora.text.toString()
        val notas = editNotas.text.toString()

        if (!Validaciones.camposCompletos(especialidad, fecha, hora)) {
            Toast.makeText(this, getString(R.string.error_campos), Toast.LENGTH_SHORT).show()
            return
        }

        val hospital = hospitales[spinnerHospital.selectedItemPosition]
        val cita = Cita(
            idCita = idCitaEditando,
            idUsuario = sesion.obtenerIdUsuario(),
            idHospital = hospital.idHospital,
            especialidad = especialidad,
            doctor = doctor,
            fecha = fecha,
            hora = hora,
            notas = notas
        )

        if (idCitaEditando == 0) {
            citasDAO.registrarCita(cita)
            Toast.makeText(this, "Cita guardada", Toast.LENGTH_SHORT).show()
        } else {
            citasDAO.actualizarCita(cita)
            Toast.makeText(this, "Cita actualizada", Toast.LENGTH_SHORT).show()
        }

        limpiarFormulario()
        listarCitas()
    }

    private fun listarCitas() {
        contenedorCitas.removeAllViews()
        val citas = citasDAO.listarCitasPorUsuario(sesion.obtenerIdUsuario())

        if (citas.isEmpty()) {
            contenedorCitas.addView(crearTexto("No hay citas registradas."))
            return
        }

        for (cita in citas) {
            val texto = """
                ${cita.fecha} ${cita.hora}
                Hospital: ${cita.nombreHospital}
                Especialidad: ${cita.especialidad}
                Doctor: ${cita.doctor}
                Notas: ${cita.notas}
            """.trimIndent()

            contenedorCitas.addView(crearTexto(texto))

            val filaBotones = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
            }
            val btnEditar = Button(this).apply {
                text = "Editar"
                setOnClickListener { cargarCitaEnFormulario(cita) }
            }
            val btnEliminar = Button(this).apply {
                text = "Eliminar"
                setOnClickListener { confirmarEliminacion(cita) }
            }

            filaBotones.addView(btnEditar)
            filaBotones.addView(btnEliminar)
            contenedorCitas.addView(filaBotones)
        }
    }

    private fun cargarCitaEnFormulario(cita: Cita) {
        idCitaEditando = cita.idCita
        val posicion = hospitales.indexOfFirst { it.idHospital == cita.idHospital }
        if (posicion >= 0) spinnerHospital.setSelection(posicion)
        editEspecialidad.setText(cita.especialidad)
        editDoctor.setText(cita.doctor)
        editFecha.setText(cita.fecha)
        editHora.setText(cita.hora)
        editNotas.setText(cita.notas)
        btnGuardar.text = "Actualizar cita"
    }

    private fun confirmarEliminacion(cita: Cita) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar cita")
            .setMessage("Desea eliminar esta cita?")
            .setPositiveButton("Si") { _, _ ->
                citasDAO.eliminarCita(cita.idCita, sesion.obtenerIdUsuario())
                listarCitas()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun limpiarFormulario() {
        idCitaEditando = 0
        editEspecialidad.text.clear()
        editDoctor.text.clear()
        editFecha.text.clear()
        editHora.text.clear()
        editNotas.text.clear()
        btnGuardar.text = "Guardar cita"
    }

    private fun crearTexto(texto: String): TextView {
        return TextView(this).apply {
            this.text = texto
            textSize = 15f
            setPadding(0, 12, 0, 12)
        }
    }
}
