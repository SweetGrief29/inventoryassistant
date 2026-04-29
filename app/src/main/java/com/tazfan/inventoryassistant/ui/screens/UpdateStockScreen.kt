package com.tazfan.inventoryassistant.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tazfan.inventoryassistant.data.local.entity.Item
import com.tazfan.inventoryassistant.ui.viewmodel.InventoryViewModel

import androidx.compose.ui.tooling.preview.Preview
import com.tazfan.inventoryassistant.ui.theme.InventoryAssistantTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateStockScreen(
    viewModel: InventoryViewModel,
    onNavigateBack: () -> Unit
) {
    val items by viewModel.allItems.collectAsState()
    UpdateStockContent(
        items = items,
        onUpdateStock = { itemId, q -> viewModel.updateStock(itemId, q) },
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateStockContent(
    items: List<Item>,
    onUpdateStock: (Int, Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    var selectedItem by remember { mutableStateOf<Item?>(null) }
    var quantity by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Stok") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box {
                OutlinedTextField(
                    value = selectedItem?.name ?: "Pilih Barang",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Barang") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, "Dropdown", Modifier.clickable { expanded = true })
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.name) },
                            onClick = {
                                selectedItem = item
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Jumlah Tambahan") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(
                onClick = {
                    val q = quantity.toIntOrNull() ?: 0
                    if (selectedItem != null && q > 0) {
                        onUpdateStock(selectedItem!!.id, q)
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tambah Stok")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UpdateStockScreenPreview() {
    InventoryAssistantTheme {
        UpdateStockContent(
            items = listOf(
                Item(id = 1, name = "Beras 5kg", category = "Lainnya", imagePath = null, costPrice = 60000.0, sellingPrice = 75000.0, stock = 10),
                Item(id = 2, name = "Minyak Goreng 2L", category = "Lainnya", imagePath = null, costPrice = 28000.0, sellingPrice = 32000.0, stock = 5)
            ),
            onUpdateStock = { _, _ -> },
            onNavigateBack = {}
        )
    }
}
