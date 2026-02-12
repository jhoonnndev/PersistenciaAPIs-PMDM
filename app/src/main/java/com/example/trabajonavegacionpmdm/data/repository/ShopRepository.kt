package com.example.trabajonavegacionpmdm.data.repository

import com.example.trabajonavegacionpmdm.data.local.dao.ShopDao
import com.example.trabajonavegacionpmdm.data.local.entities.VehicleEntity
import com.example.trabajonavegacionpmdm.data.local.relations.VehiclePopulated
import com.example.trabajonavegacionpmdm.data.remote.ShopApi
import kotlinx.coroutines.flow.Flow

class ShopRepository(
    private val dao: ShopDao,
    private val api: ShopApi
) {
    // 1. La UI SIEMPRE lee de aquí (Flow = datos en vivo desde la BBDD)
    val vehicles: Flow<List<VehiclePopulated>> = dao.getAllVehicles()

    // 2. Función para recargar datos desde Internet
    suspend fun refreshData() {
        try {
            // A. Descargar de la API
            val vehiclesFromApi = api.getVehicles()

            // B. Si la descarga fue bien, limpiamos BBDD vieja y metemos la nueva
            if (vehiclesFromApi.isNotEmpty()) {
                // Opcional: dao.deleteAllVehicles() si quieres borrar lo viejo

                // Guardamos uno a uno (o crea un insertAll en el DAO)
                vehiclesFromApi.forEach { vehicle ->
                    dao.insertVehicle(vehicle)
                }
            }
        } catch (e: Exception) {
            // Si falla Internet, no hacemos nada.
            // La app seguirá mostrando lo que tenga guardado en 'vehicles' (Persistencia)
            e.printStackTrace()
        }
    }

    // Funciones puente para insertar desde la UI (Carrito, Crear coche...)
    suspend fun insertVehicleLocal(vehicle: VehicleEntity) {
        dao.insertVehicle(vehicle)
    }

    suspend fun deleteVehicle(vehicle: VehicleEntity) {
        dao.deleteVehicle(vehicle)
    }
}