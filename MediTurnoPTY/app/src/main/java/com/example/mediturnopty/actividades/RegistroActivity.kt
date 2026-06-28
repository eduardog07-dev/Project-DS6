package com.example.mediturnopty.actividades

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mediturnopty.R
import com.example.mediturnopty.basedatos.UsuariosDAO
import com.example.mediturnopty.modelos.Usuario
import com.example.mediturnopty.utilidades.Constantes
import com.example.mediturnopty.utilidades.Validaciones

class RegistroActivity : AppCompatActivity() {

    private lateinit var editNombre: EditText
    private lateinit var editCorreo: EditText
    private lateinit var editTelefono: EditText
    private lateinit var editContrasena: EditText
    private lateinit var editConfirmar: EditText
    private lateinit var btnRegistrar: Button

    private lateinit var usuariosDAO: UsuariosDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        usuariosDAO = UsuariosDAO(this)

        editNombre = findViewById(R.id.editNombreRegistro)
        editCorreo = findViewById(R.id.editCorreoRegistro)
        editTelefono = findViewById(R.id.editTelefonoRegistro)
        editContrasena = findViewById(R.id.editContrasenaRegistro)
        editConfirmar = findViewById(R.id.editConfirmarRegistro)
        btnRegistrar = findViewById(R.id.btnRegistrarCliente)

        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            finish()
        }

        btnRegistrar.setOnClickListener {
            registrarCliente()
        }
    }

    private fun registrarCliente() {
        val nombre = editNombre.text.toString()
        val correo = editCorreo.text.toString()
        val telefono = editTelefono.text.toString()
        val contrasena = editContrasena.text.toString()
        val confirmar = editConfirmar.text.toString()

        if (!Validaciones.camposCompletos(nombre, correo, telefono, contrasena, confirmar)) {
            Toast.makeText(this, getString(R.string.error_campos), Toast.LENGTH_SHORT).show()
            return
        }

        if (!Validaciones.correoValido(correo)) {
            Toast.makeText(this, "Ingrese un correo valido", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Validaciones.contrasenaValida(contrasena)) {
            Toast.makeText(this, "La contrasena debe tener minimo 4 caracteres", Toast.LENGTH_SHORT).show()
            return
        }

        if (contrasena != confirmar) {
            Toast.makeText(this, "Las contrasenas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        if (usuariosDAO.existeCorreo(correo)) {
            Toast.makeText(this, "Este correo ya esta registrado", Toast.LENGTH_SHORT).show()
            return
        }

        val usuario = Usuario(
            nombre = nombre,
            correo = correo,
            telefono = telefono,
            contrasena = contrasena,
            rol = Constantes.ROL_CLIENTE
        )

        val resultado = usuariosDAO.registrarUsuario(usuario)
        if (resultado > 0) {
            Toast.makeText(this, "Cliente registrado correctamente", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, InicioSesionActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "No se pudo registrar el cliente", Toast.LENGTH_SHORT).show()
        }
    }
}
