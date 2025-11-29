package com.example.bosqueantiguo.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bosqueantiguo.model.ClimaApi
import com.example.bosqueantiguo.repository.ClimaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClimaViewModel : ViewModel() {

    companion object {
        private const val TAG = "ClimaViewModel"
    }

    private val repository = ClimaRepository()

    private val _clima = MutableStateFlow<ClimaApi?>(null)
    private val _isLoading = MutableStateFlow(false)
    private val _hasError = MutableStateFlow(false)

    val clima: StateFlow<ClimaApi?> = _clima.asStateFlow()
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    val hasError: StateFlow<Boolean> = _hasError.asStateFlow()

    init {
        Log.d(TAG, "ClimaViewModel inicializado")
    }

    fun cargarClima(ciudad: String) {
        Log.d(TAG, "ViewModel: Iniciando carga del clima para $ciudad")
        viewModelScope.launch {
            _isLoading.value = true
            _hasError.value = false
            Log.d(TAG, "Estado: isLoading = true, hasError = false")

            try {
                repository.obtenerClima(ciudad).collect { climaApi ->
                    if (climaApi != null) {
                        _clima.value = climaApi
                        _hasError.value = false
                        Log.d(TAG, "Datos de clima recibidos en ViewModel para $ciudad")
                    } else {
                        _hasError.value = true
                        _clima.value = null
                        Log.w(TAG, "No se recibieron datos de clima o hubo un error en el repositorio")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error en ViewModel al cargar el clima:", e)
                _hasError.value = true
                _clima.value = null
            } finally {
                _isLoading.value = false
                Log.d(TAG, "Carga de clima finalizada: isLoading = false")
            }
        }
    }

    fun reintentar(ciudad: String) {
        Log.d(TAG, "Usuario presionó Reintentar para la ciudad $ciudad")
        cargarClima(ciudad)
    }
}