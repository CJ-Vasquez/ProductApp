package com.cibertec.productapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cibertec.productapp.R
import com.cibertec.productapp.data.local.entity.CartItemEntity
import com.cibertec.productapp.data.local.entity.OrderEntity
import com.cibertec.productapp.data.repository.CartRepository
import kotlinx.coroutines.launch

/**
 * ViewModel para operaciones del carrito de compras
 */
class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CartRepository(application.applicationContext)
    private val context = application.applicationContext

    // LiveData observables
    val cartItems: LiveData<List<CartItemEntity>> = repository.getAllCartItems()
    val cartItemCount: LiveData<Int> = repository.getCartItemCount()
    val cartTotal: LiveData<Double?> = repository.getCartTotal()
    val orders: LiveData<List<OrderEntity>> = repository.getAllOrders()

    private val _statusMessage = MutableLiveData<String?>()
    val statusMessage: LiveData<String?> = _statusMessage

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    private val _orderCreated = MutableLiveData<Long?>()
    val orderCreated: LiveData<Long?> = _orderCreated

    /**
     * Añadir producto al carrito
     */
    fun addToCart(productId: Long, productTitle: String, productPrice: Double, productImage: String?) {
        viewModelScope.launch {
            try {
                repository.addToCart(productId, productTitle, productPrice, productImage)
                _statusMessage.value = context.getString(R.string.added_to_cart)
            } catch (e: Exception) {
                _statusMessage.value = context.getString(R.string.error_adding_to_cart, e.message)
            }
        }
    }

    /**
     * Actualizar cantidad de un item del carrito
     */
    fun updateCartItemQuantity(cartItem: CartItemEntity, newQuantity: Int) {
        viewModelScope.launch {
            try {
                repository.updateCartItemQuantity(cartItem, newQuantity)
                if (newQuantity <= 0) {
                    _statusMessage.value = context.getString(R.string.removed_from_cart)
                }
            } catch (e: Exception) {
                _statusMessage.value = context.getString(R.string.error_updating_cart, e.message)
            }
        }
    }

    /**
     * Remover item del carrito
     */
    fun removeFromCart(cartItem: CartItemEntity) {
        viewModelScope.launch {
            try {
                repository.removeFromCart(cartItem)
                _statusMessage.value = context.getString(R.string.removed_from_cart)
            } catch (e: Exception) {
                _statusMessage.value = context.getString(R.string.error_removing_from_cart, e.message)
            }
        }
    }

    /**
     * Limpiar todo el carrito
     */
    fun clearCart() {
        viewModelScope.launch {
            try {
                repository.clearCart()
                _statusMessage.value = context.getString(R.string.cart_cleared)
            } catch (e: Exception) {
                _statusMessage.value = context.getString(R.string.error_clearing_cart, e.message)
            }
        }
    }

    /**
     * Crear orden de compra
     */
    fun createOrder(
        customerName: String,
        customerEmail: String,
        customerPhone: String,
        shippingAddress: String,
        paymentMethod: String,
        subtotal: Double
    ) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val tax = subtotal * 0.18 // 18% IGV
                val total = subtotal + tax

                val orderId = repository.createOrder(
                    customerName = customerName,
                    customerEmail = customerEmail,
                    customerPhone = customerPhone,
                    shippingAddress = shippingAddress,
                    paymentMethod = paymentMethod,
                    subtotal = subtotal,
                    tax = tax,
                    total = total
                )

                // Limpiar carrito después de crear la orden
                repository.clearCart()

                _orderCreated.value = orderId
                _statusMessage.value = context.getString(R.string.order_created_successfully)
            } catch (e: Exception) {
                _statusMessage.value = context.getString(R.string.error_creating_order, e.message)
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Obtener orden por ID
     */
    fun getOrderById(orderId: Long, callback: (OrderEntity?) -> Unit) {
        viewModelScope.launch {
            try {
                val order = repository.getOrderById(orderId)
                callback(order)
            } catch (e: Exception) {
                _statusMessage.value = context.getString(R.string.error_loading_order, e.message)
                callback(null)
            }
        }
    }

    /**
     * Limpiar mensaje de estado
     */
    fun clearStatusMessage() {
        _statusMessage.value = null
    }

    /**
     * Limpiar orden creada
     */
    fun clearOrderCreated() {
        _orderCreated.value = null
    }
}
