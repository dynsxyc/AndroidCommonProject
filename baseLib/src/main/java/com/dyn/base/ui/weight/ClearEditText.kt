package com.dyn.base.ui.weight

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatEditText
import com.blankj.utilcode.util.ConvertUtils
import com.dyn.base.R
import com.orhanobut.logger.Logger

open class ClearEditText(context: Context, attributes: AttributeSet?) :
    AppCompatEditText(context, attributes) {
    companion object {
        /** 默认的清除按钮图标资源  */
        private val ICON_CLEAR_DEFAULT = R.drawable.ic_edit_clear
    }

    var drawableClear: Drawable
    var drawableLook: Drawable //密码可见
    var drawableNoLook: Drawable //密码不可见
    var isShowHidePassMode = false
        set(value) {
            field = value
            updateIconClear()
        }


    init {
        // 获取自定义属性
        val typedArray = context.obtainStyledAttributes(attributes, R.styleable.ClearEditText)
        // 获取清除按钮图标资源
        val iconClear =
            typedArray.getResourceId(R.styleable.ClearEditText_iconClear, ICON_CLEAR_DEFAULT)
        val iconLook =
            typedArray.getResourceId(R.styleable.ClearEditText_iconLook, R.drawable.ic_zhenyan)
        val iconNoLook =
            typedArray.getResourceId(R.styleable.ClearEditText_iconNoLook, R.drawable.ic_biyan)
        val isShowHidePassMode =
            typedArray.getBoolean(R.styleable.ClearEditText_isShowHidePassMode, false)
        drawableClear = resources.getDrawable(iconClear)
        drawableLook = resources.getDrawable(iconLook)
        drawableNoLook = resources.getDrawable(iconNoLook)
        this.isShowHidePassMode = isShowHidePassMode
        if (isShowHidePassMode){
            var drawables = compoundDrawables
                    setCompoundDrawablesWithIntrinsicBounds(
                        drawables[0], drawables[1], drawableNoLook,
                        drawables[3]
                    )
        }else{
            updateIconClear()
        }

        typedArray.recycle()
        compoundDrawablePadding = ConvertUtils.dp2px(5f)
        // 设置TextWatcher用于更新清除按钮显示状态
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                updateIconClear()
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.i("editTouch",event.toString())
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 点击是的 x 坐标
                val xDown = event.getX().toInt()
                // 清除按钮的起始区间大致为[getWidth() - getCompoundPaddingRight(), getWidth()]，
                // 点击的x坐标在此区间内则可判断为点击了清除按钮
                if (xDown >= width - compoundPaddingRight && xDown < width) {
                    if (isShowHidePassMode.not()) {
                        // 清空文本
                        setText("")
                    }else{
                        var drawables = compoundDrawables
                        when (transformationMethod) {
                            is HideReturnsTransformationMethod -> {
                                transformationMethod = PasswordTransformationMethod.getInstance()
                                setCompoundDrawablesWithIntrinsicBounds(
                                    drawables[0], drawables[1], drawableNoLook,
                                    drawables[3]
                                )
                            }
                            is PasswordTransformationMethod -> {
                                transformationMethod = HideReturnsTransformationMethod.getInstance()
                                setCompoundDrawablesWithIntrinsicBounds(
                                    drawables[0], drawables[1], drawableLook,
                                    drawables[3]
                                )
                            }
                        }
                    }
                }
                updateIconClear()
                requestFocus()
                val index = text?.length?.let {
                    it
                }?:0
                setSelection(index)
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        updateIconClear()
    }

    fun updateIconClear() {
        var drawables = compoundDrawables
        if (isShowHidePassMode.not()) {
            if (length() > 0 && hasFocus()) {
                setCompoundDrawablesWithIntrinsicBounds(
                    drawables[0], drawables[1], drawableClear,
                    drawables[3]
                )
            } else {
                setCompoundDrawablesWithIntrinsicBounds(
                    drawables[0], drawables[1], null,
                    drawables[3]
                )
            }
        }else{
            when(transformationMethod){
                is PasswordTransformationMethod ->{
                    setCompoundDrawablesWithIntrinsicBounds(
                        drawables[0], drawables[1], drawableNoLook,
                        drawables[3]
                    )
                }
                is HideReturnsTransformationMethod ->{
                    setCompoundDrawablesWithIntrinsicBounds(
                        drawables[0], drawables[1], drawableLook,
                        drawables[3]
                    )
                }
            }
        }
    }




    fun setIconClear(@DrawableRes resId: Int) {
        drawableClear = resources.getDrawable(resId)
        updateIconClear()
    }
}