package com.example.trabajonavegacionpmdm.data.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.trabajonavegacionpmdm.data.local.entities.BrandEntity
import com.example.trabajonavegacionpmdm.data.local.entities.ExtraFeatureEntity
import com.example.trabajonavegacionpmdm.data.local.entities.TechnicalSpecsEntity
import com.example.trabajonavegacionpmdm.data.local.entities.VehicleEntity
import com.example.trabajonavegacionpmdm.data.local.entities.VehicleExtraCrossRef

// Resultado de consulta, no tabla
data class VehiclePopulated( //Vehiculo  con todos sus atributos
    @Embedded val vehicle: VehicleEntity,

    // Relación con Marca (un coche tiene una marca)
    // parentColumn Vehicle
    // entityColumn Brand
    @Relation(
        parentColumn = "brandOwnerId",
        entityColumn = "brandId"
    )
    val brand: BrandEntity,

    // Relación con Ficha técnica (1:1)
    // parentColumn: Vehicle
    // entityColumn: Specs
    @Relation(
        parentColumn = "vehicleId",
        entityColumn = "vehicleOwnerId"
    )
    val technicalSpecs: TechnicalSpecsEntity?,

    // Relación con Extras (N:M usando la tabla de cruce)
    @Relation(
        parentColumn = "vehicleId",
        entityColumn = "extraId",
        associateBy = Junction(VehicleExtraCrossRef::class)
    )
    val extras: List<ExtraFeatureEntity>
)