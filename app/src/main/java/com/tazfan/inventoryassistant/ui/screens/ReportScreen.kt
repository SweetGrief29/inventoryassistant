package com.tazfan.inventoryassistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tazfan.inventoryassistant.data.local.entity.Sale
import com.tazfan.inventoryassistant.ui.theme.InventoryAssistantTheme
import com.tazfan.inventoryassistant.ui.viewmodel.InventoryViewModel
import java.util.Locale

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
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2E1A))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "Ringkasan Pendapatan", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    ReportRow("Total Penjualan", totalRevenue)
                    ReportRow("Total Modal", totalCost)
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.Gray.copy(alpha = 0.5f))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total Untung",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Rp ${String.format(Locale.GERMANY, "%,.0f", totalProfit)}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReportRow(label: String, value: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.LightGray)
        Text(text = "Rp ${String.format(Locale.GERMANY, "%,.0f", value)}", color = Color.White, fontWeight = FontWeight.Medium)
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
