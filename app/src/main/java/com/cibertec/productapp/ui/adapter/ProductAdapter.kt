package com.cibertec.productapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cibertec.productapp.R
import com.cibertec.productapp.data.local.entity.ProductEntity
import com.cibertec.productapp.databinding.ItemProductBinding

/**
 * Adapter para RecyclerView que muestra los productos.
 */
class ProductAdapter(
    private var items: List<ProductEntity> = emptyList(),
    private val onItemClick: (ProductEntity) -> Unit,
    private val onAddToCartClick: ((ProductEntity) -> Unit)? = null
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    fun setItems(newItems: List<ProductEntity>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(inflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductEntity) {
            try {
                binding.tvTitle.text = product.title ?: "Sin título"
                binding.tvCategory.text = product.category ?: "Sin categoría"
                binding.tvPrice.text = try {
                    binding.root.context.getString(R.string.price_format, product.price)
                } catch (e: Exception) {
                    "$${product.price}"
                }

                // Cargar imagen con Glide (si existe URL)
                val imgUrl = product.image
                if (!imgUrl.isNullOrEmpty()) {
                    Glide.with(binding.root.context)
                        .load(imgUrl)
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder)
                        .into(binding.ivProduct)
                } else {
                    binding.ivProduct.setImageResource(R.drawable.ic_placeholder)
                }

                // Indicador de sincronización
                binding.syncedIndicator.visibility = if (product.isSynced) View.VISIBLE else View.GONE

                binding.root.setOnClickListener {
                    try {
                        onItemClick(product)
                    } catch (e: Exception) {
                        // Log del error pero no crash
                    }
                }

                // Botón agregar al carrito
                binding.btnAddToCart.setOnClickListener {
                    try {
                        onAddToCartClick?.invoke(product)
                    } catch (e: Exception) {
                        // Log del error pero no crash
                    }
                }
            } catch (e: Exception) {
                // Si hay error en el binding, usar valores por defecto
                binding.tvTitle.text = "Error al cargar producto"
                binding.tvCategory.text = ""
                binding.tvPrice.text = "$0.00"
            }
        }
    }
}
