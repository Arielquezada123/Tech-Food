package com.example.techfood.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.techfood.Adapter.AdapterProducto
import com.example.techfood.Models.Producto
import com.example.techfood.databinding.FragmentGalleryBinding
import com.google.firebase.database.*

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private lateinit var productosList: ArrayList<Producto>
    private lateinit var productosRecyclerView: RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var adapterProducto: AdapterProducto

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productosRecyclerView = binding.rvProductos
        productosRecyclerView.layoutManager = LinearLayoutManager(context)
        productosRecyclerView.setHasFixedSize(true)
        productosList = arrayListOf()
        adapterProducto = AdapterProducto(productosList)
        productosRecyclerView.adapter = adapterProducto

        getProductos()
    }

    private fun getProductos() {
        database = FirebaseDatabase.getInstance().getReference("Productos")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    productosList.clear()
                    for (productosSnapshot in snapshot.children) {
                        val producto = productosSnapshot.getValue(Producto::class.java)
                        if (producto != null) {
                            productosList.add(producto)
                        }
                    }
                    adapterProducto.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error aquí si es necesario
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
