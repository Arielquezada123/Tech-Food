package com.example.techfood.Models

data class Producto(
    var id: String? = null,
    var nombre: String? = null,
    var precio: String? = null,
    var imageUrl: String? = null
) {
    // Constructor vacío necesario para Firebase
    constructor() : this("", "", "", "")
}

