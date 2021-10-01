package com.example.callblocker.ui

import android.Manifest
import android.app.role.RoleManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.callblocker.R
import com.example.callblocker.ui.theme.CallBlockerTheme
import com.example.callblocker.util.CallScreeningServiceRequiredApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun Main() {
    CallBlockerTheme {
        val systemUiController = rememberSystemUiController()
        val systemBarsColor = MaterialTheme.colors.background
        SideEffect {
            systemUiController.setSystemBarsColor(color = systemBarsColor)
        }

        // Not showing rationale dialog for simplicity
        if (Build.VERSION.SDK_INT < CallScreeningServiceRequiredApi) {
            RequestPhonePermissions()
        } else {
            RequestCallScreeningRole()
        }

        val navController = rememberNavController()
        Navigation(navController)
    }
}

@RequiresApi(CallScreeningServiceRequiredApi)
private const val CallScreeningRole = RoleManager.ROLE_CALL_SCREENING

private val PhonePermissions = arrayOf(
    Manifest.permission.READ_PHONE_STATE,
    Manifest.permission.CALL_PHONE
)

@Composable
@RequiresApi(CallScreeningServiceRequiredApi)
private fun RequestCallScreeningRole() {
    val context = LocalContext.current
    val roleManager = context.getSystemService(ComponentActivity.ROLE_SERVICE) as RoleManager
    val requestRoleLauncher = rememberLauncherForActivityResult(
        RequestRole()
    ) { granted ->
        if (!granted) {
            Toast.makeText(
                context,
                context.getString(R.string.block_call_role_not_granted),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LaunchedEffect(null) {
        if (!roleManager.isRoleHeld(CallScreeningRole)) {
            requestRoleLauncher.launch(CallScreeningRole)
        }
    }
}

@Composable
private fun RequestPhonePermissions() {
    val context = LocalContext.current
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionGrantedMap ->
        PhonePermissions.forEach {
            if (permissionGrantedMap[it] == false) {
                Toast.makeText(
                    context,
                    context.getString(R.string.block_call_permissions_not_granted),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    LaunchedEffect(null) {
        if (!arePhonePermissionsGranted(context)) {
            requestPermissionLauncher.launch(PhonePermissions)
        }
    }
}

private fun arePhonePermissionsGranted(context: Context): Boolean {
    PhonePermissions.forEach {
        if (ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }

    return true
}
