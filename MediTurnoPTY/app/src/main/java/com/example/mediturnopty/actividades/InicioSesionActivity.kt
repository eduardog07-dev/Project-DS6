package com.example.mediturnopty.actividades

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mediturnopty.R
import com.example.mediturnopty.basedatos.UsuariosDAO
import com.example.mediturnopty.utilidades.Constantes
import com.example.mediturnopty.utilidades.SesionUsuario
import com.example.mediturnopty.utilidades.Validaciones

class InicioSesionActivity : AppCompatActivity() {

    private lateinit var txtTitulo: TextView
    private lateinit var editCorreo: EditText
    private lateinit var editContrasena: EditText
    private lateinit var btnIngresar: Button
    private lateinit var btnIrRegistro: Button

    private lateinit var usuariosDAO: UsuariosDAO
    private lateinit var sesion: SesionUsuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion)

        usuariosDAO = UsuariosDAO(this)
        sesion = SesionUsuario(this)

        txtTitulo = findViewById(R.id.txtTituloIngreso)
        editCorreo = findViewById(R.id.editCorreoIngreso)
        editContrasena = findViewById(R.id.editContrasenaIngreso)
        btnIngresar = findViewById(R.id.btnIngresar)
        btnIrRegistro = findViewById(R.id.btnIrRegistro)

        txtTitulo.text = "Inicio de sesion"

        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            finish()
        }

        btnIngresar.setOnClickListener {
            ingresar()
        }

        btnIrRegistro.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }

    private fun ingresar() {
        val correo = editCorreo.text.toString()
        val contrasena = editContrasena.text.toString()

        if (!Validaciones.camposCompletos(correo, contrasena)) {
            Toast.makeText(this, getString(R.string.error_campos), Toast.LENGTH_SHORT).show()
            return
        }

        val usuario = usuariosDAO.validarIngreso(correo, contrasena)
        if (usuario == null) {
            Toast.makeText(this, "Correo o contrasena incorrectos", Toast.LENGTH_LONG).show()
            return
        }

        sesion.guardarSesion(usuario)
        if (usuario.rol == Constantes.ROL_ADMIN) {
            startActivity(Intent(this, PanelAdminActivity::class.java))
        } else {
            startActivity(Intent(this, PanelClienteActivity::class.java))
        }
        finish()
    }
}
