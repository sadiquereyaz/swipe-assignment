package com.reyaz.swipeassignment.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
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
