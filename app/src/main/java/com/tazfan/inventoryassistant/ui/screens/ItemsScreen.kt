package com.tazfan.inventoryassistant.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tazfan.inventoryassistant.R
import com.tazfan.inventoryassistant.data.local.entity.Item
import com.tazfan.inventoryassistant.ui.theme.InventoryAssistantTheme
import com.tazfan.inventoryassistant.ui.viewmodel.InventoryViewModel
import java.util.Locale

enum class FabMode {
    NONE, ADD_NEW, UPDATE_STOCK, UPDATE_PRICE, DELETE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(
    viewModel: InventoryViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAddItem: () -> Unit
) {
    val items by viewModel.allItems.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Semua") }
    
    var fabExpanded by remember { mutableStateOf(false) }
    var currentFabMode by remember { mutableStateOf(FabMode.NONE) }

    val pendingChanges = remember { mutableStateMapOf<Int, Item>() }
    var itemToDelete by remember { mutableStateOf<Item?>(null) }
    var showExitConfirmation by remember { mutableStateOf(false) }

    val filteredItems = items.filter {
        (selectedCategory == "Semua" || it.category == selectedCategory) &&
        it.name.contains(searchQuery, ignoreCase = true)
    }

    BackHandler(enabled = currentFabMode != FabMode.NONE || pendingChanges.isNotEmpty()) {
        if (pendingChanges.isNotEmpty()) {
            showExitConfirmation = true
        } else {
            currentFabMode = FabMode.NONE
        }
    }

    // Dialog Konfirmasi Hapus
    if (itemToDelete != null) {
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            title = { Text("Hapus Barang") },
            text = { Text("Apakah Anda yakin ingin menghapus ${itemToDelete?.name}?") },
            confirmButton = {
                TextButton(onClick = {
                    itemToDelete?.let { viewModel.deleteItem(it) }
                    itemToDelete = null
                }) {
                    Text("Hapus", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) {
                    Text("Batal")
                }
            }
        )
    }

    // Dialog Konfirmasi Keluar (Unsaved Changes)
    if (showExitConfirmation) {
        AlertDialog(
            onDismissRequest = { showExitConfirmation = false },
            title = { Text("Simpan Perubahan?") },
            text = { Text("Ada perubahan yang belum disimpan. Ingin menyimpannya sebelum keluar?") },
            confirmButton = {
                TextButton(onClick = {
                    pendingChanges.values.forEach { viewModel.updateItem(it) }
                    pendingChanges.clear()
                    showExitConfirmation = false
                    onNavigateBack()
                }) {
                    Text("Simpan & Keluar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    pendingChanges.clear()
                    showExitConfirmation = false
                    onNavigateBack()
                }) {
                    Text("Buang & Keluar", color = Color.Red)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Stok Barang", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (pendingChanges.isNotEmpty()) {
                            showExitConfirmation = true
                        } else if (currentFabMode != FabMode.NONE) {
                            currentFabMode = FabMode.NONE
                        } else {
                            onNavigateBack()
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF4CAF50))
                    }
                },
                actions = {
                    if (pendingChanges.isNotEmpty()) {
                        Button(
                            onClick = {
                                pendingChanges.values.forEach { viewModel.updateItem(it) }
                                pendingChanges.clear()
                                currentFabMode = FabMode.NONE
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Simpan", color = Color.White)
                        }
                    } else if (currentFabMode != FabMode.NONE) {
                        TextButton(
                            onClick = { currentFabMode = FabMode.NONE },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Selesai", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                AnimatedVisibility(
                    visible = fabExpanded,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        FabSubButton(label = "Barang Baru", icon = Icons.Default.Add) {
                            onNavigateToAddItem()
                            fabExpanded = false
                        }
                        FabSubButton(label = "Update Stok", icon = Icons.Default.Edit) {
                            currentFabMode = if (currentFabMode == FabMode.UPDATE_STOCK) FabMode.NONE else FabMode.UPDATE_STOCK
                            fabExpanded = false
                        }
                        FabSubButton(label = "Update Harga", icon = Icons.Default.Edit) {
                            currentFabMode = if (currentFabMode == FabMode.UPDATE_PRICE) FabMode.NONE else FabMode.UPDATE_PRICE
                            fabExpanded = false
                        }
                        FabSubButton(label = "Hapus Barang", icon = Icons.Default.Delete) {
                            currentFabMode = if (currentFabMode == FabMode.DELETE) FabMode.NONE else FabMode.DELETE
                            fabExpanded = false
                        }
                    }
                }
                
                FloatingActionButton(
                    onClick = { fabExpanded = !fabExpanded },
                    containerColor = if (fabExpanded) Color.White else Color(0xFF4CAF50),
                    contentColor = if (fabExpanded) Color.Gray else Color.White,
                    shape = CircleShape
                ) {
                    Icon(
                        if (fabExpanded) Icons.Default.Close else Icons.Default.Add,
                        contentDescription = "Menu"
                    )
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Cari nama barang...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4CAF50),
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )

            // Filter Kategori (Horizontal Chips)
            val categories = listOf("Semua", "Makanan", "Minuman", "Lainnya")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF4CAF50),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredItems) { item ->
                    val displayItem = pendingChanges[item.id] ?: item
                    ItemCard(
                        item = displayItem, 
                        mode = currentFabMode,
                        onUpdateStock = { newStock ->
                            pendingChanges[item.id] = displayItem.copy(stock = newStock)
                        },
                        onUpdatePrice = { newPrice ->
                            pendingChanges[item.id] = displayItem.copy(sellingPrice = newPrice)
                        },
                        onDelete = {
                            itemToDelete = item
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FabSubButton(label: String, icon: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, color = Color.Black, fontSize = 14.sp)
        }
    }
}

@Composable
fun ItemCard(
    item: Item, 
    mode: FabMode,
    onUpdateStock: (Int) -> Unit,
    onUpdatePrice: (Double) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gambar Barang
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF0F0F0)),
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
                    Text(item.name.take(1).uppercase(), fontSize = 24.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(text = item.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = item.category, fontSize = 12.sp, color = Color.Gray)
                    }
                    if (mode == FabMode.DELETE) {
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Bagian Stok
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (mode == FabMode.UPDATE_STOCK) {
                            IconButton(onClick = { if (item.stock > 0) onUpdateStock(item.stock - 1) }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.RemoveCircleOutline, contentDescription = null)
                            }
                            Text(
                                text = item.stock.toString(), 
                                fontWeight = FontWeight.Bold, 
                                modifier = Modifier.padding(horizontal = 8.dp),
                                color = if (item.stock < 5) Color.Red else Color.Black
                            )
                            IconButton(onClick = { onUpdateStock(item.stock + 1) }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.AddCircleOutline, contentDescription = null)
                            }
                        } else {
                            val stockColor = if (item.stock < 5) Color.Red else Color.Black
                            Text(text = "Stok: ${item.stock}", color = stockColor)
                        }
                    }

                    // Bagian Harga
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (mode == FabMode.UPDATE_PRICE) {
                            var priceText by remember(item.sellingPrice) { 
                                mutableStateOf(if (item.sellingPrice % 1 == 0.0) item.sellingPrice.toLong().toString() else item.sellingPrice.toString()) 
                            }
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFF5F5F5), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Black)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    BasicTextField(
                                        value = priceText,
                                        onValueChange = { 
                                            if (it.isEmpty() || it.toDoubleOrNull() != null) {
                                                priceText = it
                                                it.toDoubleOrNull()?.let { newPrice -> onUpdatePrice(newPrice) }
                                            }
                                        },
                                        textStyle = TextStyle(
                                            fontWeight = FontWeight.Bold, 
                                            color = Color.Black, 
                                            fontSize = 16.sp,
                                            textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                                        ),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        decorationBox = { innerTextField ->
                                            Row {
                                                Text("Rp ", fontWeight = FontWeight.Bold, color = Color.Black)
                                                innerTextField()
                                            }
                                        }
                                    )
                                }
                            }
                        } else {
                            Text(
                                text = "Rp ${String.format(Locale.GERMANY, "%,.0f", item.sellingPrice)}", 
                                fontWeight = FontWeight.Bold, 
                                color = Color(0xFF4CAF50)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemsScreenPreview() {
    InventoryAssistantTheme {
        ItemsContentPreview(
            items = listOf(
                Item(id = 1, name = "Beras 5kg", category = "Lainnya", imagePath = null, costPrice = 60000.0, sellingPrice = 75000.0, stock = 10),
                Item(id = 2, name = "Minyak Goreng 2L", category = "Lainnya", imagePath = null, costPrice = 28000.0, sellingPrice = 32000.0, stock = 5)
            )
        )
    }
}

@Composable
fun ItemsContentPreview(items: List<Item>) {
    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding)) {
            items.forEach { 
                ItemCard(
                    item = it, 
                    mode = FabMode.NONE,
                    onUpdateStock = {},
                    onUpdatePrice = {},
                    onDelete = {}
                ) 
            }
        }
    }
}
