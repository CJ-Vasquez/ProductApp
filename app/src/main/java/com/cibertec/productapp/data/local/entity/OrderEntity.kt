package com.cibertec.productapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad para órdenes de compra
 */
@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderNumber: String,
    val customerName: String,
    val customerEmail: String,
    val customerPhone: String,
    val shippingAddress: String,
    val paymentMethod: String,
    val subtotal: Double,
    val tax: Double,
    val total: Double,
    val orderDate: Long = System.currentTimeMillis(),
    val status: String = "Pendiente"
)
