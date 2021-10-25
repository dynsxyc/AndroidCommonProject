package com.dyn.base.utils

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.widget.ImageView
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.StringUtils
import com.bumptech.glide.Glide
import com.dyn.base.R
import kotlin.random.Random

object ImageLoaderManager {
    private val holderDrawables by lazy {
        val list = mutableListOf<Drawable>()
        val colorArrays = StringUtils.getStringArray(R.array.default_img_colors)
        colorArrays.forEach {
            val drawable = GradientDrawable()
            drawable.shape = GradientDrawable.RECTANGLE
            drawable.setColor(Color.parseColor(it))
            list.add(drawable)
        }
        list
    }

    fun displayImage(imageView: ImageView, url: Any?, placeholder: Drawable? = null) {
        var holder = placeholder
        if (holder == null) {
            holder = holderDrawables[Random.nextInt(holderDrawables.size)]
        }
        if (url == null || (url is String && url.isNullOrEmpty())) {
            imageView.setImageDrawable(holder)
            return
        }

        Glide.with(imageView).load(url).placeholder(holder).into(imageView)
    }

}