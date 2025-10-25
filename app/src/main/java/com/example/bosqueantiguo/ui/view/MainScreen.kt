package com.example.bosqueantiguo.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bosqueantiguo.R

@Composable
fun MainScreen(
    onNavigateToRegistro: () -> Unit,
    onNavigateToPerfil: () -> Unit,
    onNavigateToAjustes: () -> Unit
) {
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
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onNavigateToRegistro, modifier = Modifier.fillMaxWidth()) {
            Text("Registro")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onNavigateToPerfil, modifier = Modifier.fillMaxWidth()) {
            Text("Perfil")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onNavigateToAjustes, modifier = Modifier.fillMaxWidth()) {
            Text("Ajustes")
        }
    }
}
