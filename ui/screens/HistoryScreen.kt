package com.tazfan.inventoryassistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat Penjualan") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sales) { sale ->
                HistoryItemCard(sale)
            }
        }
    }
}

@Composable
fun HistoryItemCard(sale: Sale) {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    val dateString = sdf.format(Date(sale.timestamp))
    
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = sale.itemName, fontWeight = FontWeight.Bold)
                Text(text = "Rp ${sale.sellingPrice * sale.quantity}")
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${sale.quantity} pcs", style = MaterialTheme.typography.bodySmall)
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
