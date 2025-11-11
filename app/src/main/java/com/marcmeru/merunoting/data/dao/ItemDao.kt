package com.marcmeru.merunoting.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.marcmeru.merunoting.data.entity.Item

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item): Long

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getById(id: Long): Item?

    @Query("SELECT * FROM items WHERE parent_id IS NULL ORDER BY name")
    suspend fun getRootItems(): List<Item>

    @Query("SELECT * FROM items WHERE parent_id = :parentId ORDER BY name")
    suspend fun getChildren(parentId: Long): List<Item>

    @Query("SELECT * FROM items ORDER BY name")
    suspend fun getAllItems(): List<Item>

    @Transaction
    suspend fun deleteWithChildren(item: Item) {
        val children = getChildren(item.id)
        children.forEach { deleteWithChildren(it) }
        delete(item)
    }
}
