package com.reyaz.swipeassignment.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.reyaz.swipeassignment.ui.ProductListScreen

@OptIn(ExperimentalMaterial3Api::class)
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
            composable("add_product") {
                 NotificationScreen()
            }
        }
    }

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn (
        modifier = modifier.fillMaxSize()
    ){
        item {
            Text("Notification Screen")
        }
    }
}


