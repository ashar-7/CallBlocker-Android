package com.example.callblocker.ui.blocklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.callblocker.repository.BlockListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlockListViewModel @Inject constructor(
    private val blockListRepository: BlockListRepository
) : ViewModel() {

    val uiState = blockListRepository.getBlockList().map {
        BlockListUiState(blockList = it, isLoading = false)
    }.stateIn(viewModelScope, started = SharingStarted.Lazily, initialValue = initialUiState())

    private fun initialUiState() = BlockListUiState(emptyList(), isLoading = true)

    fun unblock(contactId: Int) {
        viewModelScope.launch {
            blockListRepository.unblock(contactId)
        }
    }
}
