package com.reyaz.swipeassignment.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.reyaz.swipeassignment.ui.addproduct.AddProductScreen
import com.reyaz.swipeassignment.ui.list.ProductListingScreen
import com.reyaz.swipeassignment.ui.product.ProductViewModel

@Composable
fun AppNavigation(viewModel: ProductViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "listing") {
        composable("listing") {
            ProductListingScreen(viewModel) {
                navController.navigate("add")
            }
        }
        composable("add") {
            AddProductScreen(viewModel) {
                navController.popBackStack()
            }
        }
    }
}