package com.cibertec.productapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cibertec.productapp.databinding.ActivityMainBinding
import com.cibertec.productapp.ui.activities.AddProductActivity
import com.cibertec.productapp.ui.activities.ProductListActivity
import com.cibertec.productapp.ui.activities.CartActivity

/**
 * MainActivity: Pantalla principal con menú de navegación.
 * Contiene botones para "Agregar Producto" y "Ver Lista de Productos".
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setSupportActionBar(binding.toolbar)
            supportActionBar?.title = "ProductApp"

            setupClickListeners()

        } catch (e: Exception) {
            android.widget.Toast.makeText(this, "Error inicializando: ${e.message}", android.widget.Toast.LENGTH_LONG).show()
        }
    }

    private fun setupClickListeners() {
        // Botón para agregar producto
        binding.btnAddProduct.setOnClickListener {
            try {
                val intent = Intent(this, AddProductActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                android.widget.Toast.makeText(this, "Error al abrir agregar: ${e.message}", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para ver lista de productos
        binding.btnViewProducts.setOnClickListener {
            try {
                val intent = Intent(this, ProductListActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                android.widget.Toast.makeText(this, "Error al abrir lista: ${e.message}", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para ver carrito
        binding.btnCart.setOnClickListener {
            try {
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                android.widget.Toast.makeText(this, "Error al abrir carrito: ${e.message}", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }
}