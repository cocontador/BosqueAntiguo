package com.example.bosqueantiguo.ui.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.bosqueantiguo.R
import com.example.bosqueantiguo.viewmodel.UsuarioViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen(
    viewModel: UsuarioViewModel = viewModel(),
    onGuardado: () -> Unit = {},
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

    var imageUri by remember { mutableStateOf<Uri?>(null) } // Imagen mostrada
    var cameraUri by remember { mutableStateOf<Uri?>(null) } // Destino de la foto


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

    // Launcher para seleccionar imagen desde galería
    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            // Copiamos la imagen elegida a la carpeta local de la app
            val localUri = copyImageToAppStorage(context, uri)
            imageUri = localUri
            viewModel.setImagenUri(localUri.toString()) // guarda la URI local
        }
    }

    // Launcher para tomar foto con cámara
    val takePicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { ok ->
        if (ok) {
            imageUri = cameraUri
            viewModel.setImagenUri(cameraUri.toString()) // guarda la URI en el ViewModel
        }
    }
    // Crea archivo temporal para la foto
    fun createImageUri(context: Context): Uri {
        val time = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile("JPEG_${time}_", ".jpg", dir)
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }

    // Launcher para pedir permiso de cámara
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

    // Redirigir cuando el guardado sea exitoso
    LaunchedEffect(state.guardadoExitoso) {
        if (state.guardadoExitoso) onGuardado()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🖼 Imagen (foto tomada o seleccionada)
            AsyncImage(
                model = imageUri ?: R.drawable.logoba,
                contentDescription = "Foto del usuario",
                modifier = Modifier
                    .size(180.dp)
                    .padding(top = 16.dp)
            )

            // Botones para elegir o tomar foto
            Button(
                onClick = { pickImage.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Elegir desde galería") }

            Button(
                onClick = { openCamera() },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Tomar foto con cámara") }

            // Campos del formulario
            OutlinedTextField(
                value = state.nombre,
                onValueChange = viewModel::onNombreChange,
                label = { Text("Nombre") },
                isError = state.errores.nombreError != null,
                supportingText = {
                    state.errores.nombreError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.correo,
                onValueChange = viewModel::onCorreoChange,
                label = { Text("Correo electrónico") },
                isError = state.errores.correoError != null,
                supportingText = {
                    state.errores.correoError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.edad,
                onValueChange = viewModel::onEdadChange,
                label = { Text("Edad") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = state.errores.edadError != null,
                supportingText = {
                    state.errores.edadError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.contrasena,
                onValueChange = viewModel::onContrasenaChange,
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                isError = state.errores.contrasenaError != null,
                supportingText = {
                    state.errores.contrasenaError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { viewModel.guardarUsuario() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar usuario")
            }
        }
    }
}
