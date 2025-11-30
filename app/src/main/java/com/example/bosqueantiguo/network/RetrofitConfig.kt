package com.example.bosqueantiguo.network

import android.content.Context
import android.util.Log
import com.example.bosqueantiguo.BosqueAntiguoApp
import com.example.bosqueantiguo.datastore.TokenManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitConfig {
    
    private const val TAG = "RetrofitConfig"
    
    // --- Configuración de Red ---
    private const val IP_EMULADOR = "10.0.2.2"
    private const val IP_DISPOSITIVO_FISICO = "192.168.1.4"

    // --- Puertos de los Microservicios ---
    private const val PUERTO_PRODUCTOS = "8080"
    private const val PUERTO_VENTAS = "8081"
    private const val PUERTO_USUARIOS = "8082"

    // --- Configuración Activa ---
    private const val USE_DEVICE_URLS = true 
    private val ACTIVE_IP = if (USE_DEVICE_URLS) IP_DISPOSITIVO_FISICO else IP_EMULADOR

    // --- URLs Base Finales ---
    private val BASE_URL_PRODUCTOS = "http://$ACTIVE_IP:$PUERTO_PRODUCTOS/"
    private val BASE_URL_VENTAS = "http://$ACTIVE_IP:$PUERTO_VENTAS/"
    private val BASE_URL_USUARIOS = "http://$ACTIVE_IP:$PUERTO_USUARIOS/"
    private const val BASE_URL_CLIMA = "https://api.openweathermap.org/data/2.5/"

    // Se necesita el contexto para inicializar el TokenManager, que a su vez es necesario para el AuthInterceptor.
    // La única forma segura de obtenerlo es a través de un método init que se llame desde la clase Application.
    private lateinit var tokenManager: TokenManager
    private lateinit var okHttpClient: OkHttpClient

    fun init(context: Context) {
        tokenManager = (context.applicationContext as BosqueAntiguoApp).tokenManager
        
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        Log.d(TAG, "RetrofitConfig inicializado.")
    }

    private fun buildRetrofit(baseUrl: String): Retrofit {
        if (!this::okHttpClient.isInitialized) {
            throw IllegalStateException("RetrofitConfig no ha sido inicializado. Llama a init() desde tu clase Application.")
        }
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // --- Servicios de API ---
    val productoApiService: ProductoApiService by lazy { buildRetrofit(BASE_URL_PRODUCTOS).create(ProductoApiService::class.java) }
    val categoriaApiService: CategoriaApiService by lazy { buildRetrofit(BASE_URL_PRODUCTOS).create(CategoriaApiService::class.java) }
    val authApiService: AuthApiService by lazy { buildRetrofit(BASE_URL_USUARIOS).create(AuthApiService::class.java) }
    val ventaApiService: VentaApiService by lazy { buildRetrofit(BASE_URL_VENTAS).create(VentaApiService::class.java) }
    val climaApiService: ClimaApiService by lazy { buildRetrofit(BASE_URL_CLIMA).create(ClimaApiService::class.java) }
}
