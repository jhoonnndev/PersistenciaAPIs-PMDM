package com.example.trabajonavegacionpmdm.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trabajonavegacionpmdm.data.Vehicle
import com.example.trabajonavegacionpmdm.data.VehicleProvider
import com.example.trabajonavegacionpmdm.data.local.relations.VehiclePopulated
import com.example.trabajonavegacionpmdm.data.repository.ShopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// VIEWMODEL (Para gestión del estado del carrito)

//ViewModel Compartido
// Usamos esto para pasar datos complejos o mantener estado entre pantallas.
class ShopViewModel (private val repository: ShopRepository): ViewModel() {

    val vehicles: StateFlow<List<VehiclePopulated>> = repository.vehicles
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var selectedQuantity by mutableStateOf(0)
    var selectedVehicle: VehiclePopulated? by mutableStateOf(null)

    val totalPrice: Double
        get() = (selectedVehicle?.vehicle?.price ?: 0.0) * selectedQuantity

    init {

        refreshData()
    }
    fun refreshData() {
        viewModelScope.launch {
            repository.refreshData()
        }
    }


    fun addToCart(vehicle: VehiclePopulated, quantity: Int) {
        selectedVehicle = vehicle
        selectedQuantity = quantity
    }

    fun clearCart() {
        selectedVehicle = null
        selectedQuantity = 0
    }

    fun getVehicleById(id: Int): VehiclePopulated? {
        return vehicles.value.find { it.vehicle.vehicleId.toInt() == id }
    }
}