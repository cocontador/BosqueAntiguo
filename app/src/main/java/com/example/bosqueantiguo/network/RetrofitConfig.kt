package com.example.bosqueantiguo.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitConfig {
    
    private const val TAG = "RetrofitConfig"
    
    // --- Configuración de Red ---
    private const val IP_EMULADOR = "10.0.2.2"
    private const val IP_DISPOSITIVO_FISICO = "192.168.1.X"

    // --- Puertos de los Microservicios ---
    private const val PUERTO_PRODUCTOS = "8080"
    private const val PUERTO_VENTAS = "8081"
    private const val PUERTO_USUARIOS = "8082"

    // --- Configuración Activa ---
    // Cambia a false si estás usando el emulador en lugar de un dispositivo físico
    private const val USE_DEVICE_URLS = true 
    private val ACTIVE_IP = if (USE_DEVICE_URLS) IP_DISPOSITIVO_FISICO else IP_EMULADOR

    // --- URLs Base Finales ---
    private val BASE_URL_PRODUCTOS = "http://$ACTIVE_IP:$PUERTO_PRODUCTOS/"
    private val BASE_URL_VENTAS = "http://$ACTIVE_IP:$PUERTO_VENTAS/"
    private val BASE_URL_USUARIOS = "http://$ACTIVE_IP:$PUERTO_USUARIOS/"
    private const val BASE_URL_CLIMA = "https://api.openweathermap.org/data/2.5/"

    init {
        Log.d(TAG, "Inicializando RetrofitConfig")
        Log.d(TAG, "IP Activa: $ACTIVE_IP")
        Log.d(TAG, "URL Productos: $BASE_URL_PRODUCTOS")
        Log.d(TAG, "URL Usuarios/Auth: $BASE_URL_USUARIOS")
        Log.d(TAG, "URL Ventas: $BASE_URL_VENTAS")
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    
    // --- Instancias de Retrofit ---
    private fun buildRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofitProductos = buildRetrofit(BASE_URL_PRODUCTOS)
    private val retrofitVentas = buildRetrofit(BASE_URL_VENTAS)
    private val retrofitUsuarios = buildRetrofit(BASE_URL_USUARIOS)
    private val retrofitClima = buildRetrofit(BASE_URL_CLIMA)

    // --- Servicios de API ---
    val productoApiService: ProductoApiService by lazy { retrofitProductos.create(ProductoApiService::class.java) }
    val authApiService: AuthApiService by lazy { retrofitUsuarios.create(AuthApiService::class.java) }
    val ventaApiService: VentaApiService by lazy { retrofitVentas.create(VentaApiService::class.java) }
    val climaApiService: ClimaApiService by lazy { retrofitClima.create(ClimaApiService::class.java) }
}