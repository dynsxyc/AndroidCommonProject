package com.dyn.base.ui.weight

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.dyn.base.R

class StartTriangleView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {
    companion object {
        const val DEFAULT_COLOR = Color.BLACK
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val mPaint: Paint = Paint()
    private val mPath: Path = Path()
    private var mWidth = 0
    private var isArrowEnd = false

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.StartTriangleView)
        val color = a.getColor(R.styleable.StartTriangleView_triangleColor, DEFAULT_COLOR)
        mWidth = a.getDimensionPixelSize(R.styleable.StartTriangleView_triangleWidth, 0)
        isArrowEnd = a.getBoolean(R.styleable.StartTriangleView_triangleIsArrowEnd, false)
        a.recycle()
        mPaint.color = color
        mPaint.isDither = true
        mPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mWidth = if (mWidth == 0) width else mWidth
        mPath.reset()
        if (isArrowEnd) {
            mPath.moveTo(width.toFloat() , height.toFloat()/ 2f)
            mPath.lineTo(0f, 0f)
            mPath.lineTo(0f, height.toFloat())
        } else {
            mPath.moveTo(0f, height.toFloat()/2f)
            mPath.lineTo(width.toFloat() , 0f)
            mPath.lineTo(width.toFloat(), height.toFloat())
        }
        mPath.close()
        canvas?.drawPath(mPath, mPaint)
    }


}