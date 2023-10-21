package com.dyn.base.binding_adapter

import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.method.LinkMovementMethod
import android.text.method.ScrollingMovementMethod
import android.view.KeyEvent
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.doOnLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.SpanUtils.ALIGN_BASELINE
import com.blankj.utilcode.util.SpanUtils.ALIGN_CENTER
import com.dyn.base.ui.weight.ClearEditText
import com.jakewharton.rxbinding4.widget.TextViewAfterTextChangeEvent
import com.jakewharton.rxbinding4.widget.TextViewBeforeTextChangeEvent
import com.jakewharton.rxbinding4.widget.afterTextChangeEvents
import com.jakewharton.rxbinding4.widget.beforeTextChangeEvents
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
                textView.postDelayed({
                    textView.setSelection(textView.text?.length ?: 0)
                }, 200)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @BindingAdapter(value = ["textOnKeyEvent"], requireAll = false)
    @JvmStatic
    fun textOnKeyEvent(textView: EditText, handled: IKeyEventListener?) {
//        try {
//            textView.keys {
//              return@keys handled?.onKeyEvent(it)?:true
//            } .subscribe()
//        } catch (e: Exception) {
//                e.printStackTrace()
//        }
    }

    interface IKeyEventListener {
        fun onKeyEvent(event: KeyEvent): Boolean
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

    @BindingAdapter(value = ["textScrollEnable"], requireAll = false)
    @JvmStatic
    fun textScrollEnable(textView: TextView, enable: Boolean) {
        try {
            if (enable) {
                textView.movementMethod = ScrollingMovementMethod()
            }
        } catch (e: Exception) {

        }
    }

    @BindingAdapter(value = ["textMinWidth"], requireAll = false)
    @JvmStatic
    fun textMinWidth(textView: TextView, minWidth: Float) {
        textView.minWidth = minWidth.toInt()
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
    fun textWatcher(textView: EditText, listener: AfterTextChangeListener?) {
        listener?.let { ls ->
            textView.afterTextChangeEvents().skip(500, TimeUnit.MILLISECONDS).subscribe {
                ls.onAfterEvent(it)
            }
        }
    }

    @BindingAdapter(value = ["beforeTextChangeListener"], requireAll = false)
    @JvmStatic
    fun textWatcher(textView: EditText, listener: BeforeTextChangeListener) {
        textView.beforeTextChangeEvents().skip(500, TimeUnit.MILLISECONDS).subscribe {
            listener.onBeforeEvent(it)
        }
    }

    @BindingAdapter(
        value = [
            "spanStartTextStr",
            "spanEndTextStr",
            "spanStartTextColor",
            "spanEndTextColor",
            "spanStartTextSize",
            "spanEndTextSize",
            "spanStartIsBold",
            "spanEndIsBold",
        ], requireAll = false
    )
    @JvmStatic
    fun textSpan(
        textView: TextView,
        startStr: String? = null,
        endStr: String? = null,
        startColor: Int = 0,
        endColor: Int = 0,
        startSize: Int = 0,
        endSize: Int = 0,
        startIsBold: Boolean = false,
        endIsBold: Boolean = false,
    ) {
        val span = SpanUtils.with(textView)
        startStr?.let {
            span.append(it)
            if (startColor != 0) {
                span.setForegroundColor(startColor)
            }
            if (startSize != 0) {
                span.setFontSize(startSize, true)
            }
            if (startIsBold) {
                span.setBold()
            }
        }
        endStr?.let {
            span.append(it)
            if (endColor != 0) {
                span.setForegroundColor(endColor)
            }
            if (endSize != 0) {
                span.setFontSize(endSize, true)
            }
            if (endIsBold) {
                span.setBold()
            }
        }
        span.create()
    }

    /**
     * */
    @BindingAdapter(
        value = ["gradientTextStartColor", "gradientTextEndColor", "gradientTextIsVertical"],
        requireAll = false
    )
    @JvmStatic
    fun gradientTextColor(
        textView: AppCompatTextView,
        @ColorInt startColor: Int,
        @ColorInt endColor: Int,
        isV: Boolean = true,
    ) {
        val height = textView.paint.textSize
        val width = height * textView.text.length
        var x0 = 0f
        var y0 = 0f
        var x1 = 0f
        var y1 = 0f
        if (isV) {
            y1 = height
        } else {
            x1 = width
        }
        val gradient = LinearGradient(x0, y0, x1, y1, startColor, endColor, Shader.TileMode.CLAMP)
        textView.paint.shader = gradient
        textView.invalidate();
    }

    @BindingAdapter(
        value = [
            "spanImgStart",
            "spanImgEnd",
            "spanImgTextStr",
            "spanImgTextAlign",
        ], requireAll = false
    )
    @JvmStatic
    fun textSpanImg(
        textView: TextView,
        startImgRes: Drawable? = null,
        endImgRes: Drawable? = null,
        textStr: String? = null,
        spanImgTextAlign: Int = ALIGN_BASELINE,

        ) {
        val span = SpanUtils.with(textView)
        if (startImgRes != null) {
            span.appendImage(startImgRes, spanImgTextAlign)
        }
        span.append(textStr ?: "")
        if (endImgRes != null) {
            span.appendImage(endImgRes, spanImgTextAlign)
        }
        span.create()
    }

    interface AfterTextChangeListener {
        fun onAfterEvent(event: TextViewAfterTextChangeEvent)
    }

    interface BeforeTextChangeListener {
        fun onBeforeEvent(event: TextViewBeforeTextChangeEvent)
    }


    @JvmStatic
    @BindingAdapter("textEditable")
    fun setTextEditable(
        sBt: EditText,
        newValue: Editable?
    ) {
        sBt.text = newValue
    }

    @JvmStatic
    @BindingAdapter("showTextEditable")
    fun showTextEditable(
        textView: TextView,
        newValue: Editable?
    ) {
        if (textView != null && textView.movementMethod == null) {
            textView.movementMethod = LinkMovementMethod.getInstance()
        }
        if (textView.text != newValue)
            textView.text = newValue
    }

    @JvmStatic
    @InverseBindingAdapter(
        attribute = "textEditable",
        event = "textEditableChanged"
    )
    fun textEditable(editText: EditText): Editable = editText.text


    @JvmStatic
    @BindingAdapter(
        "textEditableChanged",
        requireAll = false
    )
    fun textEditableChanged(
        editText: EditText,
        textListener: InverseBindingListener?,
    ) {
        editText.doOnLayout {
            textListener?.onChange()
        }
    }

}