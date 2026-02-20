package com.example.trabajonavegacionpmdm.data

import kotlinx.serialization.Serializable

// SIMULACIÓN DE DATOS
data class Vehicle(
    val id: Int,
    val brand: String,
    val model: String,
    val price: Double,
    val hp: Int,
    val imageRes: Int // Sustituiremos por imagenes
)

// Clase que lee el JSON ( por eso imageName es String )
@Serializable
data class VehicleJsonDto(
    val id: Int,
    val brand: String,
    val model: String,
    val price: Double,
    val hp: Int,
    val imageName: String
)
