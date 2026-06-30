package com.tazfan.inventoryassistant.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tazfan.inventoryassistant.ui.theme.InventoryAssistantTheme

@Composable
fun ProfileScreen(
    onNavigateToItems: () -> Unit,
    onNavigateToReport: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Toko
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2E1A))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Store, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = "Toko Kelontong Berkah", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = "Admin Mode", style = MaterialTheme.typography.bodyMedium, color = Color.LightGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Tombol Manajemen
            ProfileMenuButton(
                text = "Manajemen Stok & Harga",
                icon = Icons.Default.Inventory,
                onClick = onNavigateToItems
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
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color(0xFF1B2E1A),
        ),
        border = BorderStroke(1.dp, Color(0xFF51BF55)),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    InventoryAssistantTheme {
        ProfileScreen({}, {})
    }
}
