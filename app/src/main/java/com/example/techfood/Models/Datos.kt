package com.example.techfood.Models

import android.util.Log
data class Datos(
    val dato: String = ""
) {
    companion object {
        fun fromFirebaseModel(firebaseDatos: Map<String, Any>): Datos {
            // Verificar si el campo "dato" está presente y no es nulo
            val dato = if ("dato" in firebaseDatos && firebaseDatos["dato"] != null) {
                firebaseDatos["dato"].toString()
            } else {
                ""
            }

            Log.d("Datos", "Dato mapeado desde Firebase: $dato")
            return Datos(dato = dato)
        }
    }
}