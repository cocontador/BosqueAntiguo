package com.example.bosqueantiguo.ui.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.bosqueantiguo.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.unit.dp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(onNavigateBack: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current

    var imageUri by remember { mutableStateOf<Uri?>(null) } // imagen mostrada
    var cameraUri by remember { mutableStateOf<Uri?>(null) } // destino de la foto

    // Abrir galería (no requiere permisos en Android 13+)
    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> if (uri != null) imageUri = uri }

    // Tomar foto y guardar en cameraUri
    val takePicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { ok -> if (ok) imageUri = cameraUri }

    // Pedir permiso de cámara si hace falta
    val requestCameraPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = createImageUri(context)
            cameraUri = uri
            takePicture.launch(uri)
        }
    }

    fun openCamera() {
        val granted = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        if (granted) {
            val uri = createImageUri(context)
            cameraUri = uri
            takePicture.launch(uri)
        } else {
            requestCameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen (foto tomada o seleccionada) con placeholder
            AsyncImage(
                model = imageUri ?: R.drawable.logoba,
                contentDescription = "Imagen de perfil",
                modifier = Modifier.size(180.dp)
            )

            Button(
                onClick = { pickImage.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Elegir desde galería") }

            Button(
                onClick = { openCamera() },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Tomar foto con cámara") }
        }
    }
}

/** Crea archivo temporal y devuelve su Uri con FileProvider */
private fun createImageUri(context: Context): Uri {
    val time = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val file = File.createTempFile("JPEG_${time}_", ".jpg", dir)
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}
