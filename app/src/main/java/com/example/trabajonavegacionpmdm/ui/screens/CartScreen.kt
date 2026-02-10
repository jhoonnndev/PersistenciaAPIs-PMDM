package com.example.trabajonavegacionpmdm.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trabajonavegacionpmdm.ui.viewmodel.ShopViewModel

//CARRITO COMPRA
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, viewModel: ShopViewModel) {
    //Recuperamos datos del ViewModel
    val vehicle = viewModel.selectedVehicle
    val quantity = viewModel.selectedQuantity
    val total = viewModel.totalPrice


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cancelar Operacion") },
                navigationIcon = {
                    IconButton(onClick = {
                        //Limpiamos toda la ruta para volver al home directos
                        navController.navigate("home"){
                            popUpTo("home"){
                                inclusive = true
                            }
                        }
                        viewModel.clearCart()  //Limpiamos carrito al salir
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Cancelar operacion y Volver atras"
                        )
                    }
                }
            )
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Text("Carrito de Compra", style = MaterialTheme.typography.headlineLarge)

                Spacer(modifier = Modifier.width(12.dp))

                Icon(
                    modifier = Modifier.size(36.dp),
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = "Carrito de compra"
                )
            }


            Spacer(modifier = Modifier.height(24.dp))

            if (vehicle != null) {
                Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Image(
                            painter = painterResource(id = vehicle.imageRes),
                            contentDescription = "Imagen del ${vehicle.model}",
                            modifier = Modifier
                                .height(200.dp)
                                .clip(RoundedCornerShape(8.dp)), // Bordes
                            contentScale = ContentScale.Fit
                        )
                        Text("Vehículo: ${vehicle.brand} ${vehicle.model}")
                        Text("Cantidad seleccionada: $quantity")
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Text("TOTAL A PAGAR: $$total", style = MaterialTheme.typography.titleLarge)
                    }
                }
            } else {
                Text("El carrito está vacío")
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedButton(onClick = {
                navController.navigate("home")
            }) {
                Text("Pagar  --> $$total")
            }
        }
    }
}