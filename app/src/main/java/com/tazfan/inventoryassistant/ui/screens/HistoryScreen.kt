package com.tazfan.inventoryassistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.sp
import com.tazfan.inventoryassistant.data.local.entity.Sale
import com.tazfan.inventoryassistant.ui.theme.InventoryAssistantTheme
import com.tazfan.inventoryassistant.ui.viewmodel.InventoryViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: InventoryViewModel,
    onNavigateBack: () -> Unit
) {
    val allSales by viewModel.allSales.collectAsState()
    HistoryContent(allSales, onNavigateBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryContent(
    sales: List<Sale>,
    onNavigateBack: () -> Unit
) {
    val totalProfit = sales.sumOf { (it.sellingPrice - it.costPrice) * it.quantity }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat & Keuntungan") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Ringkasan Keuntungan
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2E1A))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Total Keuntungan", color = Color.White, fontSize = 14.sp)
                    Text(
                        text = "Rp ${String.format(Locale.GERMANY, "%,.0f", totalProfit)}",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "Daftar Transaksi",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                fontWeight = FontWeight.Bold
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sales) { sale ->
                    HistoryItemCard(sale)
                }
            }
        }
    }
}

@Composable
fun HistoryItemCard(sale: Sale) {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    val dateString = sdf.format(Date(sale.timestamp))
    val profit = (sale.sellingPrice - sale.costPrice) * sale.quantity
    
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = sale.itemName, fontWeight = FontWeight.Bold)
                Text(text = "Profit: Rp ${String.format(Locale.GERMANY, "%,.0f", profit)}", color = Color(0xFF4CAF50))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${sale.quantity} pcs x Rp ${String.format(Locale.GERMANY, "%,.0f", sale.sellingPrice)}", style = MaterialTheme.typography.bodySmall)
                Text(text = dateString, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    InventoryAssistantTheme {
        HistoryContent(
            sales = listOf(
                Sale(1, 1, "Beras 5kg", 2, 60000.0, 75000.0, System.currentTimeMillis()),
                Sale(2, 2, "Minyak Goreng 2L", 1, 28000.0, 32000.0, System.currentTimeMillis() - 86400000)
            ),
            onNavigateBack = {}
        )
    }
}
