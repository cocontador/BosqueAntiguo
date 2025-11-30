package com.example.bosqueantiguo

import android.app.Application
import com.example.bosqueantiguo.datastore.TokenManager
import com.example.bosqueantiguo.model.AppDatabase
import com.example.bosqueantiguo.network.RetrofitConfig
import com.example.bosqueantiguo.repository.UsuarioRepository

class BosqueAntiguoApp : Application() {

    // Instancia de la base de datos Room
    // CORREGIDO: Se añade el tipo explícito para ayudar al compilador
    private val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }

    // Instancia del repositorio de usuarios, ahora con su dependencia satisfecha
    val usuarioRepository: UsuarioRepository by lazy {
        UsuarioRepository(database.usuarioDao())
    }

    // Instancia del gestor de tokens
    lateinit var tokenManager: TokenManager
        private set

    override fun onCreate() {
        super.onCreate()
        // Inicializa el TokenManager
        tokenManager = TokenManager(this)
        // Inicializa RetrofitConfig
        RetrofitConfig.init(this)
    }
}
