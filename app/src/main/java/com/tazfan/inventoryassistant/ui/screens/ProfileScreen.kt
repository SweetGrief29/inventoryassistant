package com.tazfan.inventoryassistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Store
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
fun ProfileScreen(
    onNavigateToItems: () -> Unit,
    onNavigateToEditStock: () -> Unit,
    onNavigateToReport: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Profil & Manajemen") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Toko
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Store, contentDescription = null, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = "Nama Toko Anda", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Admin Mode", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            Text(text = "Manajemen Inventaris", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))

            // Tombol Manajemen
            ProfileMenuButton(
                text = "Cek Stok Barang",
                icon = Icons.Default.Inventory,
                onClick = onNavigateToItems
            )
            
            ProfileMenuButton(
                text = "Edit Stok & Tambah Barang",
                icon = Icons.Default.Edit,
                onClick = onNavigateToEditStock
            )

            ProfileMenuButton(
                text = "Laporan Keuntungan",
                icon = Icons.Default.Add, // Bisa diganti icon report
                onClick = onNavigateToReport
            )
        }
    }
}

@Composable
fun ProfileMenuButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(60.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = text)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    InventoryAssistantTheme {
        ProfileScreen({}, {}, {})
    }
}
