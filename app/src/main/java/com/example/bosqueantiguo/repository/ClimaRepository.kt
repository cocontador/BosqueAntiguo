package com.example.bosqueantiguo.repository

import android.util.Log
import com.example.bosqueantiguo.model.ClimaApi
import com.example.bosqueantiguo.network.RetrofitConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repository que maneja los datos del clima desde la API
 * Encapsula la llamada de red y maneja los errores
 */
class ClimaRepository {

    companion object {
        private const val TAG = "ClimaRepository"
        // API Key de OpenWeatherMap
        private const val API_KEY = "156493d1ae2d3cdb059694e405b77777"
    }

    private val apiService = RetrofitConfig.climaApiService

    /**
     * Obtiene el clima de una ciudad específica.
     * @param ciudad La ciudad para la que se quiere obtener el clima.
     */
    fun obtenerClima(ciudad: String): Flow<ClimaApi?> = flow {
        Log.d(TAG, " Iniciando petición a API de clima para la ciudad: $ciudad")

        try {
            Log.d(TAG, " Realizando petición HTTP...")
            // Llamada a la API corregida: sin el if y con parámetros de unidades e idioma
            val response = apiService.getClima(ciudad = ciudad, apiKey = API_KEY, units = "metric", lang = "es")

            Log.d(TAG, " Respuesta recibida:")
            Log.d(TAG, "   - Código: ${response.code()}")
            Log.d(TAG, "   - Mensaje: ${response.message()}")
            Log.d(TAG, "   - Es exitosa: ${response.isSuccessful}")

            if (response.isSuccessful) {
                response.body()?.let { clima ->
                    Log.d(TAG, " Clima recibido para $ciudad: ${clima.weather.firstOrNull()?.description}, Temp: ${clima.main.temp}°C")
                    emit(clima)
                } ?: run {
                    Log.w(TAG, " Respuesta exitosa pero body es null")
                    emit(null)
                }
            } else {
                Log.e(TAG, "Error en la respuesta HTTP:")
                Log.e(TAG, "   - Código: ${response.code()}")
                Log.e(TAG, "   - Mensaje: ${response.message()}")
                Log.e(TAG, "   - Body error: ${response.errorBody()?.string()}")
                emit(null)
            }
        } catch (e: Exception) {
            Log.e(TAG, " Excepción en petición API de clima:", e)
            emit(null)
        }
    }
}
