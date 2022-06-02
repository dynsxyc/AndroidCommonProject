package com.bzl.behavior

import android.content.Context
import android.graphics.Color
import android.graphics.Color.blue
import androidx.annotation.IntRange
import kotlin.random.Random

object ContextExt {
    fun Context. randomColor(@IntRange(from = 0, to = 255) alpha: Int = 255): Int = let {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val random = Random
            return@let Color.argb(
                alpha,
                random.nextInt(225),
                random.nextInt(225),
                random.nextInt(225),
            )
        } else {
            return@let this.resources.getColor(android.R.color.holo_blue_bright)
        }
    }
}