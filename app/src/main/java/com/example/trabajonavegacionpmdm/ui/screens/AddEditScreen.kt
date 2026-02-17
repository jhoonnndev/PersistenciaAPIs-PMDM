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
import com.example.trabajonavegacionpmdm.ui.viewmodel.ShopViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    viewModel: ShopViewModel,
    onBackClick: () -> Unit
) {
    // Estados locales para el formulario
    var brand by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var horsePower by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var engine by remember { mutableStateOf("") }

    // Observamos el mensaje global para saber si se guardó bien
    val uiMessage by viewModel.uiMessage.collectAsState()

    // Efecto: Si el mensaje es de éxito, volvemos atrás automáticamente
    LaunchedEffect(uiMessage) {
        if (uiMessage == "¡Vehículo creado con éxito!") {
            onBackClick()
            viewModel.clearMessage() // Limpiamos para que no vuelva a saltar
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Vehículo") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
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
                .verticalScroll(rememberScrollState()), // Scroll por si el teclado tapa campos
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sección 1: Datos de la Marca
            Text("Marca", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            OutlinedTextField(
                value = brand,
                onValueChange = { brand = it },
                label = { Text("Nombre Marca (Ej. Toyota)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = country,
                onValueChange = { country = it },
                label = { Text("País de origen") },
                modifier = Modifier.fillMaxWidth()
            )

            Divider()

            // Sección 2: Datos del Vehículo
            Text("Vehículo", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            OutlinedTextField(
                value = model,
                onValueChange = { model = it },
                label = { Text("Modelo (Ej. Corolla)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Precio (€)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("URL de la Imagen") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("https://...") }
            )

            Divider()

            // Sección 3: Ficha Técnica
            Text("Ficha Técnica", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = horsePower,
                    onValueChange = { horsePower = it },
                    label = { Text("Potencia (CV)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Peso (Kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }
            OutlinedTextField(
                value = engine,
                onValueChange = { engine = it },
                label = { Text("Motor (Ej. Híbrido, Gasolina)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Guardar
            Button(
                onClick = {
                    // Validación simple
                    if (model.isNotEmpty() && price.isNotEmpty() && brand.isNotEmpty()) {
                        viewModel.addVehicle(
                            model = model,
                            price = price.toDoubleOrNull() ?: 0.0,
                            imageUrl = imageUrl,
                            brandName = brand,
                            country = country,
                            horsePower = horsePower.toIntOrNull() ?: 0,
                            engineType = engine,
                            weight = weight.toDoubleOrNull() ?: 0.0
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("GUARDAR VEHÍCULO")
            }

            // Mensaje de error (si hubo fallo al guardar)
            if (uiMessage != null && uiMessage != "¡Vehículo creado con éxito!") {
                Text(
                    text = uiMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}