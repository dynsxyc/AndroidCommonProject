package com.dyn.base.binding_adapter

import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.dyn.base.ui.weight.ClearEditText
import com.jakewharton.rxbinding4.widget.TextViewAfterTextChangeEvent
import com.jakewharton.rxbinding4.widget.afterTextChangeEvents
import java.util.concurrent.TimeUnit

object BindingTextAdapter {
    @BindingAdapter(value = ["textGravity"], requireAll = false)
    @JvmStatic
    fun textGravity(textView: TextView, gravity: Int) {
        textView.gravity = gravity
    }

    @BindingAdapter(value = ["textSelectionIndex"], requireAll = false)
    @JvmStatic
    fun textSelectionIndex(textView: EditText, index: Int) {
        try {
            textView.setSelection(index)
        } catch (e: Exception) {

        }
    }

    @BindingAdapter(value = ["textSelectionToEnd"], requireAll = false)
    @JvmStatic
    fun textSelectionToEnd(textView: EditText, isSelectionEnd: Boolean) {
        try {
            if (isSelectionEnd) {
                textView.setSelection(textView.text?.length ?: 0)
            }
        } catch (e: Exception) {

        }
    }

    @BindingAdapter(value = ["textEnable"], requireAll = false)
    @JvmStatic
    fun textEnable(textView: TextView, enable: Boolean) {
        try {
            if (enable != textView.isEnabled) {
                textView.isEnabled = enable
            }
        } catch (e: Exception) {

        }
    }

    @BindingAdapter(value = ["textMinWidth"], requireAll = false)
    @JvmStatic
    fun textMinWidth(textView: TextView, minWidth: Float) {
        textView.minWidth = minWidth.toInt()
    }

    @BindingAdapter(value = ["android:textSize"], requireAll = false)
    @JvmStatic
    fun textSize(textView: TextView, textSize: Float) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    @BindingAdapter(value = ["iconClear"], requireAll = false)
    @JvmStatic
    fun iconClear(textView: ClearEditText, drawable: Drawable?) {
        drawable?.let {
            textView.drawableClear = drawable
        }
    }

    @BindingAdapter(value = ["tvFocusable"], requireAll = false)
    @JvmStatic
    fun tvFocusable(textView: ClearEditText, requestFocusable: Boolean) {
        if (requestFocusable) {
            textView.requestFocus()
        } else {
            textView.clearFocus()
        }
    }


    @BindingAdapter(value = ["afterTextChangeListener"], requireAll = false)
    @JvmStatic
    fun textWatcher(textView: EditText, listener: AfterTextChangeListener) {
        textView.afterTextChangeEvents().skip(500, TimeUnit.MILLISECONDS).subscribe {
            listener.onEvent(it)
        }
    }

    interface AfterTextChangeListener {
        fun onEvent(event: TextViewAfterTextChangeEvent)
    }


}