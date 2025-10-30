package com.cibertec.productapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cibertec.productapp.data.local.entity.CartItemEntity

/**
 * DAO para operaciones del carrito de compras
 */
@Dao
interface CartDao {

    @Query("SELECT * FROM cart_items ORDER BY addedDate DESC")
    fun getAllCartItems(): LiveData<List<CartItemEntity>>

    @Query("SELECT * FROM cart_items WHERE productId = :productId LIMIT 1")
    suspend fun getCartItemByProductId(productId: Long): CartItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateCartItem(cartItem: CartItemEntity): Long

    @Update
    suspend fun updateCartItem(cartItem: CartItemEntity)

    @Delete
    suspend fun deleteCartItem(cartItem: CartItemEntity)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    @Query("SELECT COUNT(*) FROM cart_items")
    fun getCartItemCount(): LiveData<Int>

    @Query("SELECT SUM(productPrice * quantity) FROM cart_items")
    fun getCartTotal(): LiveData<Double?>
}
