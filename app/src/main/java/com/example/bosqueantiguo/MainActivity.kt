package com.example.bosqueantiguo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bosqueantiguo.ui.theme.BosqueAntiguoTheme
import com.example.bosqueantiguo.ui.view.AjustesScreen
import com.example.bosqueantiguo.ui.view.FormularioScreen
import com.example.bosqueantiguo.ui.view.MainScreen
import com.example.bosqueantiguo.ui.view.PerfilScreen
import com.example.bosqueantiguo.ui.view.ResumenScreen
import com.example.bosqueantiguo.viewmodel.UsuarioViewModel
import com.example.bosqueantiguo.viewmodel.UsuarioViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instala la pantalla de inicio
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

                    // Estructura de navegación con animaciones
                    NavHost(navController = navController, startDestination = "main") {
                        composable("main") {
                            MainScreen(
                                onNavigateToRegistro = { navController.navigate("formulario") },
                                onNavigateToPerfil = { navController.navigate("resumen") },
                                onNavigateToAjustes = { navController.navigate("ajustes") }
                            )
                        }
                        composable(
                            "formulario",
                            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
                            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
                            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
                        ) {
                            FormularioScreen(
                                viewModel = viewModel,
                                onGuardado = { navController.navigate("resumen") },
                                onNavigateBack = { navController.navigateUp() }
                            )
                        }
                        composable(
                            "resumen",
                            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
                            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
                            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
                        ) {
                            ResumenScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.navigateUp() },
                                onUsuarioClick = { userId -> navController.navigate("perfil/$userId") },
                                onAddUsuario = { navController.navigate("formulario") }
                            )
                        }
                        composable(
                            "perfil/{userId}",
                            arguments = listOf(navArgument("userId") { type = NavType.IntType }),
                            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
                            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
                            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
                        ) { backStackEntry ->
                            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
                            PerfilScreen(
                                userId = userId,
                                viewModel = viewModel,
                                onNavigateBack = { navController.navigateUp() }
                            )
                        }
                        composable(
                            "ajustes",
                            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
                            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
                            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
                        ) {
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
