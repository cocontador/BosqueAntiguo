package com.example.bosqueantiguo.ui.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.bosqueantiguo.R
import com.example.bosqueantiguo.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    userId: Int, // <-- El ID del usuario a mostrar
    viewModel: UsuarioViewModel,
    onNavigateBack: () -> Unit
) {
    val usuarios by viewModel.usuarios.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Busca el usuario específico por su ID
    val usuario = usuarios.find { it.id == userId }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null && usuario != null) {
                // Actualiza la foto del usuario específico
                viewModel.actualizarFotoUsuario(usuario.id, uri)
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil de Usuario") },
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
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else if (usuario == null) {
                Text("Usuario no encontrado.")
            } else {
                // Lógica para elegir la foto por defecto según el nombre
                val defaultPhoto = when {
                    usuario.nombre.contains("pau", ignoreCase = true) -> R.drawable.pau
                    usuario.nombre.contains("coco", ignoreCase = true) -> R.drawable.coco
                    usuario.nombre.contains("cesar", ignoreCase = true) -> R.drawable.cesar
                    else -> R.drawable.logoba
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    AsyncImage(
                        model = usuario.fotoUri?.toUri() ?: defaultPhoto,
                        contentDescription = "Imagen de perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = usuario.nombre,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = usuario.correo,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = { /* TODO: Lógica para la cámara */ }) {
                            Icon(Icons.Default.PhotoCamera, contentDescription = "Cámara")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Cámara")
                        }

                        Button(onClick = { galleryLauncher.launch("image/*") }) {
                            Icon(Icons.Default.Image, contentDescription = "Galería")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Galería")
                        }
                    }
                }
            }
        }
    }
}
