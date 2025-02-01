package com.reyaz.swipeassignment.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.reyaz.swipeassignment.presentation.notification.NotificationScreen
import com.reyaz.swipeassignment.presentation.product.composable.ProductListScreen

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {


        NavHost(
            navController = navController,
            startDestination = "products") {
            composable("products") {
                ProductListScreen(
                    onNavigateToNotification = { navController.navigate("notification_route") }
                )

            }
            composable("notification_route") {
                 NotificationScreen()
            }
        }
    }
