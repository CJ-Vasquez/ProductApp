package com.cibertec.productapp.data.remote.model

/**
 * Modelo de request para enviar productos a la API
 */
data class ProductRequest(
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String? = null
)
