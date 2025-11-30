package com.example.bosqueantiguo.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bosqueantiguo.model.ProductoApi
import com.example.bosqueantiguo.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductoViewModel : ViewModel() {
    
    companion object {
        private const val TAG = "ProductoViewModel"
    }
    
    private val repository = ProductoRepository()
    
    init {
        Log.d(TAG, "ProductoViewModel inicializado")
    }
    
    private val _productos = MutableStateFlow<List<ProductoApi>>(emptyList())
    private val _isLoading = MutableStateFlow(false)
    private val _hasError = MutableStateFlow(false)
    
    val productos: StateFlow<List<ProductoApi>> = _productos.asStateFlow()
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    val hasError: StateFlow<Boolean> = _hasError.asStateFlow()
    
    fun cargarProductos() {
        Log.d(TAG, "ViewModel: Iniciando carga de productos")
        viewModelScope.launch {
            _isLoading.value = true
            _hasError.value = false
            try {
                // CORREGIDO: Pedimos TODOS los productos, no solo los disponibles.
                repository.obtenerProductos(soloDisponibles = false).collect { listaProductos ->
                    _productos.value = listaProductos
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error en ViewModel al cargar productos:", e)
                _hasError.value = true
                _productos.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun buscarYAgregarProducto(productoId: Long, carritoViewModel: CarritoViewModel) {
        Log.d(TAG, "Buscando producto con ID: $productoId para agregarlo al carrito")
        viewModelScope.launch {
            try {
                val producto = repository.obtenerProductoPorId(productoId)
                if (producto != null) {
                    Log.d(TAG, "Producto encontrado: ${producto.nombre}")
                    carritoViewModel.agregarProducto(producto)
                } else {
                    Log.w(TAG, "No se encontró el producto con ID: $productoId")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al buscar producto por ID: $productoId", e)
            }
        }
    }
    
    fun reintentar() {
        Log.d(TAG, "Usuario presionó Reintentar")
        cargarProductos()
    }
}