package com.example.callblocker.ui.blocklist

import com.example.callblocker.model.Contact

data class BlockListUiState(
    val blockList: List<Contact>,
    val isLoading: Boolean
)
