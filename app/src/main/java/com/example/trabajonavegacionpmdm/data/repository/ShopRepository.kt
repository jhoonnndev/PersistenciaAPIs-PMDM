package com.example.trabajonavegacionpmdm.data.repository

import com.example.trabajonavegacionpmdm.data.local.dao.ShopDao
import com.example.trabajonavegacionpmdm.data.local.entities.VehicleExtraCrossRef
import com.example.trabajonavegacionpmdm.data.local.relations.VehiclePopulated
import com.example.trabajonavegacionpmdm.data.remote.ShopApi
import com.example.trabajonavegacionpmdm.data.toEntity
import com.example.trabajonavegacionpmdm.data.toEntityTriple
import kotlinx.coroutines.flow.Flow

class ShopRepository(
    private val api: ShopApi,
    private val dao: ShopDao
) {
    // 1. La UI SIEMPRE lee de la Base de Datos Local (Single Source of Truth)
    val vehicles: Flow<List<VehiclePopulated>> = dao.getAllVehicles()

    // 2. Función para recargar datos desde Internet y guardarlos en local
    suspend fun refreshData() {
        try {
            // A. Descargar lista de la API
            val remoteVehicles = api.getVehicles()

            if (remoteVehicles.isNotEmpty()) {
                // Opcional: Borrar datos viejos para no duplicar
                dao.deleteAllVehicles()

                // B. Procesar cada vehículo que llega de internet
                remoteVehicles.forEach { remoteVehicle ->

                    // Usamos tu Mapper para separar el JSON en 3 entidades
                    val (vehicleEntity, brandEntity, specsEntity) = remoteVehicle.toEntityTriple()

                    // --- PASO 1: Guardar la MARCA ---
                    // Necesitamos el ID que genera Room al guardar para asignarlo al coche
                    val brandId = dao.insertBrand(brandEntity)

                    // --- PASO 2: Guardar el COCHE ---
                    // Asignamos la FK de la marca al coche y guardamos
                    val vehicleWithBrandId = vehicleEntity.copy(brandOwnerId = brandId)
                    val vehicleId = dao.insertVehicle(vehicleWithBrandId)

                    // --- PASO 3: Guardar la FICHA TÉCNICA ---
                    // Asignamos la FK del coche a la ficha y guardamos
                    val specsWithVehicleId = specsEntity.copy(vehicleOwnerId = vehicleId)
                    dao.insertTechnicalSpecs(specsWithVehicleId)

                    // --- PASO 4: Guardar los EXTRAS (Relación N:M) ---
                    remoteVehicle.extras.forEach { remoteExtra ->
                        // Convertir extra remoto a entidad
                        val extraEntity = remoteExtra.toEntity()

                        // Guardar el extra (o recuperar su ID si ya existía)
                        val extraId = dao.insertExtra(extraEntity) // Asume que devuelve Long

                        // Crear la relación en la tabla cruce
                        val crossRef = VehicleExtraCrossRef(vehicleId, extraId)
                        dao.insertVehicleExtraCrossRef(crossRef)
                    }
                }
            }
            // En ShopRepository.kt
        } catch (e: Exception) {
            // Esto imprimirá el error real en la consola de Android Studio (Logcat) en rojo
            android.util.Log.e("ShopRepository", "Error al descargar datos: ${e.message}")
            e.printStackTrace()
        }
    }
}