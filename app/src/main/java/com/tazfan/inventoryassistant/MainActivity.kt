package com.tazfan.inventoryassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tazfan.inventoryassistant.ui.screens.*
import com.tazfan.inventoryassistant.ui.theme.InventoryAssistantTheme
import com.tazfan.inventoryassistant.ui.viewmodel.InventoryViewModel

import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InventoryAssistantTheme {
                InventoryApp()
            }
        }
    }
}

@Composable
fun InventoryApp() {
    val navController = rememberNavController()
    val viewModel: InventoryViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onNavigateToSale = { navController.navigate("sale") },
                onNavigateToItems = { navController.navigate("items") },
                onNavigateToEditStock = { navController.navigate("edit_stock") },
                onNavigateToHistory = { navController.navigate("history") },
                onNavigateToReport = { navController.navigate("report") }
            )
        }
        composable("sale") {
            SaleScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("items") {
            ItemsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAddItem = { navController.navigate("add_item") }
            )
        }
        composable("edit_stock") {
            EditStockScreen(
                onNavigateToAddItem = { navController.navigate("add_item") },
                onNavigateToUpdateStock = { navController.navigate("update_stock") },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("add_item") {
            AddItemScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("update_stock") {
            UpdateStockScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("history") {
            HistoryScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("report") {
            ReportScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    InventoryAssistantTheme {
        HomeScreen(
            onNavigateToSale = {},
            onNavigateToItems = {},
            onNavigateToEditStock = {},
            onNavigateToHistory = {},
            onNavigateToReport = {}
        )
    }
}
