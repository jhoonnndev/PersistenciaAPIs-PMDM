package com.example.trabajonavegacionpmdm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.trabajonavegacionpmdm.R
import com.example.trabajonavegacionpmdm.ui.viewmodel.ShopViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(navController: NavController, vehicleId: Int, viewModel: ShopViewModel) {
    val vehicle = viewModel.getVehicleById(vehicleId)
    val options = listOf("1", "2", "3")
    var expanded by remember { mutableStateOf(false) }
    var selectedQuantity by remember { mutableStateOf(options[0]) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver atrás"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (vehicle != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text("Detalles: ${vehicle.vehicle.model}", style = MaterialTheme.typography.headlineMedium)

                AsyncImage(
                    model = vehicle.vehicle.imageUrl,
                    contentDescription = "Imagen del ${vehicle.vehicle.model}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Marca: ${vehicle.brand.name}")
                Text("Modelo: ${vehicle.vehicle.model}")
                Text("Precio Unidad: ${vehicle.vehicle.price} €")

                vehicle.technicalSpecs?.let {
                    Text("Motor: ${it.engineType}")
                    Text("Potencia: ${it.horsePower} CV")
                }

                if (vehicle.extras.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Extras incluidos:", style = MaterialTheme.typography.titleSmall)
                    vehicle.extras.forEach { extra ->
                        Text("- ${extra.name} (+${extra.price}€)")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Selecciona la cantidad:", style = MaterialTheme.typography.bodyMedium)

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedQuantity,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Cantidad") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        options.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    selectedQuantity = selectionOption
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {
                        val qty = selectedQuantity.toInt()
                        viewModel.addToCart(vehicle, qty)
                        navController.navigate("cart")
                    }) {
                        Text("Comprar")
                    }
                }
            }
        }
    }
}