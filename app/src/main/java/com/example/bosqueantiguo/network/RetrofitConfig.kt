package com.example.bosqueantiguo.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitConfig {
    
    private const val TAG = "RetrofitConfig"
    
    private const val BASE_URL_PRODUCTOS_EMULATOR = "http://10.0.2.2:8080/"
    private const val BASE_URL_VENTAS_EMULATOR = "http://10.0.2.2:8081/"
    private const val BASE_URL_CLIMA = "https://api.openweathermap.org/data/2.5/"

    private const val IP_DISPOSITIVO_FISICO = "192.168.1.X"
    private const val BASE_URL_PRODUCTOS_DEVICE = "http://$IP_DISPOSITIVO_FISICO:8080/"
    private const val BASE_URL_VENTAS_DEVICE = "http://$IP_DISPOSITIVO_FISICO:8081/"

    private const val USE_DEVICE_URLS = true 

    private val BASE_URL_BACKEND = if (USE_DEVICE_URLS) BASE_URL_PRODUCTOS_DEVICE else BASE_URL_PRODUCTOS_EMULATOR
    private val BASE_URL_VENTAS = if (USE_DEVICE_URLS) BASE_URL_VENTAS_DEVICE else BASE_URL_VENTAS_EMULATOR
    
    init {
        Log.d(TAG, "Inicializando RetrofitConfig")
        Log.d(TAG, "URL Base Backend (Productos/Auth): $BASE_URL_BACKEND")
        Log.d(TAG, "URL Base Ventas: $BASE_URL_VENTAS")
        Log.d(TAG, "URL Base Clima: $BASE_URL_CLIMA")
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val retrofitBackend = Retrofit.Builder()
        .baseUrl(BASE_URL_BACKEND)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitVentas = Retrofit.Builder()
        .baseUrl(BASE_URL_VENTAS)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitClima = Retrofit.Builder()
        .baseUrl(BASE_URL_CLIMA)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val productoApiService: ProductoApiService by lazy { retrofitBackend.create(ProductoApiService::class.java) }
    val authApiService: AuthApiService by lazy { retrofitBackend.create(AuthApiService::class.java) }
    val ventaApiService: VentaApiService by lazy { retrofitVentas.create(VentaApiService::class.java) }
    val climaApiService: ClimaApiService by lazy { retrofitClima.create(ClimaApiService::class.java) }
}