package com.example.callblocker.ui.addtoblocklist

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.launch
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.callblocker.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddToBlockListScreen(
    viewModel: AddToBlockListViewModel,
    onDismiss: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val contact by viewModel.contact

    val pickContact = rememberLauncherForActivityResult(
        PickContact(LocalContext.current.contentResolver)
    ) { contactResult ->
        viewModel.setName(contactResult.name)
        viewModel.setPhoneNumber(contactResult.phoneNumber)
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        val textFieldModifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)

        NameTextField(
            name = contact.name,
            onNameChange = viewModel::setName,
            modifier = textFieldModifier
        )
        PhoneTextField(
            phoneNumber = contact.phoneNumber,
            onPhoneNumberChange = viewModel::setPhoneNumber,
            modifier = textFieldModifier
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ImportContactButton(onClick = { pickContact.launch() })

            SaveButton(
                onClick = {
                    if(contact.name.isNotBlank() && contact.phoneNumber.isNotBlank()) {
                        viewModel.saveContact()
                        onDismiss()
                        keyboardController?.hide()
                    }
                }
            )
        }
    }
}

@Composable
private fun NameTextField(
    name: String,
    modifier: Modifier = Modifier,
    onNameChange: (String) -> Unit
) {
    BaseTextField(
        value = name,
        onValueChange = onNameChange,
        hint = stringResource(R.string.add_to_blocklist_name_field_hint),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            capitalization = KeyboardCapitalization.Words
        ),
        modifier = modifier
    )
}

@Composable
private fun PhoneTextField(
    phoneNumber: String,
    modifier: Modifier = Modifier,
    onPhoneNumberChange: (String) -> Unit
) {
    BaseTextField(
        value = phoneNumber,
        onValueChange = onPhoneNumberChange,
        hint = stringResource(R.string.add_to_blocklist_phone_field_hint),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        modifier = modifier
    )
}

@Composable
private fun BaseTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        maxLines = 1,
        textStyle = MaterialTheme.typography.body2.copy(color = LocalContentColor.current),
        cursorBrush = SolidColor(LocalContentColor.current),
        decorationBox = { innerTextField ->
            Box(contentAlignment = Alignment.CenterStart) {
                innerTextField()
                if (value.isEmpty()) {
                    TextFieldHint(hint)
                }
            }
        }
    )
}

@Composable
private fun TextFieldHint(text: String, style: TextStyle = MaterialTheme.typography.body2) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Text(text, style = style)
    }
}

@Composable
private fun ImportContactButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    TextButton(modifier = modifier, onClick = onClick) {
        Icon(
            Icons.Default.Contacts,
            contentDescription = stringResource(R.string.add_to_blocklist_import_contact_icon_desc)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(stringResource(R.string.add_to_blocklist_import_contact), fontSize = 12.sp)
    }
}

@Composable
private fun SaveButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    TextButton(modifier = modifier, onClick = onClick) {
        Text(text = stringResource(R.string.add_to_blocklist_save_button_text))
    }
}
