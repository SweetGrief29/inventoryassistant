package com.tazfan.inventoryassistant.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val costPrice: Double,
    val sellingPrice: Double,
    val stock: Int
)
