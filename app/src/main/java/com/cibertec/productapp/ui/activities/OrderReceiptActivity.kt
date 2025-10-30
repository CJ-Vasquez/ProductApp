package com.cibertec.productapp.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cibertec.productapp.R
import com.cibertec.productapp.data.local.entity.OrderEntity
import com.cibertec.productapp.databinding.ActivityOrderReceiptBinding
import com.cibertec.productapp.ui.viewmodel.CartViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Activity para mostrar el recibo detallado de la compra
 */
class OrderReceiptActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderReceiptBinding
    private val cartViewModel: CartViewModel by viewModels()
    private var orderId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        getOrderData()
        setupListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun getOrderData() {
        orderId = intent.getLongExtra("order_id", -1)

        if (orderId != -1L) {
            cartViewModel.getOrderById(orderId) { order ->
                if (order != null) {
                    runOnUiThread {
                        displayOrderDetails(order)
                    }
                } else {
                    runOnUiThread {
                        android.widget.Toast.makeText(this, "Error al cargar el recibo", android.widget.Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        } else {
            android.widget.Toast.makeText(this, "ID de orden inválido", android.widget.Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupListeners() {
        binding.btnBackToHome.setOnClickListener {
            // Volver al inicio y limpiar stack
            finishAffinity()
            val intent = android.content.Intent(this, com.cibertec.productapp.MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnNewOrder.setOnClickListener {
            // Ir al carrito para nueva compra
            finish()
            val intent = android.content.Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }

    private fun displayOrderDetails(order: OrderEntity) {
        // Información del pedido
        binding.tvOrderNumber.text = order.orderNumber
        binding.tvOrderDate.text = formatDate(order.orderDate)
        binding.tvOrderStatus.text = order.status

        // Información del cliente
        binding.tvCustomerName.text = order.customerName
        binding.tvCustomerEmail.text = order.customerEmail
        binding.tvCustomerPhone.text = order.customerPhone

        // Información de envío
        binding.tvShippingAddress.text = order.shippingAddress

        // Información de pago
        binding.tvPaymentMethod.text = order.paymentMethod

        // Totales
        binding.tvReceiptSubtotal.text = getString(R.string.price_format, order.subtotal)
        binding.tvReceiptTax.text = getString(R.string.price_format, order.tax)
        binding.tvReceiptTotal.text = getString(R.string.price_format, order.total)

        // Mostrar mensaje de éxito
        showSuccessAnimation()
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    private fun showSuccessAnimation() {
        // Animación simple de éxito
        binding.ivSuccessIcon.apply {
            alpha = 0f
            scaleX = 0.5f
            scaleY = 0.5f
            animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(600)
                .start()
        }

        binding.tvSuccessMessage.apply {
            alpha = 0f
            translationY = 50f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(600)
                .setStartDelay(200)
                .start()
        }
    }
}
