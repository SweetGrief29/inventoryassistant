package com.tazfan.inventoryassistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tazfan.inventoryassistant.ui.theme.InventoryAssistantTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSale: () -> Unit,
    onNavigateToItems: () -> Unit,
    onNavigateToEditStock: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToReport: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Inventory Assistant") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tombol Penjualan (Besar di atas)
            MenuButtonLarge(
                text = "Penjualan",
                icon = Icons.Default.ShoppingCart,
                onClick = onNavigateToSale
            )

            // Grid 2x2 untuk tombol lainnya
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    MenuButtonSmall(
                        text = "Cek Stok",
                        icon = Icons.Default.Inventory,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToItems
                    )
                    MenuButtonSmall(
                        text = "Edit Stok",
                        icon = Icons.Default.Edit,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToEditStock
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    MenuButtonSmall(
                        text = "Riwayat Penjualan",
                        icon = Icons.Default.ListAlt,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToHistory
                    )
                    MenuButtonSmall(
                        text = "Laporan",
                        icon = Icons.Default.BarChart,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToReport
                    )
                }
            }
        }
    }
}

@Composable
fun MenuButtonLarge(text: String, icon: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = MaterialTheme.shapes.medium,
        contentPadding = PaddingValues(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = text, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MenuButtonSmall(text: String, icon: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(120.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = text, fontWeight = FontWeight.Medium, softWrap = true, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    InventoryAssistantTheme {
        HomeScreen({}, {}, {}, {}, {})
    }
}
