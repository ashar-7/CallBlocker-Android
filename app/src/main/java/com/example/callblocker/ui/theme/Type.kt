package com.example.callblocker.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.callblocker.R

private val PoppinsFontFamily = FontFamily(
    listOf(
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_medium, FontWeight.Medium),
    )
)

val Typography = Typography(defaultFontFamily = PoppinsFontFamily)
