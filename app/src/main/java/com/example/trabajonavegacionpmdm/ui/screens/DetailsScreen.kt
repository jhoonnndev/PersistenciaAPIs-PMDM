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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.trabajonavegacionpmdm.ui.viewmodel.ShopViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(navController: NavController, vehicleId: Int, viewModel: ShopViewModel) {

    // 1. INICIALIZACIÓN DIRECTA (Sin LaunchedEffect)
    // Llamamos a la función para cargar el coche y limpiar extras.
    // Como tu ViewModel comprueba "if (found != selectedVehicle)", esto es seguro y no reinicia
    // los extras cada vez que la pantalla se redibuja (recomposición), solo si cambia el ID.
    viewModel.selectVehicleById(vehicleId)

    // 2. OBSERVAMOS EL ESTADO
    // Leemos las variables directamente del ViewModel, que ya habrán sido actualizadas por la línea anterior.
    val vehicle = viewModel.selectedVehicle
    val addedExtras = viewModel.selectedExtras
    val currentTotal = viewModel.totalPrice
    val quantity = viewModel.selectedQuantity

    // Variables locales para el menú desplegable
    val options = listOf(1, 2, 3, 4, 5)
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                // Si vehicle es null, mostramos cargando o texto genérico
                title = { Text(text = vehicle?.vehicle?.model ?: "Cargando...") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            // Solo mostramos la barra inferior si tenemos datos del coche
            if (vehicle != null) {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Total:", style = MaterialTheme.typography.labelMedium)
                        Text(
                            "$currentTotal €", // Se actualiza solo al marcar extras
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(onClick = { navController.navigate("cart") }) {
                        Text("Añadir al Carrito")
                    }
                }
            }
        }
    ) { innerPadding ->
        // Contenido principal
        if (vehicle != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // --- IMAGEN ---
                AsyncImage(
                    model = vehicle.vehicle.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                // --- INFO BÁSICA ---
                Text(text = vehicle.brand.name, style = MaterialTheme.typography.labelLarge)
                Text(text = vehicle.vehicle.model, style = MaterialTheme.typography.headlineMedium)
                Text(
                    text = "Precio Base: ${vehicle.vehicle.price} €",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider() // Divider() se renombró a HorizontalDivider en versiones nuevas, si da error usa Divider()
                Spacer(modifier = Modifier.height(16.dp))

                // --- FICHA TÉCNICA ---
                Text("Especificaciones", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                vehicle.technicalSpecs?.let {
                    Text("• Motor: ${it.engineType}")
                    Text("• Potencia: ${it.horsePower} CV")
                    Text("• Peso: ${it.weight} kg")
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                // --- SECCIÓN CONFIGURADOR (EXTRAS) ---
                Text("Configura tus extras", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Añade opciones para personalizar tu vehículo", style = MaterialTheme.typography.bodySmall)

                Spacer(modifier = Modifier.height(8.dp))

                // Lista de extras disponibles (Checkbox)
                vehicle.extras.forEach { extra ->
                    val isChecked = addedExtras.contains(extra)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { viewModel.toggleExtra(extra) }
                        )
                        Column {
                            Text(text = extra.name, style = MaterialTheme.typography.bodyLarge)
                            Text(
                                text = "+${extra.price} €",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- SELECTOR DE CANTIDAD ---
                Text("Cantidad", style = MaterialTheme.typography.titleSmall)
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.width(150.dp)
                ) {
                    OutlinedTextField(
                        value = quantity.toString(),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        options.forEach { num ->
                            DropdownMenuItem(
                                text = { Text(num.toString()) },
                                onClick = {
                                    viewModel.setQuantity(num)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Espacio final para que la BottomBar no tape contenido
                Spacer(modifier = Modifier.height(80.dp))
            }
        } else {
            // Estado de carga o error si el ID no existe
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}