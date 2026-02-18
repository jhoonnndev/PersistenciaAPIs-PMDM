package com.example.trabajonavegacionpmdm.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.trabajonavegacionpmdm.data.local.relations.VehiclePopulated
import com.example.trabajonavegacionpmdm.ui.viewmodel.ShopViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagementScreen(
    viewModel: ShopViewModel,
    onAddClick: () -> Unit,
    onBackClick: () -> Unit,
    onEditClick: (Long) -> Unit
) {
    // 1. Observamos la lista de vehículos desde la BBDD
    val vehicles by viewModel.vehiclesState.collectAsState()

    // 2. Observamos mensajes (ej. "Coche eliminado")
    val uiMessage by viewModel.uiMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Mostramos SnackBar si hay mensaje
    LaunchedEffect(uiMessage) {
        uiMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Empleados") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Vehículo")
            }
        }
    ) { padding ->
        // 3. Lista de Coches
        if (vehicles.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No hay vehículos. ¡Añade uno!")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(vehicles) { vehiclePopulated -> // vehiclePopulated es el objeto completo
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // AQUÍ ESTÁ LA CLAVE: Navegamos pasando el ID del coche
                                val id = vehiclePopulated.vehicle.vehicleId
                                onEditClick(id)
                            }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Muestra Marca + Modelo
                        Text(text = "${vehiclePopulated.brand.name} ${vehiclePopulated.vehicle.model}")

                        // Botón de borrar (ese ya lo tenías)
                        IconButton(onClick = { viewModel.deleteVehicle(vehiclePopulated) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmployeeVehicleItem(
    vehicle: VehiclePopulated,
    onDeleteClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen pequeña
            AsyncImage(
                model = vehicle.vehicle.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Datos del coche
            Column(modifier = Modifier.weight(1f)) {
                Text(text = vehicle.vehicle.model, style = MaterialTheme.typography.titleMedium)
                Text(text = "${vehicle.vehicle.price} €", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Marca: ${vehicle.brand.name}", style = MaterialTheme.typography.labelSmall)
            }

            // Botón Borrar (Papelera Roja)
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Borrar vehículo",
                    tint = Color.Red
                )
            }
        }
    }
}