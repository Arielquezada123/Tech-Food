package com.example.techfood.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techfood.Models.Producto
import com.example.techfood.R

class AdapterProducto(
    private val productosList: ArrayList<Producto>
) : RecyclerView.Adapter<AdapterProducto.ProductoViewHolder>() {

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreProducto: TextView = itemView.findViewById(R.id.tvNombreProducto)
        val precioProducto: TextView = itemView.findViewById(R.id.tvPrecioProducto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_productos, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productosList[position]
        holder.nombreProducto.text = producto.nombre
        holder.precioProducto.text = producto.precio
    }

    override fun getItemCount(): Int {
        return productosList.size
    }
}


