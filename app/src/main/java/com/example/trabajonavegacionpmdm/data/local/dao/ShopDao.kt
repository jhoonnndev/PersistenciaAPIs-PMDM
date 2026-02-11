package com.example.trabajonavegacionpmdm.data.local.dao

import androidx.room.*
import com.example.trabajonavegacionpmdm.data.local.entities.*
import com.example.trabajonavegacionpmdm.data.local.relations.VehiclePopulated
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDao {

    // Usamos Transaction porque accede a varias tablas

    // Obtener todos los coches con TODOS sus datos (Marca, ficha, extras)
    // Devuelve Flow para reactividad en tiempo real
    @Transaction
    @Query("SELECT * FROM vehicles")
    fun getAllVehicles(): Flow<List<VehiclePopulated>>

    // Obtener un solo coche por ID
    @Transaction
    @Query("SELECT * FROM vehicles WHERE vehicleId = :id")
    fun getVehicleById(id: Long): Flow<VehiclePopulated?>

    // CRUD

    //CREATE

    // Insertar Marca
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrand(brand: BrandEntity): Long

    // Insertar Vehículo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicle(vehicle: VehicleEntity): Long

    // Insertar Ficha Técnica
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTechnicalSpecs(specs: TechnicalSpecsEntity)

    // Insertar Extra
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExtra(extra: ExtraFeatureEntity): Long

    // Insertar Relación Coche-Extra
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicleExtraCrossRef(crossRef: VehicleExtraCrossRef)

    // REMOVE

    @Delete
    suspend fun deleteVehicle(vehicle: VehicleEntity)

    // Borrar todos los coches (para cada vez que cargue la app se borren los datos y actualicen de la api)
    @Query("DELETE FROM vehicles")
    suspend fun deleteAllVehicles()
}