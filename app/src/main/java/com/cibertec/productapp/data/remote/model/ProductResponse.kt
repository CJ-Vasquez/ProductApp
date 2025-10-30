package com.cibertec.productapp.data.remote.model

/**
 * Modelo de respuesta para productos desde la API FakeStore
 */
data class ProductResponse(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating? = null
)

/**
 * Modelo para el rating de productos
 */
data class Rating(
    val rate: Double,
    val count: Int
)
