package com.example.trabajonavegacionpmdm.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class VehicleRemote(
    val id: Long? = null,
    val model: String,
    val price: Double,
    val imageUrl: String,
    val brand: BrandRemote,    // Objeto anidado
    val specs: SpecsRemote,    // Objeto anidado
    val extras: List<ExtraRemote> = emptyList() // Lista
)

@Serializable
data class BrandRemote(
    val name: String,
    val country: String,
    val urlLogo: String
)

@Serializable
data class SpecsRemote(
    val horsePower: Int,
    val engineType: String,
    val weight: Double
)

@Serializable
data class ExtraRemote(
    val id: Long? = null,
    val name: String,
    val price: Double
)