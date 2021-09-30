package com.example.callblocker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.callblocker.data.local.entity.ContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockListDao {

    @Query("SELECT * FROM contact")
    fun getBlockList(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contact WHERE id = :id")
    suspend fun get(id: Int): ContactEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: ContactEntity)

    @Query("SELECT * FROM contact WHERE phone = :phoneNumber")
    suspend fun search(phoneNumber: String): List<ContactEntity>

    @Query("DELETE FROM contact WHERE id = :id")
    suspend fun delete(id: Int)
}
