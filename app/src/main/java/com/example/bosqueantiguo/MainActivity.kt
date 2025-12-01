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
import com.example.bosqueantiguo.repository.ProductoRepository
import com.example.bosqueantiguo.ui.theme.BosqueAntiguoTheme
import com.example.bosqueantiguo.ui.view.*
import com.example.bosqueantiguo.ui.viewmodel.*
import com.example.bosqueantiguo.viewmodel.UsuarioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            BosqueAntiguoTheme {
                Surface(color = MaterialTheme.colorScheme.background) {

                    val navController = rememberNavController()
                    
                    // --- Inyección de Dependencias Manual ---
                    val app = application as BosqueAntiguoApp
                    val productoRepository = ProductoRepository()

                    val usuarioViewModel: UsuarioViewModel = viewModel(factory = UsuarioViewModelFactory(app.usuarioRepository))
                    val productoViewModel: ProductoViewModel = viewModel(factory = ProductoViewModelFactory(productoRepository))
                    val categoriaViewModel: CategoriaViewModel = viewModel()
                    val carritoViewModel: CarritoViewModel = viewModel()
                    val authViewModel: AuthViewModel = viewModel()

                    NavHost(navController = navController, startDestination = "main") {

                        composable("login") {
                            LoginScreen(
                                authViewModel = authViewModel,
                                onLoginSuccess = {
                                    navController.navigate("main") { popUpTo(navController.graph.startDestinationId) { inclusive = true }; launchSingleTop = true }
                                },
                                onNavigateToRegistro = { navController.navigate("formulario") },
                                onNavigateToRecuperar = { navController.navigate("recuperar") }
                            )
                        }
                        
                        composable("recuperar") {
                            RecuperarScreen(onNavigateBack = { navController.popBackStack() })
                        }

                        composable("main") {
                            MainScreen(
                                onNavigateToRegistro = { navController.navigate("formulario") },
                                onNavigateToPerfil = { navController.navigate("perfil") },
                                onNavigateToAjustes = { navController.navigate("ajustes") },
                                onNavigateToProducto = { navController.navigate("categorias") },
                                onNavigateToResumen = { navController.navigate("resumen") },
                                onNavigateToClima = { navController.navigate("clima") },
                                onNavigateToCarrito = { navController.navigate("carrito") },
                                onNavigateToLogin = { navController.navigate("login") },
                                carritoViewModel = carritoViewModel
                            )
                        }

                        composable("categorias") {
                            CategoriaScreen(
                                onNavigateBack = { navController.navigateUp() },
                                onCategoriaClick = { navController.navigate("producto") },
                                categoriaViewModel = categoriaViewModel
                            )
                        }

                        composable("producto") {
                            ProductoScreen(
                                onNavigateBack = { navController.navigateUp() },
                                productoViewModel = productoViewModel,
                                carritoViewModel = carritoViewModel
                            )
                        }

                        composable("formulario") {
                            FormularioScreen(
                                viewModel = usuarioViewModel,
                                onGuardado = { navController.navigate("resumen") { popUpTo("main") { inclusive = false }; launchSingleTop = true } },
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable("resumen") {
                            ResumenScreen(
                                viewModel = usuarioViewModel,
                                navController = navController,
                                onNavigateBack = { navController.navigateUp() }
                            )
                        }

                        composable("perfil") {
                            PerfilScreen(
                                usuarioId = null,
                                viewModel = usuarioViewModel,
                                onNavigateBack = { navController.navigateUp() }
                            )
                        }

                        composable("perfil/{usuarioId}") { backStackEntry ->
                            val usuarioId = backStackEntry.arguments?.getString("usuarioId")
                            PerfilScreen(
                                usuarioId = usuarioId,
                                viewModel = usuarioViewModel,
                                onNavigateBack = { navController.navigateUp() }
                            )
                        }

                        composable("carrito") {
                            CarritoScreen(
                                carritoViewModel = carritoViewModel, 
                                onNavigateBack = { navController.navigateUp() },
                                onNavigateToConfirmacion = { navController.navigate("confirmacion") }
                            )
                        }

                        composable("confirmacion") {
                            VentaConfirmacionScreen(
                                carritoViewModel = carritoViewModel,
                                onVolverATienda = {
                                    navController.navigate("main") {
                                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("clima") { ClimaScreen() }

                        composable("ajustes") {
                            AjustesScreen(onNavigateBack = { navController.navigateUp() }, onExitApp = { finish() })
                        }
                    }
                }
            }
        }
    }
}