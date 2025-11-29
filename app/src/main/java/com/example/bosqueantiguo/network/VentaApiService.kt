package com.example.bosqueantiguo.network

import com.example.bosqueantiguo.model.VentaRequest
import com.example.bosqueantiguo.model.VentaResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Interface que define los endpoints para el microservicio de Ventas.
 */
interface VentaApiService {

    /**
     * Registra una nueva venta en el backend.
     * @param ventaRequest El cuerpo de la petición con los detalles de la venta.
     */
    @POST("api/v1/sales")
    suspend fun registrarVenta(@Body ventaRequest: VentaRequest): Response<VentaResponse>

}