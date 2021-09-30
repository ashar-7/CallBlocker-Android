package com.example.callblocker.repository

import com.example.callblocker.data.local.BlockListDao
import com.example.callblocker.mapper.ContactMapper.toDomain
import com.example.callblocker.mapper.ContactMapper.toEntity
import com.example.callblocker.model.Contact
import com.example.callblocker.util.transformNumber
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BlockListRepositoryImpl @Inject constructor(
    private val blockListDao: BlockListDao
) : BlockListRepository {

    override fun getBlockList(): Flow<List<Contact>> =
        blockListDao.getBlockList().map { list ->
            list.map { it.toDomain() }
        }

    override suspend fun getBlockedContact(id: Int): Contact? =
        blockListDao.get(id)?.toDomain()

    override suspend fun addToBlockList(contact: Contact) {
        blockListDao.insert(contact.toEntity())
    }

    override suspend fun search(phoneNumber: String): List<Contact> =
        blockListDao.search(transformNumber(phoneNumber)).map { it.toDomain() }

    override suspend fun unblock(id: Int) {
        blockListDao.delete(id)
    }
}
