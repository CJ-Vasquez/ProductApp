package com.cibertec.productapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cibertec.productapp.R
import com.cibertec.productapp.databinding.ActivityProductListBinding
import com.cibertec.productapp.data.local.entity.ProductEntity
import com.cibertec.productapp.ui.adapter.ProductAdapter
import com.cibertec.productapp.ui.viewmodel.ProductViewModel
import com.cibertec.productapp.ui.viewmodel.CartViewModel

class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding
    private val viewModel: ProductViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Manejar navegación hacia atrás
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        adapter = ProductAdapter(
            items = emptyList(),
            onItemClick = { product ->
                // Abrir detalle
                val intent = Intent(this, ProductDetailActivity::class.java)
                intent.putExtra("product_id", product.id)
                startActivity(intent)
            },
            onAddToCartClick = { product ->
                // Agregar al carrito usando CartViewModel
                cartViewModel.addToCart(
                    productId = product.id,
                    productTitle = product.title,
                    productPrice = product.price,
                    productImage = product.image
                )
            },
            onDeleteClick = { product ->
                // Mostrar diálogo de confirmación
                showDeleteConfirmationDialog(product)
            }
        )

        binding.rvProducts.layoutManager = LinearLayoutManager(this)
        binding.rvProducts.adapter = adapter

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivity(intent)
        }

        // Observamos la lista de productos
        viewModel.products.observe(this, Observer { list ->
            adapter.setItems(list)
            // Si la lista está vacía, intentar cargar desde API automáticamente
            if (list.isEmpty()) {
                android.widget.Toast.makeText(this, "Cargando productos desde API...", android.widget.Toast.LENGTH_SHORT).show()
                viewModel.syncFromApi()
            }
        })

        // Observamos el estado de carga para mostrar feedback
        viewModel.loading.observe(this) { isLoading ->
            // Aquí podrías mostrar un ProgressBar si quisieras
            if (isLoading) {
                supportActionBar?.subtitle = "Cargando..."
            } else {
                supportActionBar?.subtitle = null
            }
        }

        // Mostrar mensajes de estado
        viewModel.statusMessage.observe(this) { msg ->
            msg?.let { android.widget.Toast.makeText(this, it, android.widget.Toast.LENGTH_LONG).show() }
        }

        // Observar mensajes del carrito
        cartViewModel.statusMessage.observe(this) { msg ->
            msg?.let { android.widget.Toast.makeText(this, it, android.widget.Toast.LENGTH_SHORT).show() }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_product_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sync -> {
                viewModel.syncFromApi()
                true
            }
            R.id.action_add_samples -> {
                viewModel.addSampleProducts()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDeleteConfirmationDialog(product: ProductEntity) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirm_delete_title))
            .setMessage(getString(R.string.confirm_delete_message))
            .setPositiveButton(getString(R.string.btn_delete_confirm)) { _, _ ->
                viewModel.deleteProduct(product)
            }
            .setNegativeButton(getString(R.string.btn_cancel), null)
            .show()
    }
}
