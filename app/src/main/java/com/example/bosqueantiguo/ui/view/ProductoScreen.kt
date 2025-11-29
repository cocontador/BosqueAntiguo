package com.example.bosqueantiguo.ui.view

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bosqueantiguo.model.ProductoApi
import com.example.bosqueantiguo.ui.viewmodel.CarritoViewModel
import com.example.bosqueantiguo.ui.viewmodel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoScreen(
    onNavigateBack: () -> Unit,
    productoViewModel: ProductoViewModel,
    carritoViewModel: CarritoViewModel
) {
    val productos by productoViewModel.productos.collectAsState()
    val isLoading by productoViewModel.isLoading.collectAsState()
    val hasError by productoViewModel.hasError.collectAsState()

    LaunchedEffect(Unit) {
        productoViewModel.cargarProductos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de Productos") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { productoViewModel.reintentar() },
                        enabled = !isLoading
                    ) {
                        val rotation by animateFloatAsState(
                            targetValue = if (isLoading) 360f else 0f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1000, easing = LinearEasing),
                                repeatMode = RepeatMode.Restart
                            ),
                            label = "refresh_rotation"
                        )
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Actualizar",
                            modifier = Modifier.rotate(rotation)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                isLoading -> LoadingContent()
                hasError -> ErrorContent(onRetry = { productoViewModel.reintentar() })
                productos.isEmpty() -> EmptyContent()
                else -> ProductosContent(productos = productos, carritoViewModel = carritoViewModel)
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            CircularProgressIndicator()
            Text(text = "Cargando productos...", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun ErrorContent(onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Error al cargar productos", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.error)
            Text("Verifica los logs en Logcat para más detalles", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
            Text("Busca etiquetas: ProductoViewModel, ProductoRepository, RetrofitConfig", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRetry) { Text("Reintentar") }
            OutlinedButton(onClick = { com.example.bosqueantiguo.network.NetworkTester.probarConectividad() }) { Text("Probar Conectividad") }
        }
    }
}

@Composable
private fun EmptyContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No hay productos disponibles", style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun ProductosContent(productos: List<ProductoApi>, carritoViewModel: CarritoViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(productos) { index, producto ->
            AnimatedVisibility(
                visible = true,
                enter = slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(durationMillis = 300, delayMillis = index * 50)) + fadeIn(animationSpec = tween(durationMillis = 300, delayMillis = index * 50))
            ) {
                ProductoApiCard(producto = producto, onAddToCart = { carritoViewModel.agregarProducto(it) })
            }
        }
    }
}

@Composable
private fun ProductoApiCard(producto: ProductoApi, onAddToCart: (ProductoApi) -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "card_scale"
    )

    Card(
        modifier = Modifier.fillMaxWidth().scale(scale),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = producto.codigo ?: "P${producto.id}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                producto.categoria?.let {
                    Surface(shape = MaterialTheme.shapes.small, color = MaterialTheme.colorScheme.secondaryContainer) {
                        Text(text = it.nombre, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = producto.nombre, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = producto.descripcion ?: "Sin descripción disponible", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$${String.format("%,.0f", producto.precio)}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Stock: ${producto.stock}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (producto.stock > 0) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error
                )
            }
            Button(
                onClick = { onAddToCart(producto) },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                enabled = producto.stock > 0
            ) {
                Icon(Icons.Filled.AddShoppingCart, contentDescription = "Añadir al carrito", modifier = Modifier.size(ButtonDefaults.IconSize))
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Añadir al Carrito")
            }
        }
    }
}