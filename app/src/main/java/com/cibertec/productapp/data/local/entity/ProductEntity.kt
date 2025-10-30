package com.cibertec.productapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de producto para la base de datos Room
 * Representa un producto almacenado localmente
 */
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val apiId: Int? = null, // ID del producto en la API externa
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String? = null,
    val isSynced: Boolean = false // Indica si está sincronizado con la API
)
