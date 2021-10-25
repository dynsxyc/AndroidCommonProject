package com.dyn.base.ui.weight.passedit

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.InputFilter
import android.util.AttributeSet
import android.util.TypedValue
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.blankj.utilcode.util.ConvertUtils.sp2px
import com.dyn.base.R
import com.jakewharton.rxbinding4.widget.afterTextChangeEvents
import com.jakewharton.rxbinding4.widget.beforeTextChangeEvents
import com.orhanobut.logger.Logger
import java.util.concurrent.TimeUnit

open class PasswordEditText(context: Context, attributeSet: AttributeSet?) :
    AppCompatEditText(context, attributeSet) {
    constructor(context: Context) : this(context, null)

    // 画笔
    private var mPaint = Paint()

    // 一个密码所占的宽度
    private var mPasswordItemWidth = 0f

    // 密码的个数默认为6位数
    private var mPasswordNumber = 6

    // 背景边框颜色
    private var mBgColor: Int = android.graphics.Color.parseColor("#d1d2d6")

    // 背景边框大小
    private var mBgSize = 1f

    // 背景边框圆角大小
    private var mBgCorner = 0f

    // 分割线的颜色
    private var mDivisionLineColor = mBgColor

    // 分割线的大小
    private var mDivisionLineSize = 1f

    // 密码圆点的颜色
    private var mPasswordColor = mDivisionLineColor

    // 密码圆点的半径大小
    private var mPasswordRadius = 4f

    /**
     * 是否隐藏显示远点  默认隐藏
     * */
    private var isHide = true

    init {
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        background = null
        initAttrs(context, attributeSet)
        isCursorVisible = false
//        isCursorVisible = false
//        isEnabled = false
    }

    private fun initAttrs(context: Context, attributeSet: AttributeSet?) {

        var typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.PasswordEditText)
        // 获取大小
        mPasswordNumber =
            typedArray.getInt(R.styleable.PasswordEditText_passwordNumber, mPasswordNumber)
        mDivisionLineSize = typedArray.getDimension(
            R.styleable.PasswordEditText_divisionLineSize,
            dip2px(mDivisionLineSize)
        )
        mPasswordRadius = typedArray.getDimension(
            R.styleable.PasswordEditText_passwordRadius,
            dip2px(mPasswordRadius)
        )
        mBgSize = typedArray.getDimension(R.styleable.PasswordEditText_bgSize, dip2px(mBgSize))
        mBgCorner = typedArray.getDimension(R.styleable.PasswordEditText_bgCorner, 0f)
        // 获取颜色
        mBgColor = typedArray.getColor(R.styleable.PasswordEditText_bgColor, mBgColor)
        mDivisionLineColor =
            typedArray.getColor(R.styleable.PasswordEditText_divisionLineColor, mDivisionLineColor)
        mPasswordColor =
            typedArray.getColor(R.styleable.PasswordEditText_passwordColor, mDivisionLineColor)
        isHide = typedArray.getBoolean(R.styleable.PasswordEditText_isHide, true)
        filters = arrayOf(InputFilter.LengthFilter(mPasswordNumber))
        setSingleLine()
        val inputTypeIsDefault =
            typedArray.getBoolean(R.styleable.PasswordEditText_inputTypeIsDefault, true)
        if (inputTypeIsDefault) {
            inputType = EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD or EditorInfo.TYPE_CLASS_NUMBER
        }
        afterTextChangeEvents().subscribe { event ->
            mListener?.let {
                if (event.editable?.length == mPasswordNumber) {
                    it.passwordFull(event.editable.toString())
                }
            }
        }
        typedArray.recycle()
    }

    private fun dip2px(dip: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.displayMetrics)
    }

    override fun onDraw(canvas: Canvas?) {
        drawBg(canvas)
        drawDivider(canvas)
        drawPassword(canvas)
    }

    private fun drawPassword(canvas: Canvas?) {
        val content = text.toString().trim()
        val cy = measuredHeight / 2f
        mPaint.style = Paint.Style.FILL
        mPaint.color = mPasswordColor
        for (i in content.indices) {
            var cx =
                mBgSize + mPasswordItemWidth * i + mPasswordItemWidth / 2 + (i - 1) * mDivisionLineSize
            if (isHide) {
                canvas?.drawCircle(cx, cy, mPasswordRadius, mPaint)
            } else {
                mPaint.textSize = sp2px(28f).toFloat()
                val itemStr = content[i].toString()
                val f = mPaint.fontMetrics
                val x = cx - mPaint.measureText(itemStr) / 2
                val dy = (f.bottom - f.top) / 2 - f.bottom
                val baseLine = cy + dy
                canvas?.drawText(content[i].toString(), x, baseLine, mPaint)
            }
        }
    }

    private fun drawDivider(canvas: Canvas?) {
        mPaint.style = Paint.Style.STROKE
        mPaint.color = mDivisionLineColor
        mPaint.strokeWidth = mDivisionLineSize
        var startX: Float
        var startY = mBgSize
        var endX: Float
        var endY = measuredHeight - mBgSize
        for (i in 0 until mPasswordNumber - 1) {
            startX = mBgSize + mPasswordItemWidth * (i + 1) + mDivisionLineSize * i
            endX = startX
            canvas?.drawLine(startX, startY, endX, endY, mPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mPasswordItemWidth =
            (measuredWidth - mBgSize * 2 - (mPasswordNumber - 1) * mDivisionLineSize) / mPasswordNumber
    }

    /**
     * 画背景边框
     * */
    private fun drawBg(canvas: Canvas?) {
        val rectF = RectF(mBgSize, mBgSize, measuredWidth - mBgSize, measuredHeight - mBgSize)
        mPaint.style = Paint.Style.STROKE
        mPaint.color = mBgColor
        mPaint.strokeWidth = mBgSize
        if (mBgCorner == 0f) {
            canvas?.drawRect(rectF, mPaint)
        } else {
            canvas?.drawRoundRect(rectF, mBgCorner, mBgCorner, mPaint)
        }
    }

    fun addText(number: String) {
        var password = text.toString().trim()
        if (password.length >= mPasswordNumber) {
            return
        }
        password += number
        setText(password)
    }

    fun deleteLastPassword() {
        var text = text.toString().trim()
        if (text.isEmpty()) {
            return
        }
        text = text.substring(0, text.length - 1)
        setText(text)
    }

    var mListener: PasswordFullListener? = null

    /**
     * 密码已经全部填满
     */
    interface PasswordFullListener {
        fun passwordFull(password: String)
    }

}