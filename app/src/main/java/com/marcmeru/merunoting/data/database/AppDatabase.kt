package com.marcmeru.merunoting.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.marcmeru.merunoting.data.dao.ItemDao
import com.marcmeru.merunoting.data.entity.Item

/**
 * Room database class for the application.
 *
 * Defines the database configuration and serves as the app's main access point
 * to the persisted data for [Item] entities.
 */
@Database(entities = [Item::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to DAO for [Item] entity operations.
     */
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        /**
         * Gets the singleton instance of [AppDatabase].
         *
         * @param context The application context.
         * @return The singleton database instance.
         */
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
