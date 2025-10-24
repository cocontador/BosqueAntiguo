package com.example.bosqueantiguo.viewmodel

/**
 * Estado de la interfaz para el formulario de usuario.
 * Contiene los valores actuales y posibles mensajes de error.
 */
data class UsuarioUIState(
    val nombre: String = "",
    val correo: String = "",
    val edad: String = "",
    val contrasena: String = "",
    val errores: UsuarioErrores = UsuarioErrores(),
    val guardadoExitoso: Boolean = false
)
