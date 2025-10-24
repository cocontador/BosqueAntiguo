package com.example.bosqueantiguo

import android.app.Application
import androidx.room.Room
import com.example.bosqueantiguo.model.AppDatabase
import com.example.bosqueantiguo.repository.UsuarioRepository

/**
 * Clase Application para centralizar la creación de instancias (Singleton).
 * Aquí se inicializa la base de datos y el repositorio para que estén
 * disponibles durante todo el ciclo de vida de la aplicación.
 */
class BosqueAntiguoApp : Application() {

    // Instancia única de la base de datos
    private val database by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "bosqueantiguo_db"
        ).fallbackToDestructiveMigration().build()
    }

    // Instancia única del repositorio
    val repository by lazy {
        UsuarioRepository(database.usuarioDao())
    }
}
