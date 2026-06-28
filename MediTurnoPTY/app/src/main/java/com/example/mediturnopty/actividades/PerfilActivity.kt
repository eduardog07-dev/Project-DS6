package com.example.mediturnopty.actividades

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mediturnopty.R
import com.example.mediturnopty.basedatos.UsuariosDAO
import com.example.mediturnopty.modelos.Usuario
import com.example.mediturnopty.utilidades.SesionUsuario
import com.example.mediturnopty.utilidades.Validaciones

class PerfilActivity : AppCompatActivity() {

    private lateinit var txtCorreoRol: TextView
    private lateinit var editNombre: EditText
    private lateinit var editTelefono: EditText
    private lateinit var switchModoOscuro: Switch
    private lateinit var usuariosDAO: UsuariosDAO
    private lateinit var sesion: SesionUsuario
    private var usuarioActual: Usuario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        usuariosDAO = UsuariosDAO(this)
        sesion = SesionUsuario(this)

        txtCorreoRol = findViewById(R.id.txtCorreoRolPerfil)
        editNombre = findViewById(R.id.editNombrePerfil)
        editTelefono = findViewById(R.id.editTelefonoPerfil)
        switchModoOscuro = findViewById(R.id.switchModoOscuro)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarPerfil)

        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            finish()
        }

        cargarPerfil()

        btnGuardar.setOnClickListener {
            guardarPerfil()
        }

        switchModoOscuro.setOnCheckedChangeListener { _, isChecked ->
            sesion.guardarModoOscuro(isChecked)
        }
    }

    private fun cargarPerfil() {
        usuarioActual = usuariosDAO.obtenerUsuarioPorId(sesion.obtenerIdUsuario())
        val usuario = usuarioActual
        if (usuario == null) {
            Toast.makeText(this, "No se pudo cargar el perfil", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        editNombre.setText(usuario.nombre)
        editTelefono.setText(usuario.telefono)
        txtCorreoRol.text = "Correo: ${usuario.correo}\nRol: ${usuario.rol}"
        switchModoOscuro.isChecked = sesion.obtenerModoOscuro()
    }

    private fun guardarPerfil() {
        val usuario = usuarioActual ?: return
        val nombre = editNombre.text.toString()
        val telefono = editTelefono.text.toString()

        if (!Validaciones.camposCompletos(nombre, telefono)) {
            Toast.makeText(this, getString(R.string.error_campos), Toast.LENGTH_SHORT).show()
            return
        }

        val filas = usuariosDAO.actualizarPerfil(usuario.idUsuario, nombre, telefono)
        if (filas > 0) {
            val usuarioActualizado = usuario.copy(nombre = nombre, telefono = telefono)
            sesion.guardarSesion(usuarioActualizado)
            usuarioActual = usuarioActualizado
            Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No se pudo actualizar el perfil", Toast.LENGTH_SHORT).show()
        }
    }
}
