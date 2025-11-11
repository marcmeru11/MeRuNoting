package com.marcmeru.merunoting.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity representing an item which can be a folder or a note,
 * organized in a hierarchical structure using a parent-child relationship.
 *
 * @property id Unique identifier for the item.
 * @property parentId Identifier of the parent item, null if it is a root item.
 * @property name Name of the item.
 * @property type The type of the item, e.g., "folder" or "note".
 * @property content Content of the item; only applicable if type is "note".
 * @property createdAt Timestamp when the item was created.
 * @property updatedAt Timestamp when the item was last updated.
 */
@Entity(
    tableName = "items",
    foreignKeys = [
        ForeignKey(
            entity = Item::class,
            parentColumns = ["id"],
            childColumns = ["parent_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("parent_id")]
)
data class Item(
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "parent_id")
    val parentId: Long?,

    val name: String,

    val type: String,

    val content: String? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
