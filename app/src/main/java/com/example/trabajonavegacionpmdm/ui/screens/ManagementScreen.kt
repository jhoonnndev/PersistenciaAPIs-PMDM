package com.example.trabajonavegacionpmdm.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
    val vehicles by viewModel.vehiclesState.collectAsState()
    val uiMessage by viewModel.uiMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

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
        if (vehicles.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No hay vehículos. ¡Añade uno!")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp) // Espacio entre tarjetas
            ) {
                items(vehicles) { vehiclePopulated ->
                    //Tarjeta
                    EmployeeVehicleItem(
                        vehicle = vehiclePopulated,
                        onEditClick = { onEditClick(vehiclePopulated.vehicle.vehicleId) },
                        onDeleteClick = { viewModel.deleteVehicle(vehiclePopulated) }
                    )
                }
            }
        }
    }
}

@Composable
fun EmployeeVehicleItem(
    vehicle: VehiclePopulated,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        // Toda la tarjeta es editable
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEditClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = vehicle.vehicle.imageUrl,
                contentDescription = vehicle.vehicle.model,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
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