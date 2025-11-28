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
    
    // URL base actual (usa emulador por defecto)
    private const val BASE_URL = BASE_URL_EMULATOR
    
    init {
        Log.d(TAG, "🔧 Inicializando RetrofitConfig")
        Log.d(TAG, "🌐 URL Base configurada: $BASE_URL")
        Log.d(TAG, "📱 Emulador: $BASE_URL_EMULATOR")
        Log.d(TAG, "📲 Dispositivo físico: $BASE_URL_DEVICE")
        Log.d(TAG, "💻 Localhost: $BASE_URL_LOCALHOST")
        Log.i(TAG, "🔓 Network Security Config habilitado para HTTP")
        Log.w(TAG, "⚠️ Si no funciona, verifica que el microservicio esté en puerto 8080")
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
    
    // Instancia de Retrofit configurada
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    // Service para productos
    val productoApiService: ProductoApiService by lazy {
        Log.d(TAG, "🏭 Creando ProductoApiService...")
        val service = retrofit.create(ProductoApiService::class.java)
        Log.d(TAG, "✅ ProductoApiService creado exitosamente")
        service
    }
}