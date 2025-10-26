package com.example.bosqueantiguo.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bosqueantiguo.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack


data class Producto(
    val codigo: String,
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val stock: Int,
    val categoria: String,
    val img: Int
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoScreen(onNavigateBack: () -> Unit) {
    val productos = listOf(
        Producto("PI001", "Ficus", "Planta de interior de hojas brillantes, fácil de cuidar.", 10000, 25, "Plantas de interior", R.drawable.aire_puro),
        Producto("PI002", "Sansevieria", "Conocida como lengua de suegra, muy resistente.", 15000, 40, "Plantas de interior", R.drawable.jardineria),
        Producto("PI003", "Afelandra", "Planta tropical con hojas llamativas y flores amarillas.", 12000, 15, "Plantas de interior", R.drawable.fondo_de_planta_de_interior_verde_para_amantes_de_las_plantas),
        Producto("AO001", "Azalea", "Arbusto ornamental con flores rosadas y blancas.", 9000, 30, "Arbustos ornamentales", R.drawable.jardineria1),
        Producto("FR001", "Mandarino", "Árbol frutal de mandarinas dulces y jugosas.", 12000, 20, "Frutales", R.drawable.alto_angulo_de_plantas_en_macetas_negras)
    )


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
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                // esto asegura que el contenido no se solape con la barra superior
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(productos) { producto ->
                    ProductoCard(producto)
                }
            }
        }
    }
}

@Composable
fun ProductoCard(producto: Producto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = producto.img),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 12.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text(producto.descripcion, style = MaterialTheme.typography.bodyMedium)
                Text("Precio: $${producto.precio}", style = MaterialTheme.typography.bodyMedium)
            }

        }
    }
}