package com.example.mediturnopty.actividades

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mediturnopty.R
import com.example.mediturnopty.basedatos.CitasDAO
import com.example.mediturnopty.basedatos.MedicamentosDAO
import com.example.mediturnopty.utilidades.SesionUsuario

class PanelClienteActivity : AppCompatActivity() {

    private lateinit var txtResumenCliente: TextView
    private lateinit var sesion: SesionUsuario
    private lateinit var citasDAO: CitasDAO
    private lateinit var medicamentosDAO: MedicamentosDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panel_cliente)

        sesion = SesionUsuario(this)
        citasDAO = CitasDAO(this)
        medicamentosDAO = MedicamentosDAO(this)

        txtResumenCliente = findViewById(R.id.txtResumenCliente)
        val btnCentros = findViewById<Button>(R.id.btnCentrosMedicos)
        val btnCitas = findViewById<Button>(R.id.btnCitas)
        val btnMedicamentos = findViewById<Button>(R.id.btnMedicamentos)
        val btnHistorial = findViewById<Button>(R.id.btnHistorial)
        val btnPerfil = findViewById<Button>(R.id.btnPerfil)
        val btnCerrar = findViewById<Button>(R.id.btnCerrarSesionCliente)

        btnCentros.setOnClickListener { startActivity(Intent(this, CentrosMedicosActivity::class.java)) }
        btnCitas.setOnClickListener { startActivity(Intent(this, CitasActivity::class.java)) }
        btnMedicamentos.setOnClickListener { startActivity(Intent(this, MedicamentosActivity::class.java)) }
        btnHistorial.setOnClickListener { startActivity(Intent(this, HistorialActivity::class.java)) }
        btnPerfil.setOnClickListener { startActivity(Intent(this, PerfilActivity::class.java)) }

        btnCerrar.setOnClickListener {
            sesion.cerrarSesion()
            startActivity(Intent(this, BienvenidaActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        val idUsuario = sesion.obtenerIdUsuario()
        val totalCitas = citasDAO.contarCitasPorUsuario(idUsuario)
        val totalMedicamentos = medicamentosDAO.contarMedicamentosPorUsuario(idUsuario)
        txtResumenCliente.text = "Bienvenido: ${sesion.obtenerNombreUsuario()}\nCitas: $totalCitas\nMedicamentos: $totalMedicamentos"
    }
}
