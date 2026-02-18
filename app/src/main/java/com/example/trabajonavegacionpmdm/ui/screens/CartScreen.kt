package com.example.trabajonavegacionpmdm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trabajonavegacionpmdm.ui.viewmodel.ShopViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, viewModel: ShopViewModel) {
    val vehicle = viewModel.selectedVehicle
    val extras = viewModel.selectedExtras // Lista de extras seleccionados
    val quantity = viewModel.selectedQuantity
    val total = viewModel.totalPrice

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resumen del Pedido") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (vehicle != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${vehicle.brand.name} ${vehicle.vehicle.model}",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        // Desglose
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Precio Base:")
                            Text("${vehicle.vehicle.price} €")
                        }

                        // Lista de extras en el ticket
                        if (extras.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Extras seleccionados:", style = MaterialTheme.typography.labelLarge)
                            extras.forEach { extra ->
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("• ${extra.name}", style = MaterialTheme.typography.bodySmall)
                                    Text("+${extra.price} €", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Cantidad:")
                            Text("x$quantity")
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Total Grande
                Text("Total a Pagar", style = MaterialTheme.typography.labelLarge)
                Text(
                    "$total €",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.clearCart()
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Finalizar Compra")
                }
            } else {
                Text("Tu carrito está vacío :(")
            }
        }
    }
}