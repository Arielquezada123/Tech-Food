package com.example.techfood.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.techfood.Models.Producto
import com.example.techfood.R
import com.example.techfood.databinding.ItemProductosBinding

class AdapterProducto(
    private val productosList: ArrayList<Producto>
) : RecyclerView.Adapter<AdapterProducto.ProductoViewHolder>() {

    // Clase ViewHolder usando ViewBinding
    class ProductoViewHolder(private val binding: ItemProductosBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(producto: Producto) {
            binding.tvNombreProducto.text = producto.nombre
            binding.tvPrecioProducto.text = producto.precio
            // Cargar imagen con Glide, usando binding.ivProductoImagen
            Glide.with(binding.root.context)
                .load(producto.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(binding.ivImagenProducto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val binding = ItemProductosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        // Agregar márgenes o paddings al contenedor raíz del elemento del producto
        val layoutParams = binding.root.layoutParams as ViewGroup.MarginLayoutParams
        val margin = parent.context.resources.getDimensionPixelSize(R.dimen.item_margin)
        layoutParams.setMargins(margin, margin, margin, margin)
        binding.root.layoutParams = layoutParams
        return ProductoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productosList[position]
        holder.bind(producto)
    }

    override fun getItemCount(): Int {
        return productosList.size
    }
}


