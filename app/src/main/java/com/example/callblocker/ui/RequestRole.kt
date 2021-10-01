package com.example.callblocker.ui

import android.app.Activity
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi
import com.example.callblocker.util.CallScreeningServiceRequiredApi

/**
 * Requests a role from the user
 * The input is the name of the role to request
 * The result is true if the role is granted, otherwise false
 */
@RequiresApi(CallScreeningServiceRequiredApi)
class RequestRole : ActivityResultContract<String, Boolean>() {

    override fun createIntent(context: Context, roleName: String): Intent {
        val roleManager = context.getSystemService(ComponentActivity.ROLE_SERVICE) as RoleManager
        return roleManager.createRequestRoleIntent(roleName)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return when (resultCode) {
            Activity.RESULT_OK -> true
            else -> false
        }
    }
}
