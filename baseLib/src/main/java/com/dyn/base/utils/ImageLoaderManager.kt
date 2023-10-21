package com.dyn.base.utils

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.widget.ImageView
import com.blankj.utilcode.util.StringUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.dyn.base.R
import kotlin.random.Random

object ImageLoaderManager {
    val holderDrawables by lazy {
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

    fun displayImage(
        imageView: ImageView,
        url: Any?,
        isGif: Boolean? = false,
        placeholder: Drawable? = null,
        requestListener:RequestListener<Drawable>? = null
    ) {
        var holder = placeholder
        if (holder == null) {
            holder = holderDrawables[Random.nextInt(holderDrawables.size)]
        }
        if (url == null || (url is String && url.isNullOrEmpty())) {
            imageView.setImageDrawable(holder)
            return
        }

        val request = Glide.with(imageView)
        if (isGif == true) {
            request.load(url).placeholder(holder).into(imageView)
            return
        }
        request.load(url).placeholder(holder).addListener(requestListener).into(imageView)
    }
    fun displayImageNoCache(
        imageView: ImageView,
        url: Any?,
        placeholder: Drawable? = null
    ) {
        var holder = placeholder
        if (holder == null) {
            holder = holderDrawables[Random.nextInt(holderDrawables.size)]
        }
        if (url == null || (url is String && url.isNullOrEmpty())) {
            imageView.setImageDrawable(holder)
            return
        }
        Glide.with(imageView).load(url).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(holder).into(imageView)
    }

}