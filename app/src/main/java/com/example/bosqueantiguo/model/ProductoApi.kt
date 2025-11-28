package com.example.bosqueantiguo.model

/**
 * Modelo de datos para productos que vienen de la API
 * Representa la estructura JSON del microservicio de productos
 */
data class ProductoApi(
    val id: Long,
    val codigo: String? = null,  // Puede ser null
    val nombre: String,
    val descripcion: String? = null,  // Puede ser null
    val precio: Double,  // Changed from Int to Double to match API
    val stock: Int,
    val categoria: Categoria? = null  // Puede ser null
)

/**
 * Modelo para las categorías de productos
 */
data class Categoria(
    val id: Long,
    val nombre: String,
    val descripcion: String? = null
)