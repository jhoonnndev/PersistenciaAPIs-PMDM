package com.example.trabajonavegacionpmdm.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.trabajonavegacionpmdm.R

@Composable
fun WelcomeScreen(
    onClientClick: () -> Unit,
    onEmployeeClick: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Puedes poner el logo aquí también o solo texto
            Image(
                painter = painterResource(id = R.drawable.car_shopping),
                contentDescription = "Logo General",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "¿Cómo quieres acceder?",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón CLIENTE
            Button(
                onClick = onClientClick,
                modifier = Modifier.fillMaxWidth(0.8f).height(50.dp)
            ) {
                Text("ÁREA CLIENTES")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón EMPLEADO
            Button(
                onClick = onEmployeeClick,
                modifier = Modifier.fillMaxWidth(0.8f).height(50.dp)
            ) {
                Text("ÁREA EMPLEADOS")
            }
        }
    }
}