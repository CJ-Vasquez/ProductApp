package com.cibertec.productapp.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.cibertec.productapp.data.local.database.AppDatabase
import com.cibertec.productapp.data.local.entity.CartItemEntity
import com.cibertec.productapp.data.local.entity.OrderEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio para operaciones del carrito y órdenes
 */
class CartRepository(context: Context) {

    private val database = AppDatabase.getDatabase(context)
    private val cartDao = database.cartDao()
    private val orderDao = database.orderDao()

    // ===== CART OPERATIONS =====

    fun getAllCartItems(): LiveData<List<CartItemEntity>> = cartDao.getAllCartItems()

    fun getCartItemCount(): LiveData<Int> = cartDao.getCartItemCount()

    fun getCartTotal(): LiveData<Double?> = cartDao.getCartTotal()

    suspend fun addToCart(productId: Long, productTitle: String, productPrice: Double, productImage: String?) = withContext(Dispatchers.IO) {
        val existingItem = cartDao.getCartItemByProductId(productId)

        if (existingItem != null) {
            // Si ya existe, incrementar cantidad
            val updatedItem = existingItem.copy(quantity = existingItem.quantity + 1)
            cartDao.updateCartItem(updatedItem)
        } else {
            // Si no existe, crear nuevo item
            val newItem = CartItemEntity(
                productId = productId,
                productTitle = productTitle,
                productPrice = productPrice,
                productImage = productImage
            )
            cartDao.insertOrUpdateCartItem(newItem)
        }
    }

    suspend fun updateCartItemQuantity(cartItem: CartItemEntity, newQuantity: Int) = withContext(Dispatchers.IO) {
        if (newQuantity <= 0) {
            cartDao.deleteCartItem(cartItem)
        } else {
            val updatedItem = cartItem.copy(quantity = newQuantity)
            cartDao.updateCartItem(updatedItem)
        }
    }

    suspend fun removeFromCart(cartItem: CartItemEntity) = withContext(Dispatchers.IO) {
        cartDao.deleteCartItem(cartItem)
    }

    suspend fun clearCart() = withContext(Dispatchers.IO) {
        cartDao.clearCart()
    }

    // ===== ORDER OPERATIONS =====

    fun getAllOrders(): LiveData<List<OrderEntity>> = orderDao.getAllOrders()

    suspend fun createOrder(
        customerName: String,
        customerEmail: String,
        customerPhone: String,
        shippingAddress: String,
        paymentMethod: String,
        subtotal: Double,
        tax: Double,
        total: Double
    ): Long = withContext(Dispatchers.IO) {
        val orderNumber = "ORD-${System.currentTimeMillis()}"
        val order = OrderEntity(
            orderNumber = orderNumber,
            customerName = customerName,
            customerEmail = customerEmail,
            customerPhone = customerPhone,
            shippingAddress = shippingAddress,
            paymentMethod = paymentMethod,
            subtotal = subtotal,
            tax = tax,
            total = total
        )
        orderDao.insertOrder(order)
    }

    suspend fun getOrderById(orderId: Long): OrderEntity? = withContext(Dispatchers.IO) {
        orderDao.getOrderById(orderId)
    }
}
