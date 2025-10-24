package com.example.bosqueantiguo.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bosqueantiguo.model.Usuario
import com.example.bosqueantiguo.viewmodel.UsuarioViewModel

@Composable
fun ResumenScreen(viewModel: UsuarioViewModel = viewModel()) {
    val listaUsuarios by viewModel.usuarios.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarUsuarios()
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔹 Título simple arriba (opcional)
            Text(
                text = "Resumen de Usuarios",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Contenido
            if (listaUsuarios.isEmpty()) {
                Text("No hay usuarios registrados aún.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(listaUsuarios) { usuario ->
                        UsuarioCard(usuario = usuario)
                    }
                }
            }
        }
    }
}

@Composable
fun UsuarioCard(usuario: Usuario) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("👤 ${usuario.nombre}", style = MaterialTheme.typography.titleMedium)
            Text("📧 ${usuario.correo}", style = MaterialTheme.typography.bodyMedium)
            Text("🎂 Edad: ${usuario.edad}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
