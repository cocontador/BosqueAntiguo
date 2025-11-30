package com.example.bosqueantiguo.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    companion object {
        // La anotación @Volatile asegura que la instancia sea siempre la más actualizada
        // entre diferentes hilos, lo cual es crucial para la concurrencia.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Si la instancia ya existe, la retornamos. Si no, la creamos de forma segura.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bosque_antiguo_database"
                )
                .fallbackToDestructiveMigration() // Si se cambia la versión, destruye y recrea la BD (útil en desarrollo)
                .build()
                INSTANCE = instance
                // Retornamos la instancia recién creada
                instance
            }
        }
    }
}