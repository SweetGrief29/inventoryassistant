package com.tazfan.inventoryassistant.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sales")
data class Sale(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val itemId: Int,
    val itemName: String,
    val quantity: Int,
    val costPrice: Double,
    val sellingPrice: Double,
    val timestamp: Long = System.currentTimeMillis()
)
