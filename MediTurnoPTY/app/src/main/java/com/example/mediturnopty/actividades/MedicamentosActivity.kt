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
import com.example.mediturnopty.basedatos.MedicamentosDAO
import com.example.mediturnopty.modelos.Medicamento
import com.example.mediturnopty.utilidades.SesionUsuario
import com.example.mediturnopty.utilidades.Validaciones

class MedicamentosActivity : AppCompatActivity() {

    private lateinit var editNombre: EditText
    private lateinit var editDosis: EditText
    private lateinit var editFrecuencia: EditText
    private lateinit var editHora: EditText
    private lateinit var editObservaciones: EditText
    private lateinit var btnGuardar: Button
    private lateinit var contenedorMedicamentos: LinearLayout

    private lateinit var medicamentosDAO: MedicamentosDAO
    private lateinit var sesion: SesionUsuario
    private var idMedicamentoEditando = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicamentos)

        medicamentosDAO = MedicamentosDAO(this)
        sesion = SesionUsuario(this)

        editNombre = findViewById(R.id.editNombreMedicamento)
        editDosis = findViewById(R.id.editDosisMedicamento)
        editFrecuencia = findViewById(R.id.editFrecuenciaMedicamento)
        editHora = findViewById(R.id.editHoraMedicamento)
        editObservaciones = findViewById(R.id.editObservacionesMedicamento)
        btnGuardar = findViewById(R.id.btnGuardarMedicamento)
        contenedorMedicamentos = findViewById(R.id.contenedorMedicamentos)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiarMedicamento)

        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            finish()
        }

        btnGuardar.setOnClickListener { guardarMedicamento() }
        btnLimpiar.setOnClickListener { limpiarFormulario() }
        listarMedicamentos()
    }

    private fun guardarMedicamento() {
        val nombre = editNombre.text.toString()
        val dosis = editDosis.text.toString()
        val frecuencia = editFrecuencia.text.toString()
        val hora = editHora.text.toString()
        val observaciones = editObservaciones.text.toString()

        if (!Validaciones.camposCompletos(nombre, dosis, frecuencia, hora)) {
            Toast.makeText(this, getString(R.string.error_campos), Toast.LENGTH_SHORT).show()
            return
        }

        val medicamento = Medicamento(
            idMedicamento = idMedicamentoEditando,
            idUsuario = sesion.obtenerIdUsuario(),
            nombre = nombre,
            dosis = dosis,
            frecuencia = frecuencia,
            hora = hora,
            observaciones = observaciones
        )

        if (idMedicamentoEditando == 0) {
            medicamentosDAO.registrarMedicamento(medicamento)
            Toast.makeText(this, "Medicamento guardado", Toast.LENGTH_SHORT).show()
        } else {
            medicamentosDAO.actualizarMedicamento(medicamento)
            Toast.makeText(this, "Medicamento actualizado", Toast.LENGTH_SHORT).show()
        }

        limpiarFormulario()
        listarMedicamentos()
    }

    private fun listarMedicamentos() {
        contenedorMedicamentos.removeAllViews()
        val medicamentos = medicamentosDAO.listarMedicamentosPorUsuario(sesion.obtenerIdUsuario())

        if (medicamentos.isEmpty()) {
            contenedorMedicamentos.addView(crearTexto("No hay medicamentos registrados."))
            return
        }

        for (medicamento in medicamentos) {
            val texto = """
                ${medicamento.nombre}
                Dosis: ${medicamento.dosis}
                Frecuencia: ${medicamento.frecuencia}
                Hora: ${medicamento.hora}
                Observaciones: ${medicamento.observaciones}
            """.trimIndent()

            contenedorMedicamentos.addView(crearTexto(texto))

            val filaBotones = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
            }
            val btnEditar = Button(this).apply {
                text = "Editar"
                setOnClickListener { cargarMedicamentoEnFormulario(medicamento) }
            }
            val btnEliminar = Button(this).apply {
                text = "Eliminar"
                setOnClickListener { confirmarEliminacion(medicamento) }
            }

            filaBotones.addView(btnEditar)
            filaBotones.addView(btnEliminar)
            contenedorMedicamentos.addView(filaBotones)
        }
    }

    private fun cargarMedicamentoEnFormulario(medicamento: Medicamento) {
        idMedicamentoEditando = medicamento.idMedicamento
        editNombre.setText(medicamento.nombre)
        editDosis.setText(medicamento.dosis)
        editFrecuencia.setText(medicamento.frecuencia)
        editHora.setText(medicamento.hora)
        editObservaciones.setText(medicamento.observaciones)
        btnGuardar.text = "Actualizar medicamento"
    }

    private fun confirmarEliminacion(medicamento: Medicamento) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar medicamento")
            .setMessage("Desea eliminar ${medicamento.nombre}?")
            .setPositiveButton("Si") { _, _ ->
                medicamentosDAO.eliminarMedicamento(medicamento.idMedicamento, sesion.obtenerIdUsuario())
                listarMedicamentos()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun limpiarFormulario() {
        idMedicamentoEditando = 0
        editNombre.text.clear()
        editDosis.text.clear()
        editFrecuencia.text.clear()
        editHora.text.clear()
        editObservaciones.text.clear()
        btnGuardar.text = "Guardar medicamento"
    }

    private fun crearTexto(texto: String): TextView {
        return TextView(this).apply {
            this.text = texto
            textSize = 15f
            setPadding(0, 12, 0, 12)
        }
    }
}
