package com.tazfan.inventoryassistant.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tazfan.inventoryassistant.ui.theme.InventoryAssistantTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSale: () -> Unit,
    onNavigateToItems: () -> Unit,
    onNavigateToEditStock: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToReport: () -> Unit
) {
    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("Beranda - Segera Hadir")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    InventoryAssistantTheme {
        HomeScreen({}, {}, {}, {}, {})
    }
}
