package com.marcmeru.merunoting.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.marcmeru.merunoting.data.entity.Item

/**
 * Data Access Object (DAO) for performing database operations on [Item] entities.
 *
 * Provides methods to insert, update, delete, query items, and recursively delete folders with their children.
 */
@Dao
interface ItemDao {

    /**
     * Inserts an [item] into the database.
     * Replaces the existing item if a conflict occurs.
     *
     * @param item The item to insert.
     * @return The row ID of the inserted item.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item): Long

    /**
     * Updates an existing [item] in the database.
     *
     * @param item The item to update.
     */
    @Update
    suspend fun update(item: Item)

    /**
     * Deletes the given [item] from the database.
     *
     * @param item The item to delete.
     */
    @Delete
    suspend fun delete(item: Item)

    /**
     * Retrieves the item with the specified [id].
     *
     * @param id The unique identifier for the item.
     * @return The item if found, or null.
     */
    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getById(id: Long): Item?

    /**
     * Retrieves all root-level items (items with no parent).
     *
     * @return List of root items ordered by name.
     */
    @Query("SELECT * FROM items WHERE parent_id IS NULL ORDER BY name")
    suspend fun getRootItems(): List<Item>

    /**
     * Retrieves all direct children of a parent item identified by [parentId].
     *
     * @param parentId The ID of the parent item.
     * @return List of child items ordered by name.
     */
    @Query("SELECT * FROM items WHERE parent_id = :parentId ORDER BY name")
    suspend fun getChildren(parentId: Long): List<Item>

    /**
     * Retrieves all items in the database ordered by name.
     *
     * @return List of all items.
     */
    @Query("SELECT * FROM items ORDER BY name")
    suspend fun getAllItems(): List<Item>

    /**
     * Recursively deletes the given [item] and all of its descendants.
     *
     * This method performs a depth-first traversal, deleting children before deleting the parent.
     *
     * @param item The item to delete along with its children.
     */
    @Transaction
    suspend fun deleteWithChildren(item: Item) {
        val children = getChildren(item.id)
        children.forEach { deleteWithChildren(it) }
        delete(item)
    }
}
