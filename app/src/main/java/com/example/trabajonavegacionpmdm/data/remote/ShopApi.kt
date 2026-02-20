package com.example.trabajonavegacionpmdm.data.remote

import com.example.trabajonavegacionpmdm.data.remote.model.VehicleRemote
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ShopApi {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                coerceInputValues = true
            })
        }
    }

    // URL DE NUESTRO GITHUB PARA ACCEDER AL JSON
    private val baseUrl = "https://my-json-server.typicode.com/jhoonnndev/persistenciaapis-pmdm"

    suspend fun getVehicles(): List<VehicleRemote> {
        return client.get("$baseUrl/vehicles").body()
    }

    // Funciones extra para cumplir el expediente (aunque my-json-server no guarde cambios reales)
    suspend fun createVehicle(vehicle: VehicleRemote): VehicleRemote {
        return client.post("$baseUrl/vehicles") {
            contentType(ContentType.Application.Json)
            setBody(vehicle)
        }.body()
    }

    suspend fun updateVehicle(id: Long, vehicle: VehicleRemote): VehicleRemote {
        return client.put("$baseUrl/vehicles/$id") {
            contentType(ContentType.Application.Json)
            setBody(vehicle)
        }.body()
    }

    suspend fun deleteVehicle(id: Long) {
        client.delete("$baseUrl/vehicles/$id")
    }
}