package com.example.m2_mobile.data
import android.graphics.Bitmap

data class MenuItem(
    val name: String,
    val price: Double?,
    var image: Bitmap? = null
)