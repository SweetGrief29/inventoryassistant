package com.tazfan.inventoryassistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tazfan.inventoryassistant.ui.theme.InventoryAssistantTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditStockScreen(
    onNavigateToAddItem: () -> Unit,
    onNavigateToUpdateStock: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Stok") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onNavigateToUpdateStock,
                modifier = Modifier.fillMaxWidth().height(80.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tambah Stok Barang Ada")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onNavigateToAddItem,
                modifier = Modifier.fillMaxWidth().height(80.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tambah Barang Baru")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditStockScreenPreview() {
    InventoryAssistantTheme {
        EditStockScreen({}, {}, {})
    }
}
