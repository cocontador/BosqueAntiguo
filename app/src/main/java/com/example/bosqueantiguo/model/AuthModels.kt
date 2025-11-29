package com.example.bosqueantiguo.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo para enviar las credenciales en la petición de login.
 */
data class LoginRequest(
    val email: String,
    val password: String
)

/**
 * Modelo para recibir la respuesta del login, que usualmente contiene un token.
 */
data class LoginResponse(
    @SerializedName("token") // Asumiendo que el backend devuelve un campo "token"
    val token: String,
    @SerializedName("usuario") // Y también la información del usuario
    val usuario: UsuarioApi
)

/**
 * Representa los datos de un usuario recibidos desde la API.
 * Es similar a tu entidad de Spring, pero adaptado para el cliente Android.
 */
data class UsuarioApi(
    val id: Long,
    val email: String,
    val nombre: String?,
    val apellido: String?,
    val direccion: String?,
    val rut: String?,
    val roles: List<String> = emptyList() // Asumiendo que los roles se devuelven como una lista de strings
)
