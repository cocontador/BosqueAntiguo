package com.example.bosqueantiguo.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bosqueantiguo.model.Categoria
import com.example.bosqueantiguo.repository.CategoriaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoriaViewModel : ViewModel() {

    companion object {
        private const val TAG = "CategoriaViewModel"
    }

    private val repository = CategoriaRepository()

    private val _categorias = MutableStateFlow<List<Categoria>>(emptyList())
    val categorias: StateFlow<List<Categoria>> = _categorias.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _hasError = MutableStateFlow(false)
    val hasError: StateFlow<Boolean> = _hasError.asStateFlow()

    init {
        cargarCategorias()
    }

    fun cargarCategorias() {
        Log.d(TAG, "Iniciando carga de categorías")
        viewModelScope.launch {
            _isLoading.value = true
            _hasError.value = false
            try {
                repository.obtenerCategorias().collect { listaCategorias ->
                    _categorias.value = listaCategorias
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error en ViewModel al cargar categorías:", e)
                _hasError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }
}