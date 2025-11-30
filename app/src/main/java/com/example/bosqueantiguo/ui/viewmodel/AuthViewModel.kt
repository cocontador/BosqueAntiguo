package com.example.bosqueantiguo.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bosqueantiguo.BosqueAntiguoApp
import com.example.bosqueantiguo.model.LoginResponse
import com.example.bosqueantiguo.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val loginResponse: LoginResponse) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

// Cambiado a AndroidViewModel para tener acceso al contexto de la aplicación
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "AuthViewModel"
    }

    private val authRepository = AuthRepository()
    private val tokenManager = getApplication<BosqueAntiguoApp>().tokenManager

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()

    fun doLogin(email: String, password: String) {
        Log.d(TAG, "ViewModel: Iniciando login para $email")
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading
            Log.d(TAG, "Estado: Loading")

            val response = authRepository.login(email, password)

            if (response != null) {
                // Guardar el token
                tokenManager.saveToken(response.token)
                _loginState.value = LoginUiState.Success(response)
                Log.d(TAG, "Estado: Success. Token guardado.")
            } else {
                _loginState.value = LoginUiState.Error("Credenciales incorrectas o error de conexión.")
                Log.d(TAG, "Estado: Error.")
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = LoginUiState.Idle
    }
}