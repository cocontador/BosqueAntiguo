package com.example.bosqueantiguo

import android.app.Application
import androidx.room.Room
import com.example.bosqueantiguo.model.AppDatabase
import com.example.bosqueantiguo.repository.UsuarioRepository
import com.example.bosqueantiguo.repository.ProductoRepository

/**
 * Clase Application para centralizar la creación de instancias (Singleton).
 * Aquí se inicializa la base de datos y los repositorios para que estén
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

    // Repositorio para usuarios (Room local)
    val usuarioRepository by lazy {
        UsuarioRepository(database.usuarioDao())
    }
    
    // Repositorio para productos (API remota)
    val productoRepository by lazy {
        ProductoRepository()
    }
}
