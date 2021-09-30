package com.example.callblocker.ui.addtoblocklist

import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract

data class PickContactResult(val name: String, val phoneNumber: String)

class PickContact(
    private val contentResolver: ContentResolver
) : ActivityResultContract<Unit, PickContactResult>() {

    override fun createIntent(context: Context, input: Unit?): Intent {
        return Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
    }

    override fun parseResult(resultCode: Int, result: Intent?): PickContactResult? {
        if (resultCode == RESULT_OK && result != null) {
            try {
                val uri = result.data!!
                val cursor = contentResolver.query(uri, null, null, null, null)!!
                if (cursor.moveToFirst()) {
                    val nameIndex =
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    val phoneIndex =
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                    val name = cursor.getString(nameIndex)
                    val phoneNumber = cursor.getString(phoneIndex)

                    return PickContactResult(name, phoneNumber)
                }
            } catch (e: Exception) {
                Log.e("PickContact", e.message.toString())
            }
        }

        return null
    }
}
