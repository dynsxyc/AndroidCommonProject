package com.bzl.behavior

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MoveView(
    context: Context,
    @Nullable attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : View(context, attrs, defStyleAttr, defStyleRes) {

    constructor(
        context: Context
    ) : this(context, null)
    constructor(
        context: Context,
        @Nullable attrs: AttributeSet?
    ) : this(context, attrs,  0)
    constructor(
        context: Context,
        @Nullable attrs: AttributeSet?,
        defStyleAttr: Int
    ) : this(context, attrs, defStyleAttr, 0)
    var lastX  = 0f
    var lastY  = 0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN ->{
                lastX = event.x
                lastY = event.y
            }
            MotionEvent.ACTION_MOVE ->{
                val offsetX = event.x - lastX
                val offsetY = event.y - lastY
                ViewCompat.offsetLeftAndRight(this, offsetX.toInt())
                ViewCompat.offsetTopAndBottom(this, offsetY.toInt())
            }
        }
        return true
    }
}