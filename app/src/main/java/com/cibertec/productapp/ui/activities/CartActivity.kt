package com.cibertec.productapp.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cibertec.productapp.R
import com.cibertec.productapp.databinding.ActivityCartBinding
import com.cibertec.productapp.ui.adapter.CartAdapter
import com.cibertec.productapp.ui.viewmodel.CartViewModel

/**
 * Activity para mostrar y gestionar el carrito de compras
 */
class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private val cartViewModel: CartViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            onQuantityChanged = { cartItem, newQuantity ->
                cartViewModel.updateCartItemQuantity(cartItem, newQuantity)
            },
            onRemoveClick = { cartItem ->
                cartViewModel.removeFromCart(cartItem)
            }
        )

        binding.rvCartItems.apply {
            layoutManager = LinearLayoutManager(this@CartActivity)
            adapter = cartAdapter
        }
    }

    private fun setupObservers() {
        // Observar items del carrito
        cartViewModel.cartItems.observe(this) { items ->
            cartAdapter.submitList(items)
            updateUI(items.isEmpty())
        }

        // Observar total del carrito
        cartViewModel.cartTotal.observe(this) { total ->
            val finalTotal = total ?: 0.0
            binding.tvSubtotal.text = getString(R.string.price_format, finalTotal)
            binding.tvTotal.text = getString(R.string.price_format, finalTotal)
        }

        // Observar mensajes de estado
        cartViewModel.statusMessage.observe(this) { message ->
            message?.let {
                android.widget.Toast.makeText(this, it, android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupListeners() {
        // Botón proceder al checkout
        binding.btnProceedToCheckout.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)
            startActivity(intent)
        }

        // Botón limpiar carrito
        binding.btnClearCart.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(getString(R.string.clear_cart_title))
                .setMessage(getString(R.string.clear_cart_message))
                .setPositiveButton(getString(R.string.btn_clear)) { _, _ ->
                    cartViewModel.clearCart()
                }
                .setNegativeButton(getString(R.string.btn_cancel), null)
                .show()
        }
    }

    private fun updateUI(isEmpty: Boolean) {
        if (isEmpty) {
            binding.layoutEmptyCart.visibility = android.view.View.VISIBLE
            binding.layoutCartContent.visibility = android.view.View.GONE
        } else {
            binding.layoutEmptyCart.visibility = android.view.View.GONE
            binding.layoutCartContent.visibility = android.view.View.VISIBLE
        }
    }
}
