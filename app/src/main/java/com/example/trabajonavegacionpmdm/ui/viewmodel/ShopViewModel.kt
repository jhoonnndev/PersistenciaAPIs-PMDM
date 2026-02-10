package com.example.trabajonavegacionpmdm.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.trabajonavegacionpmdm.data.Vehicle
import com.example.trabajonavegacionpmdm.data.VehicleProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// VIEWMODEL (Para gestión del estado del carrito)

//ViewModel Compartido
// Usamos esto para pasar datos complejos o mantener estado entre pantallas.
class ShopViewModel (context: Context): ViewModel() {
    private val _vehicles = MutableStateFlow<List<Vehicle>>(emptyList())
    val vehicles: StateFlow<List<Vehicle>> = _vehicles
    var selectedQuantity by mutableStateOf(0)
    var selectedVehicle: Vehicle? by mutableStateOf(null)

    val totalPrice: Double
        get() = (selectedVehicle?.price ?: 0.0) * selectedQuantity

    init {
        // Cargamos los datos al iniciar el ViewModel
        _vehicles.value = VehicleProvider.loadVehiclesFromJson(context)
    }

    fun addToCart(vehicle: Vehicle, quantity: Int) {
        selectedVehicle = vehicle
        selectedQuantity = quantity
    }

    fun clearCart() {
        selectedVehicle = null
        selectedQuantity = 0
    }

    fun getVehicleById(id: Int): Vehicle? {
        return _vehicles.value.find { it.id == id }
    }
}