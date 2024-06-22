package com.example.techfood

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.techfood.Models.Producto
import com.example.techfood.databinding.FragmentAgregarBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import java.util.*
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.firebase.storage.FirebaseStorage
import androidx.navigation.fragment.findNavController


class AgregarFragment : Fragment() {

    private lateinit var binding: FragmentAgregarBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageRef: StorageReference
    private lateinit var selectedImageUri: Uri

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
        storageRef = FirebaseStorage.getInstance().reference.child("images") // Referencia a la carpeta "images" en Storage

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

            // Subir imagen seleccionada a Firebase Storage
            if (::selectedImageUri.isInitialized) {
                uploadImageToFirebase(id, nombre, precio)
            } else {
                Toast.makeText(requireContext(), "Seleccione una imagen", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSeleccionarImagen.setOnClickListener {
            selectImageFromGallery()
        }
        binding.btnVer.setOnClickListener {
            // Navegar al fragmento de Productos cuando se hace clic en el botón
            findNavController().navigate(R.id.nav_gallery)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openGallery()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permiso de lectura de almacenamiento externo denegado",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun selectImageFromGallery() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openGallery()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun openGallery() {
        getContent.launch("image/*")
    }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            binding.ivProductoImagen.setImageURI(selectedImageUri)
        }
    }

    private fun uploadImageToFirebase(id: String?, nombre: String, precio: String) {
        val imageRef = storageRef.child("${UUID.randomUUID()}.jpg")
        val uploadTask = imageRef.putFile(selectedImageUri)

        uploadTask.addOnSuccessListener {
            // Imagen subida exitosamente, obtenemos la URL de descarga
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()

                // Guardar datos del producto con la URL de la imagen en Firebase Realtime Database
                val producto = Producto(id, nombre, precio, imageUrl)
                if (id != null) {
                    databaseReference.child(id).setValue(producto)
                        .addOnSuccessListener {
                            Snackbar.make(
                                requireView(),
                                "Producto ingresado",
                                Snackbar.LENGTH_LONG
                            ).show()
                            // Limpiar campos después de guardar exitosamente
                            binding.etNombreProducto.text.clear()
                            binding.etPrecioProducto.text.clear()
                            binding.ivProductoImagen.setImageResource(0)
                        }
                        .addOnFailureListener {
                            Snackbar.make(
                                requireView(),
                                "Error al ingresar el producto",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                }
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show()
        }

    }
}




