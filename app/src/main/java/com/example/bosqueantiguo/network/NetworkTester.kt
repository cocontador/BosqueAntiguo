package com.example.bosqueantiguo.network

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

/**
 * Utilidad para probar conectividad con diferentes URLs
 * Ayuda a debuggear problemas de conexión
 */
object NetworkTester {
    
    private const val TAG = "NetworkTester"
    
    fun probarConectividad() {
        Log.d(TAG, "🔍 Iniciando pruebas de conectividad...")
        Log.i(TAG, "📋 INSTRUCCIONES:")
        Log.i(TAG, "   1. Asegúrate de que tu microservicio Spring Boot esté ejecutándose")
        Log.i(TAG, "   2. El microservicio debe estar en puerto 8080")
        Log.i(TAG, "   3. Abre http://localhost:8080/api/v1/products en tu navegador")
        Log.i(TAG, "   4. Debe devolver JSON con productos")
        
        val urls = listOf(
            "http://10.0.2.2:8080/api/v1/products",
            "http://localhost:8080/api/v1/products", 
            "http://127.0.0.1:8080/api/v1/products",
            "http://google.com" // Para verificar conexión a internet
        )
        
        val client = OkHttpClient()
        
        CoroutineScope(Dispatchers.IO).launch {
            urls.forEach { url ->
                try {
                    Log.d(TAG, "🌐 Probando: $url")
                    val request = Request.Builder().url(url).build()
                    
                    client.newCall(request).execute().use { response ->
                        Log.d(TAG, "📡 Resultado para $url:")
                        Log.d(TAG, "   - Código: ${response.code}")
                        Log.d(TAG, "   - Mensaje: ${response.message}")
                        Log.d(TAG, "   - Es exitosa: ${response.isSuccessful}")
                        
                        if (response.isSuccessful) {
                            val body = response.body?.string()?.take(200) // Primeros 200 caracteres
                            Log.d(TAG, "   - Body preview: $body...")
                        } else {
                            Log.w(TAG, "   - Error: ${response.code} ${response.message}")
                        }
                    }
                    
                } catch (e: IOException) {
                    Log.e(TAG, "❌ Error conectando a $url: ${e.message}")
                } catch (e: Exception) {
                    Log.e(TAG, "💥 Excepción con $url:", e)
                }
            }
        }
    }
}