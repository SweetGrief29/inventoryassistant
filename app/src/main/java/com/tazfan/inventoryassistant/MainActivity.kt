package com.tazfan.inventoryassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tazfan.inventoryassistant.ui.screens.*
import com.tazfan.inventoryassistant.ui.theme.InventoryAssistantTheme
import com.tazfan.inventoryassistant.ui.viewmodel.InventoryViewModel

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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .clip(RoundedCornerShape(50.dp)),
                containerColor = Color(0xFF1B2E1A),
                tonalElevation = 0.dp
            ) {
                // Beranda
                val homeSelected = currentRoute == "home"
                NavigationBarItem(
                    selected = homeSelected,
                    onClick = { navController.navigate("home") { popUpTo("home") { inclusive = true } } },
                    icon = {
                        Box(contentAlignment = Alignment.Center) {
                            if (homeSelected) {
                                Image(painter = painterResource(id = R.drawable.button_active), contentDescription = null, modifier = Modifier.size(60.dp, 32.dp))
                            }
                            Icon(
                                painter = painterResource(id = if (homeSelected) R.drawable.house_active else R.drawable.house),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.Unspecified
                            )
                        }
                    },
                    label = { Text("Beranda", color = if (homeSelected) Color.White else Color.Gray) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    )
                )

                // Toko (Penjualan)
                val saleSelected = currentRoute == "sale"
                NavigationBarItem(
                    selected = saleSelected,
                    onClick = { navController.navigate("sale") },
                    icon = {
                        Box(contentAlignment = Alignment.Center) {
                            if (saleSelected) {
                                Image(painter = painterResource(id = R.drawable.button_active), contentDescription = null, modifier = Modifier.size(60.dp, 32.dp))
                            }
                            Icon(
                                painter = painterResource(id = if (saleSelected) R.drawable.cart_active else R.drawable.cart),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.Unspecified
                            )
                        }
                    },
                    label = { Text("Toko", color = if (saleSelected) Color.White else Color.Gray) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    )
                )

                // Riwayat
                val historySelected = currentRoute == "history"
                NavigationBarItem(
                    selected = historySelected,
                    onClick = { navController.navigate("history") },
                    icon = {
                        Box(contentAlignment = Alignment.Center) {
                            if (historySelected) {
                                Image(painter = painterResource(id = R.drawable.button_active), contentDescription = null, modifier = Modifier.size(60.dp, 32.dp))
                            }
                            Icon(
                                painter = painterResource(id = if (historySelected) R.drawable.receipt_active else R.drawable.receipt),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.Unspecified
                            )
                        }
                    },
                    label = { Text("Riwayat", color = if (historySelected) Color.White else Color.Gray) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    )
                )

                // Profil
                val profileSelected = currentRoute == "profile"
                NavigationBarItem(
                    selected = profileSelected,
                    onClick = { navController.navigate("profile") },
                    icon = {
                        Box(contentAlignment = Alignment.Center) {
                            if (profileSelected) {
                                Image(painter = painterResource(id = R.drawable.button_active), contentDescription = null, modifier = Modifier.size(60.dp, 32.dp))
                            }
                            Icon(
                                painter = painterResource(id = if (profileSelected) R.drawable.person_active else R.drawable.person),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.Unspecified
                            )
                        }
                    },
                    label = { Text("Profil", color = if (profileSelected) Color.White else Color.Gray) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Selamat Datang")
                }
            }
            composable("sale") {
                SaleScreen(
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
            composable("profile") {
                ProfileScreen(
                    onNavigateToItems = { navController.navigate("items") },
                    onNavigateToEditStock = { navController.navigate("edit_stock") },
                    onNavigateToReport = { navController.navigate("report") }
                )
            }
            // Halaman detail (diakses dari Profil)
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
            composable("report") {
                ReportScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
