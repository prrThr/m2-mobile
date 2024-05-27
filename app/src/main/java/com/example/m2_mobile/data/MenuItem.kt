package com.example.m2_mobile.data

import android.graphics.Bitmap

data class MenuItem(
    val name: String,
    var price: Double?,
    var image: Bitmap? = null
)
