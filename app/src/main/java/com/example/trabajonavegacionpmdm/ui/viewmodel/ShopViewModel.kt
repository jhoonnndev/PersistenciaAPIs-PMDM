package com.example.trabajonavegacionpmdm.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trabajonavegacionpmdm.data.local.relations.VehiclePopulated
import com.example.trabajonavegacionpmdm.data.remote.model.BrandRemote
import com.example.trabajonavegacionpmdm.data.remote.model.SpecsRemote
import com.example.trabajonavegacionpmdm.data.remote.model.VehicleRemote
import com.example.trabajonavegacionpmdm.data.repository.ShopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShopViewModel(
    private val repository: ShopRepository
) : ViewModel() {

    // -------------------------------------------------------------------------
    // 1. ESTADO DE LA BBDD (Lista de Coches Persistentes)
    // -------------------------------------------------------------------------
    val vehiclesState: StateFlow<List<VehiclePopulated>> = repository.vehicles
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // -------------------------------------------------------------------------
    // 2. ESTADO DEL CARRITO (Memoria Temporal) - ¡LO QUE FALTABA!
    // -------------------------------------------------------------------------
    // Usamos mutableStateOf para que Compose se entere de los cambios inmediatos
    var selectedVehicle: VehiclePopulated? by mutableStateOf(null)
        private set

    var selectedQuantity: Int by mutableStateOf(0)
        private set

    // Propiedad computada: Calcula el total automáticamente
    val totalPrice: Double
        get() = (selectedVehicle?.vehicle?.price ?: 0.0) * selectedQuantity

    // -------------------------------------------------------------------------
    // 3. ESTADO DE MENSAJES (Feedback)
    // -------------------------------------------------------------------------
    private val _uiMessage = MutableStateFlow<String?>(null)
    val uiMessage = _uiMessage.asStateFlow()

    init {
        refreshData()
    }

    // -------------------------------------------------------------------------
    // ACCIONES DE CARRITO (Llamadas desde DetailsScreen y CartScreen)
    // -------------------------------------------------------------------------

    fun addToCart(vehicle: VehiclePopulated, quantity: Int) {
        selectedVehicle = vehicle
        selectedQuantity = quantity
    }

    fun clearCart() {
        selectedVehicle = null
        selectedQuantity = 0
    }

    // Función auxiliar para buscar un coche por ID dentro de la lista que ya tenemos en memoria
    // Esto es necesario para la DetailsScreen
    fun getVehicleById(id: Int): VehiclePopulated? {
        // Buscamos en la lista actual (vehiclesState.value)
        return vehiclesState.value.find { it.vehicle.vehicleId == id.toLong() }
    }

    // -------------------------------------------------------------------------
    // ACCIONES DE BBDD / API (CRUD)
    // -------------------------------------------------------------------------

    fun refreshData() {
        viewModelScope.launch {
            try {
                repository.refreshData()
            } catch (e: Exception) {
                _uiMessage.value = "Error de conexión: No se pudieron actualizar los datos."
            }
        }
    }

    fun deleteVehicle(vehicle: VehiclePopulated) {
        viewModelScope.launch {
            try {
                repository.deleteVehicle(vehicle.vehicle.vehicleId)
                _uiMessage.value = "Vehículo eliminado correctamente."
            } catch (e: Exception) {
                _uiMessage.value = "Error al eliminar: ${e.message}"
            }
        }
    }

    fun addVehicle(
        model: String,
        price: Double,
        imageUrl: String,
        brandName: String,
        country: String,
        horsePower: Int,
        engineType: String,
        weight: Double
    ) {
        viewModelScope.launch {
            try {
                val newVehicle = VehicleRemote(
                    id = null,
                    model = model,
                    price = price,
                    imageUrl = imageUrl,
                    brand = BrandRemote(name = brandName, country = country, urlLogo = ""),
                    specs = SpecsRemote(horsePower = horsePower, engineType = engineType, weight = weight),
                    extras = emptyList()
                )
                repository.addVehicle(newVehicle)
                _uiMessage.value = "¡Vehículo creado con éxito!"
            } catch (e: Exception) {
                _uiMessage.value = "Error al crear vehículo: ${e.message}"
            }
        }
    }

    // En tu archivo ShopViewModel.kt

// ... el resto de tu código ...

    // Añade esta función para conectar la UI con el Repositorio
    fun updateVehicle(
        id: Long,
        model: String,
        price: Double,
        imageUrl: String,
        brandName: String,
        country: String,
        horsePower: Int,
        engineType: String,
        weight: Double
    ) {
        viewModelScope.launch {
            try {
                // Creamos el objeto con los datos editados
                // Es IMPORTANTE pasar el 'id' original para que la API sepa cuál actualizar
                val vehicleToUpdate = VehicleRemote(
                    id = id,
                    model = model,
                    price = price,
                    imageUrl = imageUrl,
                    brand = BrandRemote(name = brandName, country = country, urlLogo = ""),
                    specs = SpecsRemote(horsePower = horsePower, engineType = engineType, weight = weight),
                    extras = emptyList() // Si tu app no edita extras, enviamos lista vacía o la actual si la tuvieras
                )

                // Llamamos al método que ya tienes listo en el repositorio
                repository.updateVehicle(id, vehicleToUpdate)
                _uiMessage.value = "¡Vehículo actualizado correctamente!"
            } catch (e: Exception) {
                _uiMessage.value = "Error al actualizar: ${e.message}"
            }
        }
    }


    fun clearMessage() {
        _uiMessage.value = null
    }


}