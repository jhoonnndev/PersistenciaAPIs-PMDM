package com.example.trabajonavegacionpmdm.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

//PANTALLA LEY
@Composable
fun LawInfoScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("ACCESO DENEGADO", color = MaterialTheme.colorScheme.error)
        Text("Según la ley vigente, debes ser mayor de edad para comprar un vehículo.")
        Spacer(modifier = Modifier.height(16.dp))

        // Botón de vuelta a pantalla bienvenida
        Button(onClick = {
            //volver
            navController.popBackStack()
        }) {
            Text("Volver")
        }
    }
}
