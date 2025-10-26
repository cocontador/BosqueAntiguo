package com.example.bosqueantiguo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bosqueantiguo.model.Usuario
import com.example.bosqueantiguo.repository.UsuarioRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel principal para gestionar usuarios.
 * Administra el estado del formulario y las operaciones CRUD.
 */
class UsuarioViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {

    // Estado del formulario
    private val _uiState = MutableStateFlow(UsuarioUIState())
    val uiState: StateFlow<UsuarioUIState> = _uiState.asStateFlow()

    // Lista de usuarios almacenados (Room Flow)
    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuarios: StateFlow<List<Usuario>> = _usuarios.asStateFlow()

    private val _imagenUri = MutableStateFlow<String?>(null)
    val imagenUri: StateFlow<String?> = _imagenUri.asStateFlow()


    private val _usuarioSeleccionado = MutableStateFlow<Usuario?>(null)
    val usuarioSeleccionado: StateFlow<Usuario?> = _usuarioSeleccionado

    fun cargarUsuarioPorId(id: Int) {
        viewModelScope.launch {
            val usuario = repository.obtenerUsuarioPorId(id)
            _usuarioSeleccionado.value = usuario
        }
    }

    fun actualizarImagenUsuario(id: Int, nuevaUri: String) {
        viewModelScope.launch {
            repository.actualizarImagenUsuario(id, nuevaUri)
            cargarUsuarioPorId(id) // refresca
        }
    }

    fun setImagenUri(uri: String?) {
        _imagenUri.value = uri
    }


    init {
        cargarUsuarios()
    }

    /** Actualiza los campos del formulario */
    fun onNombreChange(nuevo: String) {
        _uiState.update { it.copy(nombre = nuevo) }
    }
    fun resetGuardado() {
        _uiState.update { it.copy(guardadoExitoso = false) }
    }

    fun onCorreoChange(nuevo: String) {
        _uiState.update { it.copy(correo = nuevo) }
    }

    fun onEdadChange(nuevo: String) {
        _uiState.update { it.copy(edad = nuevo) }
    }

    fun onContrasenaChange(nuevo: String) {
        _uiState.update { it.copy(contrasena = nuevo) }
    }

    /** Inserta un nuevo usuario con validaciones */
    fun guardarUsuario() {
        val estado = _uiState.value
        val errores = validarCampos(estado)

        // Si hay errores, actualiza el estado y no guarda
        if (errores != UsuarioErrores()) {
            _uiState.update { it.copy(errores = errores, guardadoExitoso = false) }
            return
        }

        viewModelScope.launch {
            val usuario = Usuario(
                nombre = estado.nombre.trim(),
                correo = estado.correo.trim(),
                edad = estado.edad.toIntOrNull() ?: 0,
                contrasena = estado.contrasena.trim(),
                imagenUri = _imagenUri.value

            )
            repository.insertarUsuario(usuario)
            _uiState.update {
                it.copy(
                    nombre = "",
                    correo = "",
                    edad = "",
                    contrasena = "",
                    errores = UsuarioErrores(),
                    guardadoExitoso = true
                )
            }
        }
    }

    /** Elimina un usuario específico */
    fun eliminarUsuario(usuario: Usuario) {
        viewModelScope.launch {
            repository.eliminarUsuario(usuario)
        }
    }

    /** Valida los campos antes de guardar */
    private fun validarCampos(estado: UsuarioUIState): UsuarioErrores {
        var nombreErr: String? = null
        var correoErr: String? = null
        var edadErr: String? = null
        var contrasenaErr: String? = null

        if (estado.nombre.isBlank()) nombreErr = "El nombre no puede estar vacío, por favor ingresa tu nombre"
        if (!estado.correo.contains("@")) correoErr = "Correo inválido, debe contener @"
        if (estado.edad.toIntOrNull() == null) edadErr = "Edad inválida, ingrese solo números y no letras."
        if (estado.contrasena.length < 4) contrasenaErr = "Su clave debe tener mínimo 4 caracteres."

        return UsuarioErrores(
            nombreError = nombreErr,
            correoError = correoErr,
            edadError = edadErr,
            contrasenaError = contrasenaErr
        )
    }

    /** Carga la lista de usuarios desde Room (Flow) */
    fun cargarUsuarios() {
        viewModelScope.launch {
            repository.obtenerUsuarios().collect { lista ->
                _usuarios.value = lista
            }
        }
    }

    /** Elimina todos los usuarios */
    fun eliminarTodo() {
        viewModelScope.launch {
            repository.eliminarTodos()
        }
    }
}
