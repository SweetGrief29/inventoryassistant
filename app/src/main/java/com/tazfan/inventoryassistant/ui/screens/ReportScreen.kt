package com.tazfan.inventoryassistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tazfan.inventoryassistant.data.local.entity.Sale
import com.tazfan.inventoryassistant.ui.theme.InventoryAssistantTheme
import com.tazfan.inventoryassistant.ui.viewmodel.InventoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    viewModel: InventoryViewModel,
    onNavigateBack: () -> Unit
) {
    val allSales by viewModel.allSales.collectAsState()
    ReportContent(
        allSales = allSales,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportContent(
    allSales: List<Sale>,
    onNavigateBack: () -> Unit
) {
    val totalRevenue = allSales.sumOf { it.sellingPrice * it.quantity }
    val totalCost = allSales.sumOf { it.costPrice * it.quantity }
    val totalProfit = totalRevenue - totalCost

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Laporan Keuntungan") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Total Laporan Keseluruhan", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Total Penjualan: Rp $totalRevenue")
                    Text(text = "Total Modal: Rp $totalCost")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(
                        text = "Total Untung: Rp $totalProfit",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReportScreenPreview() {
    InventoryAssistantTheme {
        ReportContent(
            allSales = listOf(
                Sale(1, 1, "Beras 5kg", 2, 60000.0, 75000.0, System.currentTimeMillis()),
                Sale(2, 2, "Minyak Goreng 2L", 1, 28000.0, 32000.0, System.currentTimeMillis())
            ),
            onNavigateBack = {}
        )
    }
}
