package com.example.bosqueantiguo.model

import com.google.gson.annotations.SerializedName

/**
 * Representa la respuesta de la API del clima.
 */
data class ClimaApi(
    @SerializedName("weather")
    val weather: List<Weather>,
    @SerializedName("main")
    val main: Main
)

data class Weather(
    @SerializedName("description")
    val description: String
)

data class Main(
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("humidity")
    val humidity: Int
)
