package com.example.trabajonavegacionpmdm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trabajonavegacionpmdm.ui.viewmodel.ShopViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    navController: NavController,
    viewModel: ShopViewModel,
    vehicleId: Long = 0L // Recibimos el ID (0 = Crear, >0 = Editar)
) {
    // Variables de estado para el formulario
    var model by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var brandName by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var horsePower by remember { mutableStateOf("") }
    var engineType by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    val uiMessage by viewModel.uiMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // 1. EFECTO DE CARGA: Si es modo EDITAR, rellenamos los campos
    LaunchedEffect(vehicleId) {
        if (vehicleId != 0L) {
            // Pedimos al ViewModel que busque el coche por ID
            val vehicleToEdit = viewModel.getVehicleById(vehicleId.toInt())

            vehicleToEdit?.let { v ->
                // Rellenamos los campos con los datos existentes
                model = v.vehicle.model
                price = v.vehicle.price.toString()
                imageUrl = v.vehicle.imageUrl
                brandName = v.brand.name
                country = v.brand.country
                horsePower = v.technicalSpecs?.horsePower?.toString() ?: ""
                engineType = v.technicalSpecs?.engineType ?: ""
                weight = v.technicalSpecs?.weight?.toString() ?: ""
            }
        }
    }

    // 2. EFECTO DE MENSAJE: Si hay éxito, volvemos atrás
    LaunchedEffect(uiMessage) {
        uiMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
            if (it.contains("correctamente") || it.contains("éxito")) {
                navController.popBackStack()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                // Título dinámico
                title = { Text(if (vehicleId == 0L) "Nuevo Vehículo" else "Editar Vehículo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Hacemos scrollable por si el teclado tapa
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- CAMPOS DEL FORMULARIO ---
            OutlinedTextField(value = model, onValueChange = { model = it }, label = { Text("Modelo") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = imageUrl, onValueChange = { imageUrl = it }, label = { Text("URL Imagen") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = brandName, onValueChange = { brandName = it }, label = { Text("Marca") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = country, onValueChange = { country = it }, label = { Text("País") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = horsePower, onValueChange = { horsePower = it }, label = { Text("Caballos (HP)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = engineType, onValueChange = { engineType = it }, label = { Text("Tipo Motor") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Peso (kg)") }, modifier = Modifier.fillMaxWidth())

            // --- BOTÓN DE GUARDAR ---
            Button(
                onClick = {
                    // Validamos que al menos haya modelo y precio
                    if (model.isNotBlank() && price.isNotBlank()) {
                        if (vehicleId == 0L) {
                            // MODO CREAR
                            viewModel.addVehicle(
                                model, price.toDoubleOrNull() ?: 0.0, imageUrl, brandName, country,
                                horsePower.toIntOrNull() ?: 0, engineType, weight.toDoubleOrNull() ?: 0.0
                            )
                        } else {
                            // MODO EDITAR (Usamos la nueva función)
                            viewModel.updateVehicle(
                                id = vehicleId, // Pasamos el ID original
                                model = model,
                                price = price.toDoubleOrNull() ?: 0.0,
                                imageUrl = imageUrl,
                                brandName = brandName,
                                country = country,
                                horsePower = horsePower.toIntOrNull() ?: 0,
                                engineType = engineType,
                                weight = weight.toDoubleOrNull() ?: 0.0
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (vehicleId == 0L) "Crear Vehículo" else "Guardar Cambios")
            }
        }
    }
}