package com.cibertec.productapp.ui.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cibertec.productapp.R
import com.cibertec.productapp.data.local.entity.ProductEntity
import com.cibertec.productapp.databinding.ActivityProductDetailBinding
import com.cibertec.productapp.ui.viewmodel.ProductViewModel
import com.cibertec.productapp.ui.viewmodel.CartViewModel


class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private val viewModel: ProductViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()

    private var currentProduct: ProductEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)



        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Manejar navegación hacia atrás
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        // Mostrar botón editar solo para admins
        // Botón editar siempre visible
        binding.btnEdit.visibility = android.view.View.VISIBLE

        val productId = intent.getLongExtra("product_id", -1)
        if (productId == -1L) {
            finish()
            return
        }

        viewModel.getProductById(productId) { product ->
            runOnUiThread {
                product?.let { showProduct(it) }
            }
        }

        binding.btnEdit.setOnClickListener {
            currentProduct?.let { p ->
                val intent = Intent(this, AddProductActivity::class.java)
                intent.putExtra("edit_product_id", p.id)
                startActivity(intent)
            }
        }

        // Botón eliminar
        binding.btnDelete.setOnClickListener {
            currentProduct?.let { product ->
                showDeleteConfirmationDialog(product)
            }
        }

        // Botón agregar al carrito
        binding.btnAddToCart.setOnClickListener {
            currentProduct?.let { product ->
                cartViewModel.addToCart(
                    productId = product.id,
                    productTitle = product.title,
                    productPrice = product.price,
                    productImage = product.image
                )
            }
        }

        // Observadores
        viewModel.statusMessage.observe(this) { msg ->
            msg?.let { android.widget.Toast.makeText(this, it, android.widget.Toast.LENGTH_SHORT).show() }
        }

        cartViewModel.statusMessage.observe(this) { msg ->
            msg?.let { android.widget.Toast.makeText(this, it, android.widget.Toast.LENGTH_SHORT).show() }
        }
    }

    private fun showProduct(product: ProductEntity) {
        currentProduct = product
        binding.tvTitle.text = product.title
        binding.tvPrice.text = getString(R.string.price_format, product.price)
        binding.tvCategory.text = product.category
        binding.tvDescription.text = product.description

        if (!product.image.isNullOrEmpty()) {
            Glide.with(this)
                .load(product.image)
                .placeholder(R.drawable.ic_store_main)
                .error(R.drawable.ic_store_main)
                .into(binding.ivProduct)
        } else {
            binding.ivProduct.setImageResource(R.drawable.ic_store_main)
        }
    }

    private fun showDeleteConfirmationDialog(product: ProductEntity) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirm_delete_title))
            .setMessage(getString(R.string.confirm_delete_message))
            .setPositiveButton(getString(R.string.btn_delete_confirm)) { _, _ ->
                viewModel.deleteProduct(product)
                // Cerrar la actividad después de eliminar
                finish()
            }
            .setNegativeButton(getString(R.string.btn_cancel), null)
            .show()
    }
}
