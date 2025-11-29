package com.example.bosqueantiguo.ui.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bosqueantiguo.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToRegistro: () -> Unit,
    onNavigateToPerfil: () -> Unit,
    onNavigateToAjustes: () -> Unit,
    onNavigateToProducto: () -> Unit,
    onNavigateToResumen: () -> Unit,
    onNavigateToClima: () -> Unit,
    onNavigateToCarrito: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* Texto eliminado */ },
                actions = {
                    IconButton(onClick = onNavigateToCarrito) {
                        Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito de compras")
                    }
                }
            )
        }
    ) { innerPadding ->
        val scale = remember { Animatable(0f) }

        LaunchedEffect(Unit) {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 900,
                    easing = FastOutSlowInEasing
                )
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Aplicamos el padding del Scaffold
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logoba),
                contentDescription = "Logo de la aplicación",
                modifier = Modifier.size(150.dp).scale(scale.value)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = onNavigateToRegistro, modifier = Modifier.fillMaxWidth()) { Text("Registro") }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onNavigateToResumen, modifier = Modifier.fillMaxWidth()) { Text("Usuarios") }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onNavigateToProducto, modifier = Modifier.fillMaxWidth()) { Text("Catálogo") }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onNavigateToClima, modifier = Modifier.fillMaxWidth()) { Text("Clima") }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onNavigateToAjustes, modifier = Modifier.fillMaxWidth()) { Text("Ajustes") }
        }
    }
}
