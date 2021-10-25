package com.dyn.base.ui.weight

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import com.luck.picture.lib.animators.ViewHelper

class TouchMoveView : AppCompatImageView {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context) : this(context, null)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var mLastX = 0F
    private var mLastY = 0F
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return false
        }
        val x = event.rawX
        val y = event.rawY
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - mLastX
                val deltaY = y - mLastY
                translationX += deltaX
                translationY += deltaY
            }
        }
        mLastX = x
        mLastY = y

        return super.onTouchEvent(event)
    }

}