package com.example.bosqueantiguo.network

import com.example.bosqueantiguo.model.ProductoApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface que define los endpoints del microservicio de productos
 * Retrofit genera automáticamente la implementación
 */
interface ProductoApiService {
    
    /**
     * Obtiene todos los productos o solo los disponibles
     * @param disponibles si es true, solo trae productos con stock > 0
     */
    @GET("api/v1/products")
    suspend fun getProductos(
        @Query("disponibles") disponibles: Boolean? = null
    ): Response<List<ProductoApi>>
    
    /**
     * Obtiene un producto específico por ID
     */
    @GET("api/v1/products/{id}")
    suspend fun getProducto(
        @Path("id") id: Long
    ): Response<ProductoApi>
}