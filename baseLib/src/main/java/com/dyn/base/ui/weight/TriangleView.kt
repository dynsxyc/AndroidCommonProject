package com.dyn.base.ui.weight

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.graphics.PointF
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
    private var isArrowDown = false //是否是向下的三角
    private var isRight = false //是否直角
    private var rightPosition = 0 //直角位置 0 左上 1 右上 2 左下 3 右下
    var triangleColor = Color.WHITE
        set(value) {
            field = value
            mPaint.color = value
            invalidate()
        }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.TriangleView)
        triangleColor = a.getColor(R.styleable.TriangleView_triangleColor, DEFAULT_COLOR)
        mWidth = a.getDimensionPixelSize(R.styleable.TriangleView_triangleWidth, 0)
        isArrowDown = a.getBoolean(R.styleable.TriangleView_triangleIsArrowDown, false)
        isRight = a.getBoolean(R.styleable.TriangleView_triangleIsRight, false)
        rightPosition = a.getInt(R.styleable.TriangleView_triangleRightPosition, 0)
        a.recycle()
        mPaint.color = triangleColor
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
            var topPoint = PointF(width / 2f, 0f)//顶点
            val startPoint = PointF((width - mWidth) / 2f, height.toFloat())//左
            val endPoint = PointF((width + mWidth) / 2f, height.toFloat())//右
            if (isRight){
                when(rightPosition){
                    0->{
                        topPoint.x = 0f
                        endPoint.y = 0f
                    }
                    1->{
                        topPoint.x = 0f
                        startPoint.y = 0f
                        startPoint.x = (width + mWidth) / 2f
                    }
                    2->{
                        topPoint.x = 0f
                    }
                    3->{
                        topPoint.x = (width + mWidth) / 2f

                    }
                }
            }
            mPath.moveTo(topPoint.x, topPoint.y)//顶点
            mPath.lineTo(startPoint.x, startPoint.y)//左
            mPath.lineTo(endPoint.x, endPoint.y)//右
        }
        mPath.close()
        canvas?.drawPath(mPath, mPaint)
    }


}