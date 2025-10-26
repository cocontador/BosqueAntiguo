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
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.bosqueantiguo.R
import com.example.bosqueantiguo.viewmodel.UsuarioViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    usuarioId: String?,
    onNavigateBack: () -> Unit,
    viewModel: UsuarioViewModel
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var cameraUri by remember { mutableStateOf<Uri?>(null) }

    // 🔹 Cargar datos del usuario seleccionado
    LaunchedEffect(usuarioId) {
        usuarioId?.let { viewModel.cargarUsuarioPorId(it.toInt()) }
    }

    val usuario = viewModel.usuarioSeleccionado.collectAsState().value

    // 🚀 Inicializa la imagen con la del usuario (si existe)
    LaunchedEffect(usuario?.imagenUri) {
        usuario?.imagenUri?.let {
            imageUri = Uri.parse(it)
        }
    }

    // --------- FUNCIONES AUXILIARES -----------

    // Copiar imagen de la galería a carpeta interna
    fun copyImageToAppStorage(context: Context, sourceUri: Uri): Uri? {
        return try {
            val time = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File(dir, "IMG_${time}.jpg")

            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Crea archivo temporal para fotos tomadas
    fun createImageUri(context: Context): Uri {
        val time = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile("JPEG_${time}_", ".jpg", dir)
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }

    // --------- SELECTOR DE IMAGEN -----------

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            // Copiamos la imagen a la carpeta de la app
            val localUri = copyImageToAppStorage(context, uri)
            imageUri = localUri
            if (localUri != null && usuario != null) {
                viewModel.actualizarImagenUsuario(usuario.id, localUri.toString())
            }
        }
    }

    val takePicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { ok ->
        if (ok && usuario != null) {
            imageUri = cameraUri
            viewModel.actualizarImagenUsuario(usuario.id, cameraUri.toString())
        }
    }

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

    // --------- UI -----------

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil de ${usuario?.nombre ?: "Usuario"}") },
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

            Text("Foto de perfil", style = MaterialTheme.typography.titleMedium)

            AsyncImage(
                model = imageUri ?: R.drawable.no_profile_picture,
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

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline
            )

            usuario?.let {
                Text("📧 ${it.correo}", style = MaterialTheme.typography.bodyLarge)
                Text("🎂 Edad: ${it.edad}", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
