package com.example.bosqueantiguo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.bosqueantiguo.model.AppDatabase
import com.example.bosqueantiguo.repository.UsuarioRepository
import com.example.bosqueantiguo.ui.theme.BosqueAntiguoTheme
import com.example.bosqueantiguo.ui.view.FormularioScreen
import com.example.bosqueantiguo.ui.view.ResumenScreen
import com.example.bosqueantiguo.viewmodel.UsuarioViewModel
import com.example.bosqueantiguo.viewmodel.UsuarioViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instanciamos la base de datos y el repositorio de Room
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "bosqueantiguo_db"
        ).build()

        val repository = UsuarioRepository(database.usuarioDao())

        setContent {
            BosqueAntiguoTheme {
                Surface(color = MaterialTheme.colorScheme.background) {

                    // Controlador de navegación
                    val navController = rememberNavController()

                    // Factory para inyectar el repositorio al ViewModel
                    val factory = UsuarioViewModelFactory(repository)
                    val viewModel: UsuarioViewModel = viewModel(factory = factory)

                    // Estructura de navegación
                    NavHost(navController = navController, startDestination = "formulario") {
                        composable("formulario") {
                            FormularioScreen(
                                viewModel = viewModel,
                                onGuardado = {
                                    navController.navigate("resumen")
                                }
                            )
                        }
                        composable("resumen") {
                            ResumenScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}
