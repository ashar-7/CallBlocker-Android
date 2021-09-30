package com.example.callblocker.ui.blocklist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.outlined.RemoveCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.callblocker.R
import com.example.callblocker.model.Contact

@Composable
fun BlockListScreen(viewModel: BlockListViewModel, onAddNewContact: (contactId: Int?) -> Unit) {
    val uiState by remember { viewModel.uiState }.collectAsState()

    Scaffold(
        topBar = { BlockListTopBar() }
    ) { padding ->
        val modifier = Modifier
            .padding(padding)
            .fillMaxSize()

        if (uiState.isLoading && uiState.blockList.isEmpty()) {
            LoadingScreen(modifier = modifier)
        } else {
            SuccessScreen(
                blockList = uiState.blockList,
                modifier = modifier,
                onAddNewToBlockList = onAddNewContact,
                onRemove = viewModel::unblock
            )
        }
    }
}

@Composable
private fun SuccessScreen(
    blockList: List<Contact>,
    modifier: Modifier,
    onAddNewToBlockList: (contactId: Int?) -> Unit,
    onRemove: (contactId: Int) -> Unit
) {
    LazyColumn(modifier) {
        item {
            Divider()
            AddNewButton(onClick = { onAddNewToBlockList(null) })
            Divider()
        }

        if (blockList.isNotEmpty()) {
            items(blockList) { contact ->
                BlockListItem(
                    contact,
                    onClick = { onAddNewToBlockList(contact.id) },
                    onRemove = { onRemove(contact.id ?: 0) }
                )
            }
        } else {
            item {
                EmptyScreen(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun BlockListItem(
    contact: Contact,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    var unblockDialogShown by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
        ) {
            Text(
                text = contact.name,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = contact.phoneNumber,
                    style = MaterialTheme.typography.caption,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        IconButton(onClick = { unblockDialogShown = true }) {
            Icon(
                Icons.Outlined.RemoveCircle,
                stringResource(R.string.blocklist_remove_contact_icon_desc)
            )
        }
    }

    if (unblockDialogShown) {
        Surface(elevation = 24.dp) {
            AlertDialog(
                onDismissRequest = { unblockDialogShown = false },
                title = { Text(stringResource(R.string.blocklist_unblock_dialog_title)) },
                text = { Text(stringResource(R.string.blocklist_unblock_dialog_text)) },
                confirmButton = {
                    Button(onClick = { onRemove(); unblockDialogShown = false }) {
                        Text(stringResource(R.string.blocklist_unblock_dialog_confirm_text))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { unblockDialogShown = false }) {
                        Text(stringResource(R.string.blocklist_unblock_dialog_dismiss_text))
                    }
                }
            )
        }
    }
}

@Composable
private fun AddNewButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick, role = Role.Button)
            .padding(12.dp, 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = 12.dp,
            alignment = Alignment.CenterHorizontally
        )
    ) {
        Icon(Icons.Default.AddCircle, null)
        Text(
            stringResource(R.string.blocklist_add_new_button_text),
            style = MaterialTheme.typography.button
        )
    }
}

@Composable
private fun LoadingScreen(modifier: Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(16.dp), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.blocklist_empty_screen_message),
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.Center
        )
    }
}
