package com.example.callblocker.util

/**
 * Not sure what to name this function. It strips out non-digits (except '+') from the number string
 * Example: +1 (650) 555-6789 -> +16505556789
 */
fun transformNumber(number: String) = number.replace(Regex("((?<!^)[^0-9]|^[^0-9+])"), "")
