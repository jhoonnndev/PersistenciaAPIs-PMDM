package com.example.trabajonavegacionpmdm
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.trabajonavegacionpmdm.ui.screens.*
import com.example.trabajonavegacionpmdm.ui.viewmodel.ShopViewModel
//MainActivity (lo que se ve)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Pasar el Context al ViewModel
        val context = applicationContext
        val shopViewModel: ShopViewModel by viewModels {
            viewModelFactory {
                initializer {
                    ShopViewModel(context)
                }
            }
        }
        setContent {
            //Inicializar NavController
            val navController = rememberNavController()

            MaterialTheme {
                //NavHost contenedor principal
                NavHost(navController = navController, startDestination = "welcome") {

                    // Definimos un nombre para cada pantalla
                    composable("welcome") {
                        WelcomeScreen(navController)
                    }

                    composable("law_info") {
                        LawInfoScreen(navController)
                    }

                    composable("home") {
                        HomeScreen(navController, shopViewModel)
                    }

                    //Pasar datos entre pantallas
                    composable(
                        route = "details/{vehicleId}",
                        arguments = listOf(navArgument("vehicleId") { type = NavType.IntType }) // - Uso de NavType.IntType (Línea 348)
                    ) { backStackEntry ->
                        val vehicleId = backStackEntry.arguments?.getInt("vehicleId") ?: 0
                        DetailsScreen(navController, vehicleId, shopViewModel)
                    }

                    composable("cart") {
                        CartScreen(navController, shopViewModel)
                    }
                }
            }
        }
    }
}