package com.example.bosqueantiguo.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bosqueantiguo.model.CarritoItem
import com.example.bosqueantiguo.model.ProductoApi
import com.example.bosqueantiguo.model.VentaRequest
import com.example.bosqueantiguo.model.VentaDetalleRequest
import com.example.bosqueantiguo.model.VentaResponse
import com.example.bosqueantiguo.repository.VentaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class VentaUiState {
    object Idle : VentaUiState()
    object Loading : VentaUiState()
    data class Success(val venta: VentaResponse) : VentaUiState()
    data class Error(val message: String) : VentaUiState()
}

// CORREGIDO: Ahora recibe su dependencia por el constructor
class CarritoViewModel(private val ventaRepository: VentaRepository) : ViewModel() {

    companion object {
        private const val TAG = "CarritoViewModel"
    }

    private val _carritoItems = MutableStateFlow<List<CarritoItem>>(emptyList())
    val carritoItems: StateFlow<List<CarritoItem>> = _carritoItems.asStateFlow()

    private val _totalPrecio = MutableStateFlow(0.0)
    val totalPrecio: StateFlow<Double> = _totalPrecio.asStateFlow()

    private val _ventaState = MutableStateFlow<VentaUiState>(VentaUiState.Idle)
    val ventaState: StateFlow<VentaUiState> = _ventaState.asStateFlow()

    fun agregarProducto(producto: ProductoApi) {
        _carritoItems.update { currentItems ->
            val existingItem = currentItems.find { it.producto.id == producto.id }
            if (existingItem != null) {
                currentItems.map { if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + 1) else it }
            } else {
                currentItems + CarritoItem(producto = producto, cantidad = 1)
            }
        }
        actualizarTotal()
    }

    fun removerProducto(productoId: Long) {
        _carritoItems.update { currentItems ->
            currentItems.filterNot { it.producto.id == productoId }
        }
        actualizarTotal()
    }

    fun actualizarCantidad(productoId: Long, nuevaCantidad: Int) {
        if (nuevaCantidad <= 0) {
            removerProducto(productoId)
            return
        }
        _carritoItems.update { currentItems ->
            currentItems.map { if (it.producto.id == productoId) it.copy(cantidad = nuevaCantidad) else it }
        }
        actualizarTotal()
    }

    fun procesarPago() {
        viewModelScope.launch {
            if (_carritoItems.value.isEmpty()) return@launch

            _ventaState.value = VentaUiState.Loading

            val detallesRequest = _carritoItems.value.map {
                VentaDetalleRequest(productoId = it.producto.id, cantidad = it.cantidad)
            }
            val ventaRequest = VentaRequest(detalles = detallesRequest)

            val resultado = ventaRepository.registrarVenta(ventaRequest)

            if (resultado != null) {
                _ventaState.value = VentaUiState.Success(resultado)
            } else {
                _ventaState.value = VentaUiState.Error("No se pudo procesar la venta.")
            }
        }
    }
    
    fun finalizarCompraYLimpiar() {
        limpiarCarrito()
        resetVentaState()
    }

    private fun resetVentaState() {
        _ventaState.value = VentaUiState.Idle
    }

    private fun limpiarCarrito() {
        _carritoItems.value = emptyList()
        actualizarTotal()
    }

    private fun actualizarTotal() {
        val total = _carritoItems.value.sumOf { it.producto.precio * it.cantidad }
        _totalPrecio.value = total
    }
}