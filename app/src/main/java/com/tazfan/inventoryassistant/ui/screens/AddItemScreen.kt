package com.tazfan.inventoryassistant.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tazfan.inventoryassistant.ui.viewmodel.InventoryViewModel
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(
    viewModel: InventoryViewModel,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Makanan") }
    var costPrice by remember { mutableStateOf("") }
    var sellingPrice by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("0") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    
    var showCategoryDialog by remember { mutableStateOf(false) }
    val categories = listOf("Makanan", "Minuman", "Lainnya")

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> imageUri = uri }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Barang", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = "Back", 
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Placeholder Image sesuai wireframe
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp))
                    .clickable { 
                        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.AddAPhoto, 
                        contentDescription = null, 
                        modifier = Modifier.size(60.dp), 
                        tint = Color(0xFFE0E0E0)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ModernTextField(label = "Nama Barang", value = name, onValueChange = { name = it })
            
            ModernCategoryPicker(label = "Kategori", value = category) {
                showCategoryDialog = true
            }

            ModernTextField(label = "Harga Asli", value = costPrice, onValueChange = { costPrice = it }, prefix = "Rp", keyboardType = KeyboardType.Number)
            
            val isLoss = remember(costPrice, sellingPrice) {
                val cp = costPrice.toDoubleOrNull() ?: 0.0
                val sp = sellingPrice.toDoubleOrNull() ?: 0.0
                sp > 0 && sp < cp
            }

            ModernTextField(
                label = "Harga Jual", 
                value = sellingPrice, 
                onValueChange = { sellingPrice = it }, 
                prefix = "Rp", 
                keyboardType = KeyboardType.Number,
                isError = isLoss,
                supportingText = if (isLoss) "Peringatan: Harga jual lebih rendah dari harga asli (Rugi)" else null
            )
            
            ModernTextField(label = "Stok Awal", value = stock, onValueChange = { stock = it }, keyboardType = KeyboardType.Number)

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        var finalImagePath: String? = null
                        
                        imageUri?.let { uri ->
                            try {
                                val inputStream = context.contentResolver.openInputStream(uri)
                                val file = File(context.filesDir, "item_${System.currentTimeMillis()}.jpg")
                                val outputStream = FileOutputStream(file)
                                inputStream?.use { input ->
                                    outputStream.use { output ->
                                        input.copyTo(output)
                                    }
                                }
                                finalImagePath = file.absolutePath
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        viewModel.insertItem(
                            name = name,
                            category = category,
                            costPrice = costPrice.toDoubleOrNull() ?: 0.0,
                            sellingPrice = sellingPrice.toDoubleOrNull() ?: 0.0,
                            stock = stock.toIntOrNull() ?: 0,
                            imagePath = finalImagePath
                        )
                        onNavigateBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Simpan", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showCategoryDialog) {
        AlertDialog(
            onDismissRequest = { showCategoryDialog = false },
            title = { Text("Pilih Kategori") },
            text = {
                Column {
                    categories.forEach { cat ->
                        Text(
                            text = cat,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    category = cat
                                    showCategoryDialog = false
                                }
                                .padding(16.dp)
                        )
                    }
                }
            },
            confirmButton = {}
        )
    }
}

@Composable
fun InputRowItem(label: String, value: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4CAF50))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, color = Color.Gray, fontSize = 14.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = value, color = Color.Black, fontSize = 14.sp)
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
            }
        }
    }
}

@Composable
fun ModernTextField(
    label: String, 
    value: String, 
    onValueChange: (String) -> Unit, 
    prefix: String? = null, 
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    supportingText: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        prefix = prefix?.let { { Text(it) } },
        isError = isError,
        supportingText = supportingText?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF4CAF50),
            unfocusedBorderColor = Color(0xFFE0E0E0)
        )
    )
}

@Composable
fun ModernCategoryPicker(label: String, value: String, onClick: () -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        enabled = false,
        readOnly = true,
        trailingIcon = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = Color(0xFFE0E0E0),
            disabledTextColor = Color.Black,
            disabledLabelColor = Color.Gray
        )
    )
}
