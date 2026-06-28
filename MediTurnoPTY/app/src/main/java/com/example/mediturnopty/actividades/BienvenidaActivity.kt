package com.example.mediturnopty.actividades

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mediturnopty.R
import com.example.mediturnopty.utilidades.Constantes
import com.example.mediturnopty.utilidades.SesionUsuario

class BienvenidaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sesion = SesionUsuario(this)
        if (sesion.haySesionIniciada()) {
            abrirPanelSegunRol(sesion.obtenerRolUsuario())
            finish()
            return
        }

        setContentView(R.layout.activity_bienvenida)

        val btnIniciarSesion = findViewById<Button>(R.id.btnIniciarSesion)
        val btnRegistro = findViewById<Button>(R.id.btnRegistroCliente)

        btnIniciarSesion.setOnClickListener {
            startActivity(Intent(this, InicioSesionActivity::class.java))
        }

        btnRegistro.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }

    private fun abrirPanelSegunRol(rol: String) {
        if (rol == Constantes.ROL_ADMIN) {
            startActivity(Intent(this, PanelAdminActivity::class.java))
        } else {
            startActivity(Intent(this, PanelClienteActivity::class.java))
        }
    }
}
