package com.example.techfood

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.techfood.Models.Producto
import com.example.techfood.databinding.FragmentAgregarBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AgregarFragment : Fragment() {

    private lateinit var binding: FragmentAgregarBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAgregarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseReference = FirebaseDatabase.getInstance().reference.child("Productos")

        binding.btnGuardar.setOnClickListener {
            val nombre = binding.etNombreProducto.text.toString()
            val precio = binding.etPrecioProducto.text.toString()
            val id = databaseReference.push().key

            if (nombre.isEmpty()) {
                binding.etNombreProducto.error = "Ingrese un nombre"
                return@setOnClickListener
            }
            if (precio.isEmpty()) {
                binding.etPrecioProducto.error = "Ingrese un precio"
                return@setOnClickListener
            }
            val producto = Producto(id, nombre, precio)
            if (id != null) {
                databaseReference.child(id).setValue(producto)
                    .addOnSuccessListener {
                        Snackbar.make(binding.root, "Producto ingresado", Snackbar.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        Snackbar.make(binding.root, "Error al ingresar el producto", Snackbar.LENGTH_LONG).show()
                    }
            }
        }
        binding.btnVer.setOnClickListener {
            // Navegar al fragmento de Productos cuando se hace clic en el botón
            findNavController().navigate(R.id.nav_gallery)
        }
    }
}

