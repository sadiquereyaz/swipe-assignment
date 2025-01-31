package com.reyaz.swipeassignment.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.reyaz.swipeassignment.ui.ProductListScreen

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {

    NavHost(navController = navController, startDestination = "products") {
        composable("products") {
            ProductListScreen(
                onAddProduct = { navController.navigate("add_product") }
            )
        }
        composable("add_product") {
            /* AddProductScreen(
                 onBack = { navController.navigateUp() }
             )*/
        }
    }
}