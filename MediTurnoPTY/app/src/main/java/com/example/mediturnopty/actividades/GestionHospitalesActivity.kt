package com.example.mediturnopty.actividades

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mediturnopty.R
import com.example.mediturnopty.basedatos.HospitalesDAO
import com.example.mediturnopty.modelos.Hospital
import com.example.mediturnopty.utilidades.Validaciones

class GestionHospitalesActivity : AppCompatActivity() {

    private lateinit var editNombre: EditText
    private lateinit var editTipo: EditText
    private lateinit var editDireccion: EditText
    private lateinit var editTelefono: EditText
    private lateinit var editHorario: EditText
    private lateinit var editEspecialidades: EditText
    private lateinit var editDescripcion: EditText
    private lateinit var btnGuardar: Button
    private lateinit var contenedorHospitales: LinearLayout

    private lateinit var hospitalesDAO: HospitalesDAO
    private var idHospitalEditando = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_hospitales)

        hospitalesDAO = HospitalesDAO(this)

        editNombre = findViewById(R.id.editNombreHospital)
        editTipo = findViewById(R.id.editTipoHospital)
        editDireccion = findViewById(R.id.editDireccionHospital)
        editTelefono = findViewById(R.id.editTelefonoHospital)
        editHorario = findViewById(R.id.editHorarioHospital)
        editEspecialidades = findViewById(R.id.editEspecialidadesHospital)
        editDescripcion = findViewById(R.id.editDescripcionHospital)
        btnGuardar = findViewById(R.id.btnGuardarHospital)
        contenedorHospitales = findViewById(R.id.contenedorHospitalesAdmin)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiarHospital)

        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            finish()
        }

        btnGuardar.setOnClickListener { guardarHospital() }
        btnLimpiar.setOnClickListener { limpiarFormulario() }

        listarHospitales()
    }

    private fun guardarHospital() {
        val nombre = editNombre.text.toString()
        val tipo = editTipo.text.toString()
        val direccion = editDireccion.text.toString()
        val telefono = editTelefono.text.toString()
        val horario = editHorario.text.toString()
        val especialidades = editEspecialidades.text.toString()
        val descripcion = editDescripcion.text.toString()

        if (!Validaciones.camposCompletos(nombre, tipo, direccion, telefono, horario, especialidades)) {
            Toast.makeText(this, getString(R.string.error_campos), Toast.LENGTH_SHORT).show()
            return
        }

        val hospital = Hospital(
            idHospital = idHospitalEditando,
            nombre = nombre,
            tipo = tipo,
            direccion = direccion,
            telefono = telefono,
            horario = horario,
            especialidades = especialidades,
            descripcion = descripcion
        )

        if (idHospitalEditando == 0) {
            val resultado = hospitalesDAO.registrarHospital(hospital)
            if (resultado > 0) Toast.makeText(this, "Hospital registrado", Toast.LENGTH_SHORT).show()
        } else {
            val resultado = hospitalesDAO.actualizarHospital(hospital)
            if (resultado > 0) Toast.makeText(this, "Hospital actualizado", Toast.LENGTH_SHORT).show()
        }

        limpiarFormulario()
        listarHospitales()
    }

    private fun listarHospitales() {
        contenedorHospitales.removeAllViews()
        val hospitales = hospitalesDAO.listarHospitalesActivos()

        if (hospitales.isEmpty()) {
            contenedorHospitales.addView(crearTexto("No hay hospitales registrados."))
            return
        }

        for (hospital in hospitales) {
            val texto = """
                ${hospital.nombre}
                Tipo: ${hospital.tipo}
                Telefono: ${hospital.telefono}
                Horario: ${hospital.horario}
                Especialidades: ${hospital.especialidades}
                Direccion: ${hospital.direccion}
            """.trimIndent()

            contenedorHospitales.addView(crearTexto(texto))

            val filaBotones = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
            }

            val btnEditar = Button(this).apply {
                text = "Editar"
                setOnClickListener { cargarHospitalEnFormulario(hospital) }
            }
            val btnEliminar = Button(this).apply {
                text = "Eliminar"
                setOnClickListener { confirmarEliminacion(hospital) }
            }

            filaBotones.addView(btnEditar)
            filaBotones.addView(btnEliminar)
            contenedorHospitales.addView(filaBotones)
        }
    }

    private fun cargarHospitalEnFormulario(hospital: Hospital) {
        idHospitalEditando = hospital.idHospital
        editNombre.setText(hospital.nombre)
        editTipo.setText(hospital.tipo)
        editDireccion.setText(hospital.direccion)
        editTelefono.setText(hospital.telefono)
        editHorario.setText(hospital.horario)
        editEspecialidades.setText(hospital.especialidades)
        editDescripcion.setText(hospital.descripcion)
        btnGuardar.text = "Actualizar hospital"
    }

    private fun confirmarEliminacion(hospital: Hospital) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar hospital")
            .setMessage("Desea eliminar ${hospital.nombre}?")
            .setPositiveButton("Si") { _, _ ->
                hospitalesDAO.eliminarHospital(hospital.idHospital)
                listarHospitales()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun limpiarFormulario() {
        idHospitalEditando = 0
        editNombre.text.clear()
        editTipo.text.clear()
        editDireccion.text.clear()
        editTelefono.text.clear()
        editHorario.text.clear()
        editEspecialidades.text.clear()
        editDescripcion.text.clear()
        btnGuardar.text = "Guardar hospital"
    }

    private fun crearTexto(texto: String): TextView {
        return TextView(this).apply {
            this.text = texto
            textSize = 15f
            setPadding(0, 12, 0, 12)
        }
    }
}
