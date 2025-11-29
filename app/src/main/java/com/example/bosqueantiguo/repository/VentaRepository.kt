package com.example.bosqueantiguo.repository

import android.util.Log
import com.example.bosqueantiguo.model.VentaRequest
import com.example.bosqueantiguo.model.VentaResponse
import com.example.bosqueantiguo.network.RetrofitConfig

/**
 * Repository para gestionar las ventas.
 */
class VentaRepository {

    companion object {
        private const val TAG = "VentaRepository"
    }

    private val ventaService = RetrofitConfig.ventaApiService

    /**
     * Envía la venta al backend para ser registrada.
     * @param ventaRequest Los detalles de la venta a registrar.
     * @return VentaResponse si la operación es exitosa, o null en caso de error.
     */
    suspend fun registrarVenta(ventaRequest: VentaRequest): VentaResponse? {
        Log.d(TAG, "🚀 Registrando nueva venta...")
        return try {
            val response = ventaService.registrarVenta(ventaRequest)
            if (response.isSuccessful) {
                Log.d(TAG, "✅ Venta registrada con éxito. ID: ${response.body()?.id}")
                response.body()
            } else {
                Log.e(TAG, "❌ Error al registrar la venta: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 Excepción al registrar la venta:", e)
            null
        }
    }
}
