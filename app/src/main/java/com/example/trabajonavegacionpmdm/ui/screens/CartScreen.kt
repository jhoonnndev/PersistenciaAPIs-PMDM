package com.example.trabajonavegacionpmdm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trabajonavegacionpmdm.ui.viewmodel.ShopViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, viewModel: ShopViewModel) {
    val vehicle = viewModel.selectedVehicle
    val quantity = viewModel.selectedQuantity
    val total = viewModel.totalPrice

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (vehicle != null) {
                Text("Resumen de compra", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Vehículo: ${vehicle.vehicle.model}")
                Text("Cantidad: $quantity")
                Text("Precio unitario: ${vehicle.vehicle.price} €")
                Spacer(modifier = Modifier.height(16.dp))
                Text("Total a pagar: $total €", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)

                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = {
                    viewModel.clearCart()
                    navController.navigate("home")
                }) {
                    Text("Finalizar Compra")
                }
            } else {
                Text("El carrito está vacío")
            }
        }
    }
}