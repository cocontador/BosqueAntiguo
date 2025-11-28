package com.example.bosqueantiguo.network

import com.example.bosqueantiguo.model.LoginRequest
import com.example.bosqueantiguo.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Interface que define los endpoints del microservicio de autenticación.
 */
interface AuthApiService {

    /**
     * Envía las credenciales del usuario para iniciar sesión.
     * @param loginRequest El cuerpo de la petición con email y password.
     */
    @POST("api/v1/auth/login") // O la ruta que hayas configurado en tu backend
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>
}
