package com.example.trabajonavegacionpmdm.data

import android.content.Context
import com.example.trabajonavegacionpmdm.R
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.IOException


// SIMULACIÓN DE DATOS
data class Vehicle(
    val id: Int,
    val brand: String,
    val model: String,
    val price: Double,
    val hp: Int,
    val imageRes: Int // Sustituiremos por imagenes
)

// Clase que lee el JSON ( por eso imageName es String )
@Serializable
data class VehicleJsonDto(
    val id: Int,
    val brand: String,
    val model: String,
    val price: Double,
    val hp: Int,
    val imageName: String
)

object VehicleProvider {

    // Configuración para que sea tolerante a errores
    private val jsonConfig = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    fun loadVehiclesFromJson(context: Context): List<Vehicle> {
        val jsonString: String
        try {
            jsonString = context.assets.open("vehicles.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return emptyList()
        }

        // Leemos el JSON
        val dtoList = jsonConfig.decodeFromString<List<VehicleJsonDto>>(jsonString)

        // Asignamos a cada coche su imagen
        val imagenesMap = mapOf(
            "corolla" to R.drawable.corolla,
            "mustang" to R.drawable.mustang,
            "model3" to R.drawable.model3,
            "porsche" to R.drawable.porsche
        )


        return dtoList.map { dto ->
            Vehicle(
                id = dto.id,
                brand = dto.brand,
                model = dto.model,
                price = dto.price,
                hp = dto.hp,
                imageRes = imagenesMap[dto.imageName] ?: R.drawable.ic_launcher_foreground
            )
        }
    }
}