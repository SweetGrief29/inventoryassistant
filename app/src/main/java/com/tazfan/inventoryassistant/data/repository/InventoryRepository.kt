package com.tazfan.inventoryassistant.data.repository

import com.tazfan.inventoryassistant.data.local.dao.InventoryDao
import com.tazfan.inventoryassistant.data.local.entity.Item
import com.tazfan.inventoryassistant.data.local.entity.Sale
import kotlinx.coroutines.flow.Flow

class InventoryRepository(private val inventoryDao: InventoryDao) {
    val allItems: Flow<List<Item>> = inventoryDao.getAllItems()
    val allSales: Flow<List<Sale>> = inventoryDao.getAllSales()

    suspend fun insertItem(item: Item) = inventoryDao.insertItem(item)
    
    suspend fun getItemByName(name: String): Item? = inventoryDao.getItemByName(name)
    
    suspend fun updateStock(id: Int, quantity: Int) = inventoryDao.updateStock(id, quantity)
    
    suspend fun deleteItem(item: Item) = inventoryDao.deleteItem(item)
    
    suspend fun processSale(item: Item, quantity: Int) = inventoryDao.processSale(item, quantity)
    
    fun getDailySales(startOfDay: Long): Flow<List<Sale>> = inventoryDao.getDailySales(startOfDay)
    
    suspend fun getItemById(id: Int): Item? = inventoryDao.getItemById(id)
}
