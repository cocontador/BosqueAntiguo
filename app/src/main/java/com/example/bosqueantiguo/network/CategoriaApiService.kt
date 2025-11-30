package com.example.bosqueantiguo.network

import com.example.bosqueantiguo.model.Categoria
import retrofit2.Response
import retrofit2.http.GET

/**
 * Interface que define los endpoints para el microservicio de Categorías.
 */
interface CategoriaApiService {

    /**
     * Obtiene una lista de todas las categorías.
     */
    @GET("api/v1/categories") // CORREGIDO para coincidir con tu CategoriaController.java
    suspend fun getCategorias(): Response<List<Categoria>>

}
