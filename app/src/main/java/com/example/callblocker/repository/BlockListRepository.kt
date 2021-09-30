package com.example.callblocker.repository

import com.example.callblocker.model.Contact
import kotlinx.coroutines.flow.Flow

interface BlockListRepository {

    fun getBlockList(): Flow<List<Contact>>
    suspend fun getBlockedContact(id: Int): Contact?
    suspend fun addToBlockList(contact: Contact)
    suspend fun search(phoneNumber: String): List<Contact>
    suspend fun unblock(id: Int)
}
