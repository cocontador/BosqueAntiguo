package com.example.bosqueantiguo.repository

import android.util.Log
import com.example.bosqueantiguo.model.ProductoApi
import com.example.bosqueantiguo.network.RetrofitConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repository que maneja los datos de productos desde la API
 * Encapsula las llamadas de red y maneja los errores
 */
class ProductoRepository {
    
    companion object {
        private const val TAG = "ProductoRepository"
    }
    
    private val apiService = RetrofitConfig.productoApiService
    
    /**
     * Obtiene productos de la API como Flow (stream de datos)
     * @param soloDisponibles si es true, trae solo productos con stock
     */
    fun obtenerProductos(soloDisponibles: Boolean = true): Flow<List<ProductoApi>> = flow {
        Log.d(TAG, "🚀 Iniciando petición a API de productos")
        Log.d(TAG, "📋 Parámetros: soloDisponibles = $soloDisponibles")
        
        try {
            Log.d(TAG, "🌐 Realizando petición HTTP...")
            val response = apiService.getProductos(disponibles = soloDisponibles)
            
            Log.d(TAG, "📡 Respuesta recibida:")
            Log.d(TAG, "   - Código: ${response.code()}")
            Log.d(TAG, "   - Mensaje: ${response.message()}")
            Log.d(TAG, "   - Es exitosa: ${response.isSuccessful}")
            Log.d(TAG, "   - Headers: ${response.headers()}")
            
            if (response.isSuccessful) {
                response.body()?.let { productos ->
                    Log.d(TAG, "✅ Productos recibidos: ${productos.size} items")
                    productos.forEachIndexed { index, producto ->
                        Log.d(TAG, "   $index: ${producto.codigo ?: "SIN_CODIGO"} - ${producto.nombre} (Stock: ${producto.stock})")
                    }
                    emit(productos)
                } ?: run {
                    Log.w(TAG, "⚠️ Respuesta exitosa pero body es null")
                    emit(emptyList())
                }
            } else {
                Log.e(TAG, "❌ Error en la respuesta HTTP:")
                Log.e(TAG, "   - Código: ${response.code()}")
                Log.e(TAG, "   - Mensaje: ${response.message()}")
                Log.e(TAG, "   - Body error: ${response.errorBody()?.string()}")
                emit(emptyList())
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 Excepción en petición API:", e)
            Log.e(TAG, "   - Tipo: ${e.javaClass.simpleName}")
            Log.e(TAG, "   - Mensaje: ${e.message}")
            Log.e(TAG, "   - Causa: ${e.cause}")
            e.printStackTrace()
            emit(emptyList())
        }
    }
    
    /**
     * Obtiene un producto específico por ID
     */
    suspend fun obtenerProductoPorId(id: Long): ProductoApi? {
        Log.d(TAG, "🔍 Buscando producto por ID: $id")
        return try {
            val response = apiService.getProducto(id)
            Log.d(TAG, "📡 Respuesta para producto $id: código ${response.code()}")
            
            if (response.isSuccessful) {
                val producto = response.body()
                Log.d(TAG, "✅ Producto encontrado: ${producto?.nombre}")
                producto
            } else {
                Log.e(TAG, "❌ Error al buscar producto $id: ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 Excepción al buscar producto $id:", e)
            e.printStackTrace()
            null
        }
    }
}