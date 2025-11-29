package com.example.bosqueantiguo.model

import com.google.gson.annotations.SerializedName

/**
 * Representa un item dentro del carrito de compras.
 */
data class CarritoItem(
    val producto: ProductoApi,
    val cantidad: Int
)

// --- Modelos para la API de Ventas ---

/**
 * Detalle de la venta para el request.
 */
data class VentaDetalleRequest(
    val productoId: Long,
    val cantidad: Int
)

/**
 * Request para registrar una nueva venta.
 */
data class VentaRequest(
    val detalles: List<VentaDetalleRequest>
)

/**
 * Detalle de la venta en la respuesta.
 */
data class VentaDetalleResponse(
    val id: Long,
    val productoId: Long,
    val cantidad: Int,
    val subtotal: Double
)

/**
 * Respuesta completa al registrar una nueva venta.
 */
data class VentaResponse(
    val id: Long,
    val fecha: String,
    val total: Double,
    @SerializedName("usuarioId") // Asumiendo que el campo puede llamarse así en el JSON
    val usuarioId: Long?,
    val detalles: List<VentaDetalleResponse>
)
