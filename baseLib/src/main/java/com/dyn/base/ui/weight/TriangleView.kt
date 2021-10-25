package com.dyn.base.ui.weight

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.marginLeft
import com.dyn.base.R
import com.orhanobut.logger.Logger

class TriangleView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {
    companion object {
        const val DEFAULT_COLOR = Color.BLACK
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val mPaint: Paint = Paint()
    private val mPath: Path = Path()
    private var mWidth = 0
    private var isArrowDown = false

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.TriangleView)
        val color = a.getColor(R.styleable.TriangleView_triangleColor, DEFAULT_COLOR)
        mWidth = a.getDimensionPixelSize(R.styleable.TriangleView_triangleWidth, 0)
        isArrowDown = a.getBoolean(R.styleable.TriangleView_triangleIsArrowDown, false)
        a.recycle()
        mPaint.color = color
        mPaint.isDither = true
        mPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mWidth = if (mWidth == 0) width else mWidth
        mPath.reset()
        if (isArrowDown) {
            mPath.moveTo(width / 2f, height.toFloat())
            mPath.lineTo((width - mWidth) / 2f, 0f)
            mPath.lineTo((width + mWidth) / 2f, 0f)
        } else {
            mPath.moveTo(width / 2f, 0f)
            mPath.lineTo((width - mWidth) / 2f, height.toFloat())
            mPath.lineTo((width + mWidth) / 2f, height.toFloat())
        }
        mPath.close()
        canvas?.drawPath(mPath, mPaint)
    }


}