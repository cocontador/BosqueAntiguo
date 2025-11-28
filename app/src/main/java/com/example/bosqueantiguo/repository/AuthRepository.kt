package com.example.bosqueantiguo.repository

import android.util.Log
import com.example.bosqueantiguo.model.LoginRequest
import com.example.bosqueantiguo.model.LoginResponse
import com.example.bosqueantiguo.network.RetrofitConfig

/**
 * Repository para manejar la autenticación.
 * Encapsula la llamada a la API de login y maneja la respuesta.
 */
class AuthRepository {

    companion object {
        private const val TAG = "AuthRepository"
    }

    private val authService = RetrofitConfig.authApiService

    /**
     * Intenta iniciar sesión en el backend.
     * @param email El email del usuario.
     * @param password La contraseña del usuario.
     * @return LoginResponse si el login es exitoso, o null en caso de error.
     */
    suspend fun login(email: String, password: String): LoginResponse? {
        Log.d(TAG, "🚀 Iniciando petición de login para el usuario: $email")
        val request = LoginRequest(email, password)

        return try {
            val response = authService.login(request)
            Log.d(TAG, "📡 Respuesta recibida para login:")
            Log.d(TAG, "   - Código: ${response.code()}")
            Log.d(TAG, "   - Es exitosa: ${response.isSuccessful}")

            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    Log.d(TAG, "✅ Login exitoso para $email. Token recibido.")
                    loginResponse
                } else {
                    Log.w(TAG, "⚠️ Respuesta de login exitosa pero el body es null.")
                    null
                }
            } else {
                Log.e(TAG, "❌ Error en la respuesta HTTP de login: ${response.code()} ${response.message()}")
                Log.e(TAG, "   - Body error: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "💥 Excepción en la petición de login:", e)
            null
        }
    }
}
