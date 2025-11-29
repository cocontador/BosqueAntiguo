package com.example.bosqueantiguo.network

import com.example.bosqueantiguo.model.ClimaApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface que define los endpoints del microservicio de clima
 * Retrofit genera automáticamente la implementación
 */
interface ClimaApiService {

    /**
     * Obtiene el clima de una ciudad
     * @param ciudad La ciudad para la que se quiere obtener el clima
     * @param apiKey Tu API Key de OpenWeatherMap
     * @param units Unidades de medida (metric para Celsius)
     * @param lang Idioma de la respuesta
     */
    @GET("weather")
    suspend fun getClima(
        @Query("q") ciudad: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es"
    ): Response<ClimaApi>
}