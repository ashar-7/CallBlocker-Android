package com.example.callblocker.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.callblocker.R
import com.example.callblocker.ui.theme.CallBlockerTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun Main() {
    CallBlockerTheme {
        val systemUiController = rememberSystemUiController()
        val systemBarsColor = MaterialTheme.colors.background
        SideEffect {
            systemUiController.setSystemBarsColor(color = systemBarsColor)
        }

        RequestPermissions()

        val navController = rememberNavController()
        Navigation(navController)
    }
}

@Composable
private fun RequestPermissions() {
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
        when {
            !arePhonePermissionsGranted(context) -> {
                requestPermissionLauncher.launch(PhonePermissions)
            }
        }
    }
}

private val PhonePermissions = arrayOf(
    Manifest.permission.READ_PHONE_STATE,
    Manifest.permission.CALL_PHONE
)

private fun arePhonePermissionsGranted(context: Context): Boolean {
    PhonePermissions.forEach {
        if (ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }

    return true
}
