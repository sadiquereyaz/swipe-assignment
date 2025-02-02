package com.reyaz.swipeassignment.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.reyaz.swipeassignment.presentation.notification.NotificationScreen
import com.reyaz.swipeassignment.presentation.product.ProductListScreen

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {


    NavHost(
        navController = navController,
        startDestination = Route.Products
    ) {
        composable<Route.Products> {
            ProductListScreen(
                onNavigateToNotification = { navController.navigate(Route.Notification) }
            )
        }
        composable<Route.Notification> {
            NotificationScreen(
                navigateToHome = { navController.popBackStack() }
            )
        }
    }
}
