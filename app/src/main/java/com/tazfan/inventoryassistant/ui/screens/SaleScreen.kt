package com.tazfan.inventoryassistant.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tazfan.inventoryassistant.data.local.entity.Item
import com.tazfan.inventoryassistant.ui.viewmodel.InventoryViewModel
import androidx.compose.ui.tooling.preview.Preview
import com.tazfan.inventoryassistant.ui.theme.InventoryAssistantTheme
import java.util.Locale

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

@Composable
fun SaleContent(
    items: List<Item>,
    onProcessSale: (Item, Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Semua") }
    var selectedItemForSale by remember { mutableStateOf<Item?>(null) }
    var quantityInput by remember { mutableStateOf("1") }
    var showQuantityDialog by remember { mutableStateOf(false) }

    val filteredItems = items.filter {
        (selectedCategory == "Semua" || it.category == selectedCategory) &&
        it.name.contains(searchQuery, ignoreCase = true)
    }

    if (showQuantityDialog && selectedItemForSale != null) {
        AlertDialog(
            onDismissRequest = { showQuantityDialog = false },
            title = { Text("Proses Penjualan", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(selectedItemForSale!!.name, fontWeight = FontWeight.SemiBold)
                    Text("Stok: ${selectedItemForSale!!.stock}", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = quantityInput,
                        onValueChange = { if (it.isEmpty() || it.toIntOrNull() != null) quantityInput = it },
                        label = { Text("Jumlah") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    val q = quantityInput.toIntOrNull() ?: 0
                    val total = selectedItemForSale!!.sellingPrice * q
                    Text(
                        text = "Total: Rp ${String.format(Locale.GERMANY, "%,.0f", total)}",
                        modifier = Modifier.padding(top = 8.dp),
                        color = Color(0xFF51BF55),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val q = quantityInput.toIntOrNull() ?: 0
                        if (q > 0 && q <= selectedItemForSale!!.stock) {
                            onProcessSale(selectedItemForSale!!, q)
                            showQuantityDialog = false
                            quantityInput = "1"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF51BF55)),
                    enabled = (quantityInput.toIntOrNull() ?: 0) > 0 && (quantityInput.toIntOrNull() ?: 0) <= selectedItemForSale!!.stock
                ) {
                    Text("Jual")
                }
            },
            dismissButton = {
                TextButton(onClick = { showQuantityDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            placeholder = { Text("Cari nama barang...", color = Color.Gray, fontSize = 14.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black) },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF51BF55),
                unfocusedBorderColor = Color(0xFF51BF55),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Kategori
        val categories = listOf("Semua", "Makanan", "Minuman", "Lainnya")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                CategoryChip(
                    label = category,
                    isSelected = selectedCategory == category,
                    onClick = { selectedCategory = category }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Grid Barang
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(filteredItems) { item ->
                ItemGridCard(
                    item = item,
                    onClick = {
                        selectedItemForSale = item
                        showQuantityDialog = true
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) Color(0xFF51BF55) else Color.White,
        border = if (isSelected) null else BorderStroke(1.dp, Color(0xFF51BF55))
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            color = if (isSelected) Color.White else Color(0xFF51BF55),
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun ItemGridCard(item: Item, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFF51BF55).copy(alpha = 0.3f)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF2F2F2)),
                contentAlignment = Alignment.Center
            ) {
                if (!item.imagePath.isNullOrEmpty()) {
                    AsyncImage(
                        model = item.imagePath,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        tint = Color(0xFFD0D0D0),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.name,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 13.sp,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 2.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SaleScreenPreview() {
    InventoryAssistantTheme {
        SaleContent(
            items = listOf(
                Item(id = 1, name = "Makanan Kering", category = "Makanan", imagePath = null, costPrice = 10000.0, sellingPrice = 15000.0, stock = 20),
                Item(id = 2, name = "Makanan Kering", category = "Makanan", imagePath = null, costPrice = 10000.0, sellingPrice = 15000.0, stock = 20),
                Item(id = 3, name = "Makanan Kering", category = "Makanan", imagePath = null, costPrice = 10000.0, sellingPrice = 15000.0, stock = 20),
                Item(id = 4, name = "Makanan Kering", category = "Makanan", imagePath = null, costPrice = 10000.0, sellingPrice = 15000.0, stock = 20)
            ),
            onProcessSale = { _, _ -> },
            onNavigateBack = {}
        )
    }
}
