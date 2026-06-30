package com.tazfan.inventoryassistant.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .statusBarsPadding()
        ) {
            // Header Custom
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
                Text(
                    text = "Riwayat Transaksi",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            // Ringkasan Keuntungan
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF51BF55))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Total Keuntungan", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
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
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
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
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = sale.itemName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    text = "+ Rp ${String.format(Locale.GERMANY, "%,.0f", profit)}", 
                    color = Color(0xFF51BF55),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${sale.quantity} pcs x Rp ${String.format(Locale.GERMANY, "%,.0f", sale.sellingPrice)}", 
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = dateString, 
                    style = MaterialTheme.typography.bodySmall, 
                    color = Color.LightGray
                )
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
