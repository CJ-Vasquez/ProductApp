package com.cibertec.productapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cibertec.productapp.data.local.entity.OrderEntity

/**
 * DAO para operaciones de órdenes
 */
@Dao
interface OrderDao {

    @Query("SELECT * FROM orders ORDER BY orderDate DESC")
    fun getAllOrders(): LiveData<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrderById(orderId: Long): OrderEntity?

    @Insert
    suspend fun insertOrder(order: OrderEntity): Long

    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Delete
    suspend fun deleteOrder(order: OrderEntity)
}
