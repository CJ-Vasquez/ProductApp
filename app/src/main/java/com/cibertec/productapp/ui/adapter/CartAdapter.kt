package com.cibertec.productapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cibertec.productapp.R
import com.cibertec.productapp.data.local.entity.CartItemEntity
import com.cibertec.productapp.databinding.ItemCartBinding

/**
 * Adapter para mostrar items del carrito de compras
 */
class CartAdapter(
    private val onQuantityChanged: (CartItemEntity, Int) -> Unit,
    private val onRemoveClick: (CartItemEntity) -> Unit
) : ListAdapter<CartItemEntity, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CartViewHolder(
        private val binding: ItemCartBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: CartItemEntity) {
            binding.apply {
                // Información del producto
                tvProductTitle.text = cartItem.productTitle
                tvProductPrice.text = root.context.getString(R.string.price_format, cartItem.productPrice)
                tvQuantity.text = cartItem.quantity.toString()

                // Total del item
                val itemTotal = cartItem.productPrice * cartItem.quantity
                tvItemTotal.text = root.context.getString(R.string.price_format, itemTotal)

                // Cargar imagen
                if (!cartItem.productImage.isNullOrEmpty()) {
                    Glide.with(root.context)
                        .load(cartItem.productImage)
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder)
                        .into(ivProductImage)
                } else {
                    ivProductImage.setImageResource(R.drawable.ic_placeholder)
                }

                // Botones de cantidad (ahora son TextView)
                btnDecrease.setOnClickListener {
                    val newQuantity = cartItem.quantity - 1
                    if (newQuantity >= 0) {
                        onQuantityChanged(cartItem, newQuantity)
                    }
                }

                btnIncrease.setOnClickListener {
                    val newQuantity = cartItem.quantity + 1
                    onQuantityChanged(cartItem, newQuantity)
                }

                // Botón remover
                btnRemove.setOnClickListener {
                    onRemoveClick(cartItem)
                }
            }
        }
    }

    class CartDiffCallback : DiffUtil.ItemCallback<CartItemEntity>() {
        override fun areItemsTheSame(oldItem: CartItemEntity, newItem: CartItemEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartItemEntity, newItem: CartItemEntity): Boolean {
            return oldItem == newItem
        }
    }
}
