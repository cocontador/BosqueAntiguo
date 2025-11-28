package com.example.bosqueantiguo.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bosqueantiguo.model.ProductoApi
import com.example.bosqueantiguo.network.NetworkTester
import com.example.bosqueantiguo.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de productos
 * Maneja el estado de la UI y las operaciones con productos de la API
 */
class ProductoViewModel : ViewModel() {
    
    companion object {
        private const val TAG = "ProductoViewModel"
    }
    
    private val repository = ProductoRepository()
    
    init {
        Log.d(TAG, "🎯 ProductoViewModel inicializado")
        // Probar conectividad al inicializar
        NetworkTester.probarConectividad()
    }
    
    // Estado privado mutable
    private val _productos = MutableStateFlow<List<ProductoApi>>(emptyList())
    private val _isLoading = MutableStateFlow(false)
    private val _hasError = MutableStateFlow(false)
    
    // Estado público inmutable para la UI
    val productos: StateFlow<List<ProductoApi>> = _productos.asStateFlow()
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    val hasError: StateFlow<Boolean> = _hasError.asStateFlow()
    
    /**
     * Carga productos desde la API
     * Se ejecuta en un hilo de fondo (corrutina)
     */
    fun cargarProductos() {
        Log.d(TAG, "📋 ViewModel: Iniciando carga de productos")
        viewModelScope.launch {
            _isLoading.value = true
            _hasError.value = false
            Log.d(TAG, "🔄 Estado: isLoading = true, hasError = false")
            
            try {
                Log.d(TAG, "📡 Conectando con repository...")
                // Collect es como un "listener" que recibe datos del Flow
                repository.obtenerProductos().collect { listaProductos ->
                    Log.d(TAG, "📦 Datos recibidos en ViewModel: ${listaProductos.size} productos")
                    
                    // Validar productos antes de asignar
                    val productosValidos = listaProductos.filter { producto ->
                        producto.nombre.isNotEmpty() && producto.precio >= 0
                    }
                    
                    Log.d(TAG, "✅ Productos válidos después de filtrar: ${productosValidos.size}")
                    
                    _productos.value = productosValidos
                    _hasError.value = productosValidos.isEmpty()
                    Log.d(TAG, "🔄 Estado actualizado: productos = ${productosValidos.size}, hasError = ${productosValidos.isEmpty()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "💥 Error en ViewModel al cargar productos:", e)
                _hasError.value = true
                _productos.value = emptyList()
                Log.d(TAG, "🔄 Estado de error: hasError = true, productos = vacío")
            } finally {
                _isLoading.value = false
                Log.d(TAG, "✅ Carga finalizada: isLoading = false")
            }
        }
    }
    
    /**
     * Reintentar carga en caso de error
     */
    fun reintentar() {
        Log.d(TAG, "🔁 Usuario presionó Reintentar")
        cargarProductos()
    }
}