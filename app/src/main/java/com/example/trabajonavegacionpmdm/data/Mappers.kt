package com.example.trabajonavegacionpmdm.data

import com.example.trabajonavegacionpmdm.data.local.entities.*
import com.example.trabajonavegacionpmdm.data.remote.model.*

// Convierte 1 Coche de la API -> en 3 Entidades para la Base de Datos
fun VehicleRemote.toEntityTriple(): Triple<VehicleEntity, BrandEntity, TechnicalSpecsEntity> {

    // 1. Sacamos la Marca
    val brandEntity = BrandEntity(
        name = this.brand.name,
        country = this.brand.country,
        urlLogo = this.brand.urlLogo
    )

    // 2. Sacamos el Coche (El ID de la marca se pondrá luego en el Repositorio)
    val vehicleEntity = VehicleEntity(
        vehicleId = this.id ?: 0,
        model = this.model,
        price = this.price,
        imageUrl = this.imageUrl,
        brandOwnerId = 0 // Temporal
    )

    // 3. Sacamos la Ficha
    val specsEntity = TechnicalSpecsEntity(
        horsePower = this.specs.horsePower,
        engineType = this.specs.engineType,
        weight = this.specs.weight,
        vehicleOwnerId = 0 // Temporal
    )

    return Triple(vehicleEntity, brandEntity, specsEntity)
}

fun ExtraRemote.toEntity(): ExtraFeatureEntity {
    return ExtraFeatureEntity(
        extraId = this.id ?: 0,
        name = this.name,
        price = this.price
    )
}