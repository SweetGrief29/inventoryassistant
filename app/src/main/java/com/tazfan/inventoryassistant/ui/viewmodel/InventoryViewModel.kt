package com.tazfan.inventoryassistant.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tazfan.inventoryassistant.data.local.database.InventoryDatabase
import com.tazfan.inventoryassistant.data.local.entity.Item
import com.tazfan.inventoryassistant.data.local.entity.Sale
import com.tazfan.inventoryassistant.data.repository.InventoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class InventoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: InventoryRepository
    val allItems: StateFlow<List<Item>>
    val allSales: StateFlow<List<Sale>>

    init {
        val dao = InventoryDatabase.getDatabase(application).inventoryDao()
        repository = InventoryRepository(dao)
        allItems = repository.allItems.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        allSales = repository.allSales.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    }

    fun insertItem(name: String, category: String, costPrice: Double, sellingPrice: Double, stock: Int, imagePath: String? = null) {
        viewModelScope.launch {
            val existingItem = repository.getItemByName(name)
            if (existingItem != null) {
                // Jika barang sudah ada, update stok dan detail lainnya (opsional, di sini kita update semuanya)
                repository.insertItem(existingItem.copy(
                    category = category,
                    costPrice = costPrice,
                    sellingPrice = sellingPrice,
                    stock = existingItem.stock + stock,
                    imagePath = imagePath ?: existingItem.imagePath
                ))
            } else {
                repository.insertItem(Item(
                    name = name, 
                    category = category,
                    costPrice = costPrice, 
                    sellingPrice = sellingPrice, 
                    stock = stock,
                    imagePath = imagePath
                ))
            }
        }
    }

    fun updateItem(item: Item) {
        viewModelScope.launch {
            repository.insertItem(item) // In Room, insert with same ID and OnConflictStrategy.REPLACE acts as update
        }
    }

    fun updateStock(itemId: Int, quantity: Int) {
        viewModelScope.launch {
            repository.updateStock(itemId, quantity)
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }

    fun processSale(item: Item, quantity: Int) {
        viewModelScope.launch {
            if (item.stock >= quantity) {
                repository.processSale(item, quantity)
            }
        }
    }

    fun getDailySales(): Flow<List<Sale>> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return repository.getDailySales(calendar.timeInMillis)
    }
}
