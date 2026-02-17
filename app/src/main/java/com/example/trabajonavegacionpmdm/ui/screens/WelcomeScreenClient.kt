package com.example.trabajonavegacionpmdm.ui.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.trabajonavegacionpmdm.R
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreenClient(
    onAgeVerified: () -> Unit,
    onUnderage: () -> Unit,
    onBackClick: () -> Unit // <--- NUEVO: Acción para volver atrás
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            val userAge = year - selectedYear
            val isAdult = if (userAge > 18) {
                true
            } else if (userAge == 18) {
                if (month > selectedMonth) true
                else if (month == selectedMonth) day >= selectedDay
                else false
            } else {
                false
            }

            if (isAdult) {
                onAgeVerified()
            } else {
                onUnderage()
            }
        }, year, month, day
    )

    Scaffold(
        // --- AÑADIDA BARRA SUPERIOR CON FLECHA ATRÁS ---
        topBar = {
            TopAppBar(
                title = { Text("Verificación de Edad") }, // Opcional: Texto o vacío
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.car_shopping),
                contentDescription = "Logo App",
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Bienvenido a PMDM Cars",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tu concesionario de confianza",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { datePickerDialog.show() },
                modifier = Modifier.fillMaxWidth(0.7f).height(50.dp)
            ) {
                Text("VERIFICAR EDAD Y ENTRAR")
            }
        }
    }
}