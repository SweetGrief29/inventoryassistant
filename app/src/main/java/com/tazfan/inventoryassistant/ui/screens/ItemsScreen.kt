package com.tazfan.inventoryassistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tazfan.inventoryassistant.data.local.entity.Item
import com.tazfan.inventoryassistant.ui.viewmodel.InventoryViewModel

import androidx.compose.ui.tooling.preview.Preview
import com.tazfan.inventoryassistant.ui.theme.InventoryAssistantTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(
    viewModel: InventoryViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAddItem: () -> Unit
) {
    val items by viewModel.allItems.collectAsState()
    ItemsContent(
        items = items,
        onNavigateBack = onNavigateBack,
        onNavigateToAddItem = onNavigateToAddItem
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsContent(
    items: List<Item>,
    onNavigateBack: () -> Unit,
    onNavigateToAddItem: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Data Barang") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddItem) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Barang")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { item ->
                ItemCard(item)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemsScreenPreview() {
    InventoryAssistantTheme {
        ItemsContent(
            items = listOf(
                Item(1, "Beras 5kg", 60000.0, 75000.0, 10),
                Item(2, "Minyak Goreng 2L", 28000.0, 32000.0, 5)
            ),
            onNavigateBack = {},
            onNavigateToAddItem = {}
        )
    }
}

@Composable
fun ItemCard(item: Item) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Modal: Rp ${item.costPrice}")
                Text(text = "Jual: Rp ${item.sellingPrice}")
            }
            Text(text = "Stok: ${item.stock}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
