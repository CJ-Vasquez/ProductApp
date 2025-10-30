package com.cibertec.productapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad para items del carrito de compras
 */
@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productId: Long,
    val productTitle: String,
    val productPrice: Double,
    val productImage: String?,
    val quantity: Int = 1,
    val addedDate: Long = System.currentTimeMillis()
)
