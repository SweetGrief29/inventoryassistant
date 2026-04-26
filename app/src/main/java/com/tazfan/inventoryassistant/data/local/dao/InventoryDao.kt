package com.tazfan.inventoryassistant.data.local.dao

import androidx.room.*
import com.tazfan.inventoryassistant.data.local.entity.Item
import com.tazfan.inventoryassistant.data.local.entity.Sale
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDao {
    // Items
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: Item)

    @Update
    suspend fun updateItem(item: Item)

    @Query("SELECT * FROM items ORDER BY name ASC")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getItemById(id: Int): Item?

    @Query("UPDATE items SET stock = stock + :quantity WHERE id = :id")
    suspend fun updateStock(id: Int, quantity: Int)

    // Sales
    @Insert
    suspend fun insertSale(sale: Sale)

    @Transaction
    suspend fun processSale(item: Item, quantity: Int) {
        val updatedItem = item.copy(stock = item.stock - quantity)
        updateItem(updatedItem)
        
        val sale = Sale(
            itemId = item.id,
            itemName = item.name,
            quantity = quantity,
            costPrice = item.costPrice,
            sellingPrice = item.sellingPrice
        )
        insertSale(sale)
    }

    @Query("SELECT * FROM sales ORDER BY timestamp DESC")
    fun getAllSales(): Flow<List<Sale>>

    @Query("SELECT * FROM sales WHERE timestamp >= :startOfDay ORDER BY timestamp DESC")
    fun getDailySales(startOfDay: Long): Flow<List<Sale>>
}
