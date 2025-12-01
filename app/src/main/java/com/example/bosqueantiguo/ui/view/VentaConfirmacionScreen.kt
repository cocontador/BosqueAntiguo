package com.example.bosqueantiguo.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bosqueantiguo.ui.viewmodel.CarritoViewModel
import com.example.bosqueantiguo.ui.viewmodel.VentaUiState

@OptIn(ExperimentalMaterial3Api::class) // CORRECCIÓN DEFINITIVA: Se añade la anotación que faltaba.
@Composable
fun VentaConfirmacionScreen(
    carritoViewModel: CarritoViewModel,
    onVolverATienda: () -> Unit
) {
    val ventaState by carritoViewModel.ventaState.collectAsState()
    val carritoItems by carritoViewModel.carritoItems.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compra Realizada") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (ventaState is VentaUiState.Success) {
                val venta = (ventaState as VentaUiState.Success).venta

                Text("Se ha realizado la compra. nro #${venta.id}", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))

                Text("Detalle de la compra", style = MaterialTheme.typography.titleLarge)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text("Producto", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                    Text("Cantidad", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Text("Subtotal", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold, textAlign = TextAlign.End)
                }
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(carritoItems) { item ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(item.producto.nombre, modifier = Modifier.weight(1f))
                            Text(item.cantidad.toString(), modifier = Modifier.weight(0.5f), textAlign = TextAlign.Center)
                            Text(String.format("$%.2f", item.producto.precio * item.cantidad), modifier = Modifier.weight(0.5f), textAlign = TextAlign.End)
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Text("Total pagado: ", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(String.format("$%.2f", venta.total), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = {
                    carritoViewModel.finalizarCompraYLimpiar()
                    onVolverATienda()
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Volver a la tienda")
                }

            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Cargando confirmación...")
                }
            }
        }
    }
}