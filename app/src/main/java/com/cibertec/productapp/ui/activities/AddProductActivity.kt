package com.cibertec.productapp.ui.activities

import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.cibertec.productapp.R
import com.cibertec.productapp.data.local.entity.ProductEntity
import com.cibertec.productapp.databinding.ActivityAddProductBinding
import com.cibertec.productapp.ui.viewmodel.ProductViewModel

class AddProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductBinding
    private val viewModel: ProductViewModel by viewModels()
    private var editProductId: Long? = null
    private var editingProduct: ProductEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Manejar navegación hacia atrás
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        editProductId = intent.getLongExtra("edit_product_id", -1).takeIf { it != -1L }
        editProductId?.let { id ->
            // cargar producto y prellenar campos
            viewModel.getProductById(id) { product ->
                product?.let { p ->
                    runOnUiThread {
                        editingProduct = p
                        binding.etTitle.setText(p.title)
                        binding.etPrice.setText(p.price.toString())
                        binding.etCategory.setText(p.category)
                        binding.etDescription.setText(p.description)
                        binding.switchSendApi.isChecked = p.isSynced
                    }
                }
            }
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text?.toString()?.trim() ?: ""
            val priceStr = binding.etPrice.text?.toString()?.trim() ?: ""
            val category = binding.etCategory.text?.toString()?.trim() ?: ""
            val description = binding.etDescription.text?.toString()?.trim() ?: ""
            val sendToApi = binding.switchSendApi.isChecked

            if (TextUtils.isEmpty(title)) {
                binding.tilTitle.error = getString(R.string.error_title_required)
                return@setOnClickListener
            } else {
                binding.tilTitle.error = null
            }

            val price = try {
                priceStr.toDouble()
            } catch (e: Exception) {
                binding.tilPrice.error = getString(R.string.error_price_invalid)
                return@setOnClickListener
            }

            if (price <= 0.0) {
                binding.tilPrice.error = getString(R.string.error_price_greater_zero)
                return@setOnClickListener
            } else {
                binding.tilPrice.error = null
            }

            if (editingProduct != null) {
                // actualizar
                val updated = editingProduct!!.copy(
                    title = title,
                    price = price,
                    description = description,
                    category = if (category.isEmpty()) "" else category,
                    isSynced = editingProduct!!.isSynced || sendToApi
                )
                viewModel.updateProduct(updated, updateOnApi = sendToApi && updated.apiId != null)
                android.widget.Toast.makeText(this, getString(R.string.product_updated), android.widget.Toast.LENGTH_SHORT).show()
            } else {
                val product = ProductEntity(
                    title = title,
                    price = price,
                    description = description,
                    category = if (category.isEmpty()) "" else category,
                    image = null,
                    isSynced = false
                )

                viewModel.addProductLocal(product, sendToApi)
                android.widget.Toast.makeText(this, getString(R.string.product_saved), android.widget.Toast.LENGTH_SHORT).show()
            }

            finish()
        }

        // Observa mensajes de estado para mostrar al usuario
        viewModel.statusMessage.observe(this) { msg ->
            msg?.let { android.widget.Toast.makeText(this, it, android.widget.Toast.LENGTH_SHORT).show() }
        }
    }
}
