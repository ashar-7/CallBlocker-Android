package com.example.callblocker.ui.addtoblocklist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.callblocker.model.Contact
import com.example.callblocker.repository.BlockListRepository
import com.example.callblocker.ui.CONTACT_ID_NAV_ARG_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddToBlockListViewModel @Inject constructor(
    private val blockListRepository: BlockListRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _contact = mutableStateOf(initialContact())
    val contact: State<Contact> = _contact

    init {
        viewModelScope.launch {
            val id = contact.value.id
            if (id != null && id != 0)
                blockListRepository.getBlockedContact(id)?.also {
                    _contact.value = it
                }
        }
    }

    // NOTE: Should probably do validations on name and phone number and notify the UI for invalid data
    // but I'm leaving it out for simplicity
    fun saveContact() {
        viewModelScope.launch {
            val name = contact.value.name.trim()
            val phoneNumber = contact.value.phoneNumber.trim()

            if (name.isNotBlank() && phoneNumber.isNotBlank()) {
                _contact.value = contact.value.copy(name = name, phoneNumber = phoneNumber)
                blockListRepository.addToBlockList(contact.value)
            }
        }
    }

    fun setName(name: String) {
        _contact.value = contact.value.copy(name = name)
    }

    fun setPhoneNumber(phoneNumber: String) {
        _contact.value = contact.value.copy(phoneNumber = phoneNumber)
    }

    private fun initialContact() =
        Contact(
            id = savedStateHandle.get<Int>(CONTACT_ID_NAV_ARG_KEY),
            name = "",
            phoneNumber = ""
        )
}
