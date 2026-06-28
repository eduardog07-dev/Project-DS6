package com.example.mediturnopty.actividades

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mediturnopty.R
import com.example.mediturnopty.basedatos.HospitalesDAO
import com.example.mediturnopty.basedatos.UsuariosDAO
import com.example.mediturnopty.utilidades.SesionUsuario

class PanelAdminActivity : AppCompatActivity() {

    private lateinit var txtResumenAdmin: TextView
    private lateinit var hospitalesDAO: HospitalesDAO
    private lateinit var usuariosDAO: UsuariosDAO
    private lateinit var sesion: SesionUsuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panel_admin)

        hospitalesDAO = HospitalesDAO(this)
        usuariosDAO = UsuariosDAO(this)
        sesion = SesionUsuario(this)

        txtResumenAdmin = findViewById(R.id.txtResumenAdmin)
        val btnHospitales = findViewById<Button>(R.id.btnGestionHospitales)
        val btnCerrar = findViewById<Button>(R.id.btnCerrarSesionAdmin)

        btnHospitales.setOnClickListener {
            startActivity(Intent(this, GestionHospitalesActivity::class.java))
        }

        btnCerrar.setOnClickListener {
            sesion.cerrarSesion()
            startActivity(Intent(this, BienvenidaActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        val totalHospitales = hospitalesDAO.contarHospitalesActivos()
        val totalClientes = usuariosDAO.contarClientes()
        txtResumenAdmin.text = "Hospitales activos: $totalHospitales\nClientes registrados: $totalClientes"
    }
}
