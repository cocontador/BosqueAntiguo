package com.example.bosqueantiguo.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bosqueantiguo.R
import androidx.compose.animation.core.*

@Composable
fun MainScreen(
    onNavigateToRegistro: () -> Unit,
    onNavigateToPerfil: () -> Unit,
    onNavigateToAjustes: () -> Unit,
    onNavigateToProducto: () -> Unit,
    onNavigateToResumen: () -> Unit,
    onNavigateToClima: () -> Unit // Nuevo parámetro
) {
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

        Button(onClick = onNavigateToRegistro, modifier = Modifier.fillMaxWidth()) {
            Text("Registro")
        }


        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onNavigateToResumen, modifier = Modifier.fillMaxWidth()) {
            Text("Usuarios")
        }
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onNavigateToProducto, modifier = Modifier.fillMaxWidth()) {
            Text("Catálogo")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Nuevo botón para Clima
        Button(onClick = onNavigateToClima, modifier = Modifier.fillMaxWidth()) {
            Text("Clima")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onNavigateToAjustes, modifier = Modifier.fillMaxWidth()) {
            Text("Ajustes")
        }
    }
}
