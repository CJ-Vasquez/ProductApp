package com.cibertec.productapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cibertec.productapp.R
import com.cibertec.productapp.databinding.ActivityCheckoutBinding
import com.cibertec.productapp.ui.viewmodel.CartViewModel

/**
 * Activity para el proceso de checkout/pago
 */
class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private val cartViewModel: CartViewModel by viewModels()
    private var cartTotal: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupPaymentMethods()
        setupObservers()
        setupListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupPaymentMethods() {
        // Configurar RadioGroup con métodos de pago
        binding.rbCash.text = getString(R.string.payment_cash)
        binding.rbCard.text = getString(R.string.payment_card)
        binding.rbTransfer.text = getString(R.string.payment_transfer)

        // Seleccionar efectivo por defecto
        binding.rbCash.isChecked = true
    }

    private fun setupObservers() {
        // Observar total del carrito
        cartViewModel.cartTotal.observe(this) { total ->
            cartTotal = total ?: 0.0
            updateOrderSummary()
        }

        // Observar cuando se crea la orden
        cartViewModel.orderCreated.observe(this) { orderId ->
            orderId?.let {
                // Ir a la pantalla de recibo
                val intent = Intent(this, OrderReceiptActivity::class.java)
                intent.putExtra("order_id", it)
                startActivity(intent)
                finish()
            }
        }

        // Observar mensajes de estado
        cartViewModel.statusMessage.observe(this) { message ->
            message?.let {
                android.widget.Toast.makeText(this, it, android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        // Observar estado de carga
        cartViewModel.loading.observe(this) { isLoading ->
            binding.btnPlaceOrder.isEnabled = !isLoading
            binding.btnPlaceOrder.text = if (isLoading) {
                "Procesando..."
            } else {
                getString(R.string.btn_place_order)
            }
        }
    }

    private fun setupListeners() {
        binding.btnPlaceOrder.setOnClickListener {
            if (validateForm()) {
                processOrder()
            }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        // Validar nombre
        val name = binding.etCustomerName.text?.toString()?.trim()
        if (TextUtils.isEmpty(name)) {
            binding.tilCustomerName.error = "Nombre requerido"
            isValid = false
        } else {
            binding.tilCustomerName.error = null
        }

        // Validar email
        val email = binding.etCustomerEmail.text?.toString()?.trim()
        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email!!).matches()) {
            binding.tilCustomerEmail.error = "Email válido requerido"
            isValid = false
        } else {
            binding.tilCustomerEmail.error = null
        }

        // Validar teléfono
        val phone = binding.etCustomerPhone.text?.toString()?.trim()
        if (TextUtils.isEmpty(phone)) {
            binding.tilCustomerPhone.error = "Teléfono requerido"
            isValid = false
        } else {
            binding.tilCustomerPhone.error = null
        }

        // Validar dirección
        val address = binding.etShippingAddress.text?.toString()?.trim()
        if (TextUtils.isEmpty(address)) {
            binding.tilShippingAddress.error = "Dirección requerida"
            isValid = false
        } else {
            binding.tilShippingAddress.error = null
        }

        // Validar método de pago
        if (binding.rgPaymentMethods.checkedRadioButtonId == -1) {
            android.widget.Toast.makeText(this, "Selecciona un método de pago", android.widget.Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    private fun processOrder() {
        val name = binding.etCustomerName.text.toString().trim()
        val email = binding.etCustomerEmail.text.toString().trim()
        val phone = binding.etCustomerPhone.text.toString().trim()
        val address = binding.etShippingAddress.text.toString().trim()

        val paymentMethod = when (binding.rgPaymentMethods.checkedRadioButtonId) {
            R.id.rbCash -> getString(R.string.payment_cash)
            R.id.rbCard -> getString(R.string.payment_card)
            R.id.rbTransfer -> getString(R.string.payment_transfer)
            else -> getString(R.string.payment_cash)
        }

        cartViewModel.createOrder(
            customerName = name,
            customerEmail = email,
            customerPhone = phone,
            shippingAddress = address,
            paymentMethod = paymentMethod,
            subtotal = cartTotal
        )
    }

    private fun updateOrderSummary() {
        val tax = cartTotal * 0.18
        val total = cartTotal + tax

        binding.tvOrderSubtotal.text = getString(R.string.price_format, cartTotal)
        binding.tvOrderTax.text = getString(R.string.price_format, tax)
        binding.tvOrderTotal.text = getString(R.string.price_format, total)
    }
}
