package com.example.bosqueantiguo.viewmodel

/**
 * Representa los errores de validación del formulario.
 * Cada campo puede tener un mensaje o estar vacío si no hay error.
 */
data class UsuarioErrores(
    val nombreError: String? = null,
    val correoError: String? = null,
    val edadError: String? = null,
    val contrasenaError: String? = null
)
