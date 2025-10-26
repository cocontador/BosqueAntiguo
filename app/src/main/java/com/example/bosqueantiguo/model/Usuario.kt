package com.example.bosqueantiguo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa un usuario registrado en la aplicación.
 * Se almacena localmente mediante Room.
 */
@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nombre: String,
    val correo: String,
    val edad: Int,
    val contrasena: String,
    val imagenUri: String? = null

)