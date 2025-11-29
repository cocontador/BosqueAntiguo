package com.example.bosqueantiguo.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bosqueantiguo.model.LoginResponse
import com.example.bosqueantiguo.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Representa los posibles estados de la UI de la pantalla de login.
 */
sealed class LoginUiState {
    object Idle : LoginUiState() // Estado inicial
    object Loading : LoginUiState() // Cargando
    data class Success(val loginResponse: LoginResponse) : LoginUiState() // Login exitoso
    data class Error(val message: String) : LoginUiState() // Error en el login
}

/**
 * ViewModel para la pantalla de Login.
 */
class AuthViewModel : ViewModel() {

    companion object {
        private const val TAG = "AuthViewModel"
    }

    private val authRepository = AuthRepository()

    // Estado privado y mutable de la UI
    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    // Estado público e inmutable para la UI
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    /**
     * Inicia el proceso de login.
     * @param email Email del usuario.
     * @param password Contraseña del usuario.
     */
    fun doLogin(email: String, password: String) {
        Log.d(TAG, "📋 ViewModel: Iniciando login para $email")
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading
            Log.d(TAG, "🔄 Estado: Loading")

            val response = authRepository.login(email, password)

            if (response != null) {
                _loginState.value = LoginUiState.Success(response)
                Log.d(TAG, "🔄 Estado: Success. Token: ${response.token}")
            } else {
                _loginState.value = LoginUiState.Error("Credenciales incorrectas o error de conexión.")
                Log.d(TAG, "🔄 Estado: Error.")
            }
        }
    }

    /**
     * Resetea el estado del login a Idle.
     */
    fun resetLoginState() {
        _loginState.value = LoginUiState.Idle
    }
}