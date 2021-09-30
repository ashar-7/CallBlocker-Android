package com.example.callblocker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.callblocker.data.local.entity.ContactEntity

@Database(entities = [ContactEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun blockListDao(): BlockListDao
}
