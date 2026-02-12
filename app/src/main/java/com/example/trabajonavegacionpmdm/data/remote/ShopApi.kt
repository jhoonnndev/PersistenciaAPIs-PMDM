package com.example.trabajonavegacionpmdm.data.remote

import com.example.trabajonavegacionpmdm.data.local.entities.VehicleEntity
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ShopApi {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true // Si el JSON trae cosas extra, no explota
                prettyPrint = true
            })
        }
    }

    // URL de ejemplo (cámbiala por tu MockAPI o my-json-server)
    private val BASE_URL = "https://tu-mock-api.com"

    suspend fun getVehicles(): List<VehicleEntity> {
        return client.get("$BASE_URL/vehicles").body()
    }

    // Aquí añadirías funciones para getBrands, getExtras, etc.
}

