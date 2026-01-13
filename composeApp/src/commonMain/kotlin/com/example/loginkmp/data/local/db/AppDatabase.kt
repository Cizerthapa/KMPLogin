package com.example.loginkmp.data.local.db

import com.example.loginkmp.data.local.entity.ProductEntity
import com.example.loginkmp.data.local.dao.ProductDao

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProductEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}


