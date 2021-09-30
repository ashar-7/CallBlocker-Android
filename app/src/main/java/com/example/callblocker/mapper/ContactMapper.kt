package com.example.callblocker.mapper

import com.example.callblocker.data.local.entity.ContactEntity
import com.example.callblocker.model.Contact
import com.example.callblocker.util.transformNumber

object ContactMapper {
    fun Contact.toEntity() =
        ContactEntity(id = id ?: 0, name = name, phoneNumber = transformNumber(phoneNumber))

    fun ContactEntity.toDomain() =
        Contact(id = id, name = name, phoneNumber = phoneNumber)
}
