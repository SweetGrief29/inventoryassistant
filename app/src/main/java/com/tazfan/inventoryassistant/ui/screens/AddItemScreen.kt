package com.tazfan.inventoryassistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tazfan.inventoryassistant.ui.viewmodel.InventoryViewModel

import androidx.compose.ui.tooling.preview.Preview
import com.tazfan.inventoryassistant.ui.theme.InventoryAssistantTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(
    viewModel: InventoryViewModel,
    onNavigateBack: () -> Unit
) {
    AddItemContent(
        onSaveItem = { name, cost, sell, stock ->
            viewModel.insertItem(name, cost, sell, stock)
        },
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemContent(
    onSaveItem: (String, Double, Double, Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var costPrice by remember { mutableStateOf("") }
    var sellingPrice by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Barang Baru") },
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama Barang") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = costPrice,
                onValueChange = { costPrice = it },
                label = { Text("Harga Modal") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = sellingPrice,
                onValueChange = { sellingPrice = it },
                label = { Text("Harga Jual") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Stok Awal") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (name.isNotBlank() && costPrice.isNotBlank() && sellingPrice.isNotBlank() && stock.isNotBlank()) {
                        onSaveItem(
                            name,
                            costPrice.toDoubleOrNull() ?: 0.0,
                            sellingPrice.toDoubleOrNull() ?: 0.0,
                            stock.toIntOrNull() ?: 0
                        )
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddItemScreenPreview() {
    InventoryAssistantTheme {
        AddItemContent(
            onSaveItem = { _, _, _, _ -> },
            onNavigateBack = {}
        )
    }
}
