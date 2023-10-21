package com.dyn.base.ui.weight

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import com.blankj.utilcode.util.ColorUtils
import com.dyn.base.R
import com.flyco.roundview.RoundTextView
import com.orhanobut.logger.Logger

class MessageCountTextView(context: Context, attributeSet: AttributeSet?, defaultStyle: Int) : RoundTextView(context, attributeSet, defaultStyle) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    private var mDelegate = delegate
    private var isMessageStatus: Boolean = false
    private var maxCount: Int = 99
    private var messageCount: Int = 0
    private var mCountMinSize = dp2px(16f)
    private var mStatusMinSize = dp2px(5f)

    init {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.MessageCountTextView)
        isMessageStatus = typedArray.getBoolean(R.styleable.MessageCountTextView_isMessageStatus, isMessageStatus)
        maxCount = typedArray.getInt(R.styleable.MessageCountTextView_maxCount, maxCount)
        messageCount = typedArray.getInt(R.styleable.MessageCountTextView_messageCount, messageCount)
        mCountMinSize = typedArray.getDimensionPixelSize(R.styleable.MessageCountTextView_mCountMinSize, mCountMinSize.toInt()).toFloat()
        mStatusMinSize = typedArray.getDimensionPixelSize(R.styleable.MessageCountTextView_mStatusMinSize, mStatusMinSize.toInt()).toFloat()
//        val bgColor = typedArray.getColor(R.styleable.MessageCountTextView_bgColor,Color.RED)
        val bgColor = typedArray.getColor(R.styleable.MessageCountTextView_bgColor,resources.getColor(R.color.message_bg_color ))
//        val bgColor = typedArray.getColor(R.styleable.MessageCountTextView_bgColor, ColorUtils.getColor(R.color.message_bg_color))
        val tColor = typedArray.getColor(R.styleable.MessageCountTextView_textColor, Color.WHITE)
        setTextColor(tColor)
        mDelegate.backgroundColor = bgColor
        gravity = Gravity.CENTER
        setStatus(isMessageStatus)
        typedArray.recycle()
        setMessageCount(messageCount)
    }

    fun setStatus(status: Boolean) {
        isMessageStatus = status
        if (isMessageStatus) {
            showMessageStatus()
        } else {
            showMessageCount()
        }
    }

    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }

    private fun showMessageStatus() {
        textSize = 0f
        val w = mStatusMinSize.toInt()
        minWidth = w
        minHeight = w
        text = ""
        mDelegate.cornerRadius = w / 2
    }

    private fun showMessageCount() {
        val w = mCountMinSize.toInt()
        textSize = 10f
        minWidth = w
        minHeight = w
        mDelegate.cornerRadius = w / 2
    }

    fun setMessageCount(count: Int) {
        messageCount = count
        if (isMessageStatus.not()) {
            text = if (count > maxCount) {
                "$maxCount+"
            } else {
                "$count"
            }
        }
        visibility = if (count <= 0) View.GONE else View.VISIBLE
    }

    fun setMaxCount(maxCount: Int) {
        this.maxCount = maxCount
        setMessageCount(messageCount)
    }
}