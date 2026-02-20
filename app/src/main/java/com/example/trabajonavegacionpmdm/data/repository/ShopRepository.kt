package com.example.trabajonavegacionpmdm.data.repository

import android.util.Log
import com.example.trabajonavegacionpmdm.data.local.dao.ShopDao
import com.example.trabajonavegacionpmdm.data.local.entities.VehicleExtraCrossRef
import com.example.trabajonavegacionpmdm.data.local.relations.VehiclePopulated
import com.example.trabajonavegacionpmdm.data.remote.ShopApi
import com.example.trabajonavegacionpmdm.data.remote.model.VehicleRemote
import com.example.trabajonavegacionpmdm.data.toEntity
import com.example.trabajonavegacionpmdm.data.toEntityTriple
import kotlinx.coroutines.flow.Flow

class ShopRepository(
    private val api: ShopApi,
    private val dao: ShopDao
) {
    // 1. LECTURA (Single Source of Truth)
    // La UI siempre observa este Flow. Si insertamos algo abajo, esto se actualiza solo.
    val vehicles: Flow<List<VehiclePopulated>> = dao.getAllVehicles()

    fun getVehicleById(id: Long): Flow<VehiclePopulated?> {
        return dao.getVehicleById(id)
    }

    // 2. SINCRONIZACIÓN (GET)
    suspend fun refreshData() {
        try {
            // 1. Descargar lista de la API
            val remoteVehicles = api.getVehicles()

            // 2. Si hay datos, limpiamos la caché local y guardamos los nuevos
            if (remoteVehicles.isNotEmpty()) {
                dao.deleteAllVehicles() // Borrón y cuenta nueva

                // Guardamos uno a uno usando la función auxiliar
                remoteVehicles.forEach { remoteVehicle ->
                    insertRemoteVehicleIntoRoom(remoteVehicle)
                }
            }
        } catch (e: Exception) {
            Log.e("ShopRepository", "Error al refrescar datos: ${e.message}")
            e.printStackTrace()
        }
    }

    // 3. CREACIÓN (POST)
    suspend fun addVehicle(vehicle: VehicleRemote) {
        try {
            // 1. Primero subimos a la Nube (API)
            // La API nos devuelve el objeto creado con su ID final asignado por el servidor
            val createdRemoteVehicle = api.createVehicle(vehicle)

            // 2. Guardamos ese objeto confirmado en Room (incluyendo extras)
            insertRemoteVehicleIntoRoom(createdRemoteVehicle)

        } catch (e: Exception) {
            Log.e("ShopRepository", "Error al crear vehículo: ${e.message}")
            throw e
        }
    }

    // 4. BORRADO (DELETE)
    suspend fun deleteVehicle(vehicleId: Long) {
        try {
            // 1. Borrar de la API
            api.deleteVehicle(vehicleId)

            // 2. Borrar de Room
            // Al borrar el coche, el "Cascade" de Room borrará Ficha y Relaciones de Extras automáticamente
            dao.deleteVehicleById(vehicleId)

        } catch (e: Exception) {
            Log.e("ShopRepository", "Error al eliminar vehículo: ${e.message}")
            throw e
        }
    }

    // 5. UPDATE (PUT)
    suspend fun updateVehicle(id: Long, vehicle: VehicleRemote) {
        try {
            val updatedRemote = api.updateVehicle(id, vehicle)
            dao.deleteVehicleById(id)
            insertRemoteVehicleIntoRoom(updatedRemote)
        } catch (e: Exception) {
            Log.e("ShopRepository", "Error al actualizar: ${e.message}")
            throw e
        }
    }


    // FUNCIÓN PRIVADA AUXILIAR (Aquí está la lógica de los Extras)
    /**
     * Esta función se encarga de desmenuzar el objeto remoto y guardar
     * Marca, Coche, Ficha y Extras en sus respectivas tablas.
     */
    private suspend fun insertRemoteVehicleIntoRoom(remoteVehicle: VehicleRemote) {

        // 1. Usamos el Mapper para obtener las entidades principales
        val (vehicleEntity, brandEntity, specsEntity) = remoteVehicle.toEntityTriple()

        // 2. Insertar MARCA
        // Room nos devuelve el ID de la fila insertada (o existente)
        val brandId = dao.insertBrand(brandEntity)

        // 3. Insertar VEHÍCULO
        // Asignamos el ID de la marca al coche (Foreign Key)
        // IMPORTANTE: Nos aseguramos de usar el ID que viene de la API si existe
        val vehicleWithBrand = vehicleEntity.copy(
            vehicleId = remoteVehicle.id ?: 0,
            brandOwnerId = brandId
        )
        val vehicleId = dao.insertVehicle(vehicleWithBrand)

        // 4. Insertar FICHA TÉCNICA
        // Asignamos el ID del coche a la ficha (Foreign Key)
        val specsWithVehicle = specsEntity.copy(vehicleOwnerId = vehicleId)
        dao.insertTechnicalSpecs(specsWithVehicle)

        // 5. Insertar EXTRAS (Lógica N:M)
        remoteVehicle.extras.forEach { remoteExtra ->
            // A. Convertir el extra remoto a entidad
            val extraEntity = remoteExtra.toEntity()

            // B. Insertar el Extra en la tabla 'extras' y obtener su ID
            val extraId = dao.insertExtra(extraEntity)

            // C. Insertar la relación en la tabla cruce 'vehicle_extra_cross_ref'
            // Esto conecta este coche concreto (vehicleId) con este extra concreto (extraId)
            val crossRef = VehicleExtraCrossRef(
                vehicleId = vehicleId,
                extraId = extraId
            )
            dao.insertVehicleExtraCrossRef(crossRef)
        }
    }
}