package com.example.trabajonavegacionpmdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.trabajonavegacionpmdm.data.local.ShopDatabase
import com.example.trabajonavegacionpmdm.data.remote.ShopApi
import com.example.trabajonavegacionpmdm.data.repository.ShopRepository
import com.example.trabajonavegacionpmdm.ui.screens.*
import com.example.trabajonavegacionpmdm.ui.theme.TrabajoNavegacionPMDMTheme
import com.example.trabajonavegacionpmdm.ui.viewmodel.ShopViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialización de la BBDD y Repositorio
        val database = ShopDatabase.getDatabase(this)
        val api = ShopApi()
        val repository = ShopRepository(api, database.shopDao())
        val viewModel = ShopViewModel(repository)

        setContent {
            TrabajoNavegacionPMDMTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "welcome") {

                    // 1. PANTALLA BIENVENIDA
                    composable("welcome") {
                        WelcomeScreen(
                            onClientClick = { navController.navigate("client_welcome") },
                            onEmployeeClick = { navController.navigate("management") }
                        )
                    }

                    // 2. PANTALLA BIENVENIDA CLIENTE (Con validación de edad)
                    composable("client_welcome") {
                        WelcomeScreenClient(
                            onAgeVerified = { navController.navigate("home") },
                            onUnderage = { navController.navigate("law_info") },
                            onBackClick = { navController.popBackStack() } // <--- AÑADIDO: Vuelve a WelcomeScreen
                        )
                    }

                    // 2. PANTALLA HOME (CLIENTE)
                    composable("home") {
                        HomeScreen(navController = navController, viewModel = viewModel)
                    }

                    // 3. PANTALLA DETALLES
                    composable(
                        "details/{vehicleId}",
                        arguments = listOf(navArgument("vehicleId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val vehicleId = backStackEntry.arguments?.getInt("vehicleId") ?: 0
                        DetailsScreen(navController, vehicleId, viewModel)
                    }

                    // 4. PANTALLA CARRITO
                    composable("cart") {
                        CartScreen(navController, viewModel)
                    }

                    // 5. PANTALLA INFO LEGAL
                    composable("law_info") {
                        LawInfoScreen(navController)
                    }

                    // 6. GESTIÓN (EMPLEADO)
                    composable("management") {
                        ManagementScreen(
                            viewModel = viewModel,
                            onAddClick = { navController.navigate("add_edit") },
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    // 7. FORMULARIO (EMPLEADO)
                    composable("add_edit") {
                        AddEditScreen(
                            viewModel = viewModel,
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}