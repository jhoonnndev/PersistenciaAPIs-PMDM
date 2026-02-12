package com.example.trabajonavegacionpmdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.trabajonavegacionpmdm.data.local.ShopDatabase
import com.example.trabajonavegacionpmdm.data.remote.ShopApi
import com.example.trabajonavegacionpmdm.data.repository.ShopRepository
import com.example.trabajonavegacionpmdm.ui.screens.*
import com.example.trabajonavegacionpmdm.ui.viewmodel.ShopViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = ShopDatabase.getDatabase(applicationContext)
        val dao = database.shopDao()

        val api = ShopApi()

        val repository = ShopRepository(api, dao)


        val shopViewModel: ShopViewModel by viewModels {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(ShopViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return ShopViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }

        setContent {
            val navController = rememberNavController()

            MaterialTheme {
                NavHost(navController = navController, startDestination = "welcome") {

                    composable("welcome") {
                        WelcomeScreen(navController)
                    }

                    composable("law_info") {
                        LawInfoScreen(navController)
                    }

                    composable("home") {
                        // Pasar el viewModel actualizado
                        HomeScreen(navController, shopViewModel)
                    }

                    composable(
                        route = "details/{vehicleId}",
                        arguments = listOf(navArgument("vehicleId") { type = NavType.IntType })
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