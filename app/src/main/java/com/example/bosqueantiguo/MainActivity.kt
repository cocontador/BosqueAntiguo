package com.example.bosqueantiguo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bosqueantiguo.ui.theme.BosqueAntiguoTheme
import com.example.bosqueantiguo.ui.view.*
import com.example.bosqueantiguo.viewmodel.UsuarioViewModel
import com.example.bosqueantiguo.viewmodel.UsuarioViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instala la pantalla de inicio (SplashScreen)
        installSplashScreen()

        // Obtiene el repositorio desde la clase Application
        val repository = (application as BosqueAntiguoApp).repository

        setContent {
            BosqueAntiguoTheme {
                Surface(color = MaterialTheme.colorScheme.background) {

                    // Controlador de navegación
                    val navController = rememberNavController()

                    // Factory para inyectar el repositorio al ViewModel
                    val factory = UsuarioViewModelFactory(repository)
                    val viewModel: UsuarioViewModel = viewModel(factory = factory)

                    // Estructura de navegación
                    NavHost(navController = navController, startDestination = "main") {

                        // 🔹 Pantalla principal
                        composable("main") {
                            MainScreen(
                                onNavigateToRegistro = { navController.navigate("formulario") },
                                onNavigateToPerfil = { navController.navigate("perfil") }, // Perfil genérico
                                onNavigateToAjustes = { navController.navigate("ajustes") },
                                onNavigateToProducto = { navController.navigate("producto") },
                                onNavigateToResumen = { navController.navigate("resumen") }
                            )
                        }

                        // 🔹 Registro de usuario
                        composable("formulario") {
                            FormularioScreen(
                                viewModel = viewModel,
                                onGuardado = {
                                    // Navega al resumen y limpia la pila para evitar volver al formulario
                                    navController.navigate("resumen") {
                                        popUpTo("main") { inclusive = false }
                                        launchSingleTop = true
                                    }
                                },
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        // 🔹 Resumen de usuarios
                        composable("resumen") {
                            ResumenScreen(
                                viewModel = viewModel,
                                navController = navController,
                                onNavigateBack = { navController.navigateUp() }
                            )
                        }

                        // 🔹 Perfil genérico (desde menú principal)
                        composable("perfil") {
                            PerfilScreen(
                                usuarioId = null,
                                viewModel = viewModel,
                                onNavigateBack = { navController.navigateUp() }
                            )
                        }

                        // 🔹 Perfil de usuario específico (desde resumen)
                        composable("perfil/{usuarioId}") { backStackEntry ->
                            val usuarioId = backStackEntry.arguments?.getString("usuarioId")
                            PerfilScreen(
                                usuarioId = usuarioId,
                                viewModel = viewModel,
                                onNavigateBack = { navController.navigateUp() }
                            )
                        }

                        // 🔹 Catálogo de productos
                        composable("producto") {
                            ProductoScreen(onNavigateBack = { navController.navigateUp() })
                        }

                        // 🔹 Ajustes
                        composable("ajustes") {
                            AjustesScreen(
                                onNavigateBack = { navController.navigateUp() },
                                onExitApp = { finish() }
                            )
                        }
                    }
                }
            }
        }
    }
}
