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
fun SaleScreen(
    viewModel: InventoryViewModel,
    onNavigateBack: () -> Unit
) {
    val items by viewModel.allItems.collectAsState()
    SaleContent(
        items = items,
        onProcessSale = { item, q -> viewModel.processSale(item, q) },
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleContent(
    items: List<Item>,
    onProcessSale: (Item, Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    var selectedItem by remember { mutableStateOf<Item?>(null) }
    var quantity by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Penjualan") },
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
                            text = { Text("${item.name} (Stok: ${item.stock})") },
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
                label = { Text("Jumlah Terjual") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            if (selectedItem != null) {
                Text(text = "Harga: Rp ${selectedItem!!.sellingPrice}")
                val q = quantity.toIntOrNull() ?: 0
                Text(text = "Total: Rp ${selectedItem!!.sellingPrice * q}")
            }

            Button(
                onClick = {
                    val q = quantity.toIntOrNull() ?: 0
                    if (selectedItem != null && q > 0 && selectedItem!!.stock >= q) {
                        onProcessSale(selectedItem!!, q)
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedItem != null && (quantity.toIntOrNull() ?: 0) > 0 && selectedItem!!.stock >= (quantity.toIntOrNull() ?: 0)
            ) {
                Text("Proses Jual")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SaleScreenPreview() {
    InventoryAssistantTheme {
        SaleContent(
            items = listOf(
                Item(1, "Beras 5kg", 60000.0, 75000.0, 10),
                Item(2, "Minyak Goreng 2L", 28000.0, 32000.0, 5)
            ),
            onProcessSale = { _, _ -> },
            onNavigateBack = {}
        )
    }
}
