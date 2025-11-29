package com.example.bosqueantiguo.ui.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bosqueantiguo.model.CarritoItem
import com.example.bosqueantiguo.ui.viewmodel.CarritoViewModel
import com.example.bosqueantiguo.ui.viewmodel.VentaUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    carritoViewModel: CarritoViewModel,
    onNavigateBack: () -> Unit
) {
    val carritoItems by carritoViewModel.carritoItems.collectAsState()
    val totalPrecio by carritoViewModel.totalPrecio.collectAsState()
    val ventaState by carritoViewModel.ventaState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(ventaState) {
        when (val state = ventaState) {
            is VentaUiState.Success -> {
                Toast.makeText(context, "Venta registrada con éxito! ID: ${state.venta.id}", Toast.LENGTH_LONG).show()
                carritoViewModel.resetVentaState()
                onNavigateBack()
            }
            is VentaUiState.Error -> {
                Toast.makeText(context, "Error: ${state.message}", Toast.LENGTH_LONG).show()
                carritoViewModel.resetVentaState()
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras") },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") } }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)
        ) {
            if (carritoItems.isEmpty() && ventaState !is VentaUiState.Loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("El carrito está vacío", fontSize = 20.sp)
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(items = carritoItems, key = { it.producto.id }) { item ->
                        CarritoItemView(item = item, viewModel = carritoViewModel)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total:", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text(String.format("$%.2f", totalPrecio), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    if (ventaState is VentaUiState.Loading) {
                        CircularProgressIndicator()
                    } else {
                        Button(
                            onClick = { carritoViewModel.procesarPago() },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = carritoItems.isNotEmpty()
                        ) {
                            Text("Proceder al Pago")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CarritoItemView(item: CarritoItem, viewModel: CarritoViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(item.producto.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(String.format("Precio: $%.2f", item.producto.precio))

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { viewModel.actualizarCantidad(item.producto.id, item.cantidad - 1) }) {
                        Icon(Icons.Filled.Remove, contentDescription = "Quitar uno")
                    }
                    Text("${item.cantidad}", fontSize = 18.sp, modifier = Modifier.padding(horizontal = 8.dp))
                    IconButton(onClick = { viewModel.agregarProducto(item.producto) }) {
                        Icon(Icons.Filled.Add, contentDescription = "Añadir uno")
                    }
                }
                IconButton(onClick = { viewModel.removerProducto(item.producto.id) }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar producto", tint = MaterialTheme.colorScheme.error)
                }
            }
            Text(String.format("Subtotal: $%.2f", item.producto.precio * item.cantidad), modifier = Modifier.align(Alignment.End))
        }
    }
}
