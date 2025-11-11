package com.marcmeru.merunoting.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.marcmeru.merunoting.data.dao.ItemDao
import com.marcmeru.merunoting.data.entity.Item
import androidx.room.Room


@Database(entities = [Item::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build().also { instance = it }
            }
    }
}
