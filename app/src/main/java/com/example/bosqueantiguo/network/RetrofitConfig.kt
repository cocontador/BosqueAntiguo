package com.example.bosqueantiguo.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Configuración centralizada de Retrofit
 * Aquí se configura el cliente HTTP y la URL base de los microservicios
 */
object RetrofitConfig {
    
    private const val TAG = "RetrofitConfig"
    
    // URLs posibles para el microservicio de productos
    private const val BASE_URL_EMULATOR = "http://10.0.2.2:8080/"  // Para emulador
    private const val BASE_URL_DEVICE = "http://192.168.1.100:8080/"  // Para dispositivo físico (cambiar por tu IP local)
    private const val BASE_URL_LOCALHOST = "http://localhost:8080/"  // Para pruebas
    
    // URL base actual para productos (usa emulador por defecto)
    private const val BASE_URL_PRODUCTOS = BASE_URL_EMULATOR

    // URL base para el servicio de clima (ej: OpenWeatherMap)
    private const val BASE_URL_CLIMA = "https://api.openweathermap.org/data/2.5/"
    
    init {
        Log.d(TAG, "🔧 Inicializando RetrofitConfig")
        Log.d(TAG, "🌐 URL Base Productos: $BASE_URL_PRODUCTOS")
        Log.d(TAG, "🌦️ URL Base Clima: $BASE_URL_CLIMA")
        Log.i(TAG, "🔓 Network Security Config habilitado para HTTP")
        Log.w(TAG, "⚠️ Si no funciona, verifica que el microservicio de productos esté en puerto 8080")
    }
    
    // Cliente HTTP con configuración de logging para debug
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
                Log.d(TAG, "📝 HttpLoggingInterceptor configurado con nivel BODY")
            }
        )
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val request = chain.request()
            Log.d(TAG, "🚀 Petición saliente:")
            Log.d(TAG, "   - URL: ${request.url}")
            Log.d(TAG, "   - Método: ${request.method}")
            Log.d(TAG, "   - Headers: ${request.headers}")
            
            val response = chain.proceed(request)
            Log.d(TAG, "📥 Respuesta recibida:")
            Log.d(TAG, "   - Código: ${response.code}")
            Log.d(TAG, "   - Mensaje: ${response.message}")
            Log.d(TAG, "   - URL: ${response.request.url}")
            
            response
        }
        .build()
    
    // Instancia de Retrofit para Productos
    private val retrofitProductos = Retrofit.Builder()
        .baseUrl(BASE_URL_PRODUCTOS)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Instancia de Retrofit para Clima
    private val retrofitClima = Retrofit.Builder()
        .baseUrl(BASE_URL_CLIMA)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    // Service para productos
    val productoApiService: ProductoApiService by lazy {
        Log.d(TAG, "🏭 Creando ProductoApiService...")
        val service = retrofitProductos.create(ProductoApiService::class.java)
        Log.d(TAG, "✅ ProductoApiService creado exitosamente")
        service
    }

    // Service para clima
    val climaApiService: ClimaApiService by lazy {
        Log.d(TAG, "🏭 Creando ClimaApiService...")
        val service = retrofitClima.create(ClimaApiService::class.java)
        Log.d(TAG, "✅ ClimaApiService creado exitosamente")
        service
    }
}
