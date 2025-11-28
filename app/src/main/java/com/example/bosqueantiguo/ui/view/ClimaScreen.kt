package com.example.bosqueantiguo.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bosqueantiguo.ui.viewmodel.ClimaViewModel

@Composable
fun ClimaScreen(climaViewModel: ClimaViewModel = viewModel()) {

    val clima by climaViewModel.clima.collectAsState()
    val isLoading by climaViewModel.isLoading.collectAsState()
    val hasError by climaViewModel.hasError.collectAsState()

    var ciudad by remember { mutableStateOf(TextFieldValue("Santiago")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Consulta el Clima", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = ciudad,
                onValueChange = { ciudad = it },
                label = { Text("Ciudad") },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = { climaViewModel.cargarClima(ciudad.text) }, modifier = Modifier.padding(start = 8.dp)) {
                Text("Buscar")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        when {
            isLoading -> {
                CircularProgressIndicator()
            }
            hasError -> {
                Text("Error al cargar el clima. Asegúrate de tener conexión y una API Key válida.")
                Button(onClick = { climaViewModel.reintentar(ciudad.text) }) {
                    Text("Reintentar")
                }
            }
            clima != null -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text("Clima en ${ciudad.text}", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Descripción: ${clima!!.weather.firstOrNull()?.description ?: "N/A"}")
                    Text("Temperatura: ${clima!!.main.temp}°C")
                    Text("Humedad: ${clima!!.main.humidity}%")
                }
            }
        }
    }
}