package com.example.bosqueantiguo.model

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Base de datos principal de la aplicación.
 * Incluye las entidades y DAOs registrados.
 */
@Database(
    entities = [Usuario::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
}