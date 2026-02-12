package com.example.trabajonavegacionpmdm.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


// TABLA MARCA (Brand)
// Relación 1:N Una marca crea muchos coches
@Entity(tableName = "brands")
data class BrandEntity(
    @PrimaryKey(autoGenerate = true) val brandId: Long = 0,
    val name: String,        // Ej: Toyota
    val country: String,     // Ej: Japón
    val urlLogo: String      // URL de la imagen del logo
)

// TABLA VEHÍCULO (Vehicle)
// Tiene una FK que apunta a la Marca.
@Entity(
    tableName = "vehicles",
    foreignKeys = [
        ForeignKey(
            entity = BrandEntity::class,
            parentColumns = ["brandId"],
            childColumns = ["brandOwnerId"],
            onDelete = ForeignKey.CASCADE // Si borras la marca, se borran sus coches (Cascada)
        )
    ],
    // Creamos un índice para que la búsqueda por marca sea rápida
    indices = [Index("brandOwnerId")]
)
data class VehicleEntity(
    @PrimaryKey(autoGenerate = true) val vehicleId: Long = 0,
    val model: String,
    val price: Double,
    val imageUrl: String,
    val brandOwnerId: Long   // La FK que une con Brand
)

// TABLA FICHA TÉCNICA (TechnicalSpecs)
// Relación 1:1 Un coche tiene solo una ficha.
@Entity(
    tableName = "technical_specs",
    foreignKeys = [
        ForeignKey(
            entity = VehicleEntity::class,
            parentColumns = ["vehicleId"],
            childColumns = ["vehicleOwnerId"],
            onDelete = ForeignKey.CASCADE // Si borras el coche, se borra su ficha
        )
    ],
    indices = [Index(value = ["vehicleOwnerId"], unique = true)]
)
data class TechnicalSpecsEntity(
    @PrimaryKey(autoGenerate = true) val specId: Long = 0,
    val vehicleOwnerId: Long, // FK hacia el coche
    val horsePower: Int,
    val engineType: String,
    val weight: Double
)

// TABLA EXTRAS
// Relación N:M Muchos coches tienen muchos extras.
@Entity(tableName = "extras")
data class ExtraFeatureEntity(
    @PrimaryKey(autoGenerate = true) val extraId: Long = 0,
    val name: String,
    val price: Double
)

// TABLA DE UNION COCHES CON EXTRAS
@Entity(
    tableName = "vehicle_extra_cross_ref",
    primaryKeys = ["vehicleId", "extraId"], // La clave primaria es compuesta
    foreignKeys = [
        ForeignKey(entity = VehicleEntity::class, parentColumns = ["vehicleId"], childColumns = ["vehicleId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = ExtraFeatureEntity::class, parentColumns = ["extraId"], childColumns = ["extraId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("vehicleId"), Index("extraId")]
)
data class VehicleExtraCrossRef(
    val vehicleId: Long,
    val extraId: Long
)