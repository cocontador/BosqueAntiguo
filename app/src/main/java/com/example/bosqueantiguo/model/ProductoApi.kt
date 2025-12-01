package com.example.bosqueantiguo.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos para productos que vienen de la API.
 * CORREGIDO con @SerializedName en todos los campos para máxima robustez.
 */
data class ProductoApi(
    @SerializedName("id")
    val id: Long,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("descripcion")
    val descripcion: String?,
    
    @SerializedName("precio")
    val precio: Double,
    
    @SerializedName("imagenUrl")
    val imagenUrl: String?,
    
    @SerializedName("disponible")
    val disponible: Boolean,
    
    @SerializedName("stock")
    val stock: Int,
    
    @SerializedName("stockCritico")
    val stockCritico: Int,
    
    @SerializedName("categoria")
    val categoria: Categoria?
)

/**
 * Modelo para las categorías de productos.
 */
data class Categoria(
    @SerializedName("id")
    val id: Long,
    
    @SerializedName("nombre")
    val nombre: String,
    
    @SerializedName("descripcion")
    val descripcion: String? = null
)
