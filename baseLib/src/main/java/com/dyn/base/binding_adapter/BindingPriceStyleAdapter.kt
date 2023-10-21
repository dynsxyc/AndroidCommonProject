package com.dyn.base.binding_adapter

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.SpanUtils.ALIGN_CENTER
import com.dyn.base.R
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*
import kotlin.Exception

object BindingPriceStyleAdapter {

    @BindingAdapter(
        value = ["priceStyleText", "priceStyleLabelSize", "priceStyleNumberSize", "priceStyleFractionSize", "priceIsBold"],
        requireAll = false
    )
    @JvmStatic
    fun style(
        textView: TextView,
        originalStr: String?,
        labelSize: Int,
        numberSize: Int,
        fractionSize: Int,
        isBold: Boolean = false,
    ) {
        var minPrice: String = format("0.00")
        var maxPrice: String? = null
        if (originalStr.isNullOrEmpty()) {
            minPrice = format("0.00")
        } else if (originalStr.indexOf("-") != -1) {
            val split = originalStr.split("-")
            if (split.size == 2) {
                minPrice = format(split[0])
                maxPrice = format(split[1])
            }
        } else {
            minPrice = format(originalStr)
        }
        val typeface = ResourcesCompat.getFont(textView.context, R.font.medium) ?: Typeface.DEFAULT
        val result = SpanUtils.with(textView).append("¥").setFontSize(labelSize, true)
            .setTypeface(if (isBold) typeface else Typeface.DEFAULT)
            .append(minPrice.substring(0 until minPrice.length - 3)).setFontSize(numberSize, true)
            .setTypeface(if (isBold) typeface else Typeface.DEFAULT)
            .append(minPrice.substring(minPrice.length - 3)).setFontSize(fractionSize, true)
            .setTypeface(if (isBold) typeface else Typeface.DEFAULT)
        if (maxPrice.isNullOrBlank().not()) {
            result.append("-").setFontSize(labelSize, true)
                .setTypeface(if (isBold) typeface else Typeface.DEFAULT)
                .append(maxPrice!!.substring(0 until maxPrice.length - 3))
                .setFontSize(numberSize, true)
                .setTypeface(if (isBold) typeface else Typeface.DEFAULT)
                .append(maxPrice.substring(maxPrice.length - 3)).setFontSize(fractionSize, true)
                .setTypeface(if (isBold) typeface else Typeface.DEFAULT)
        }
        textView.text = result.create()
    }

    @JvmStatic
    fun format(number: String): String {
        val vaStr = if (number.indexOf(",") != -1) {
            number.replace(",", "")
        } else {
            number
        }
        val numberFormat = NumberFormat.getInstance(Locale.CHINA)
        numberFormat.roundingMode = RoundingMode.HALF_UP
        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 2
        return try {
            numberFormat.format(vaStr.toDouble())
        } catch (e: Exception) {
            numberFormat.format(0.00)
        }
    }

    /**
     * 小羊角 大价格显示
     * */
    @BindingAdapter(value = ["moneyLabelStr","moneyLabelSize", "moneyLabelColor", "moneyContentStr","moneyIsBold", "moneyContentSize", "moneyContentColor", "isMinPrice"],requireAll = false)
    @JvmStatic
    fun commonPriceStyle(
        textView: TextView,
        moneyLabelStr: String? = "¥",
        labelSize: Int,
        moneyLabelColor: Int = -1,
        contentStr: String? = "",
        moneyIsBold: Boolean = false,
        contentSize: Int,
        contentColor: Int = -1,
        isMinPrice: Boolean? = false,
    ) {
        val utils = SpanUtils.with(textView)
            .append(moneyLabelStr?:"¥").setFontSize(labelSize, true).setForegroundColor(moneyLabelColor)
            .append(contentStr?:"").setFontSize(contentSize, true).setForegroundColor(contentColor)
        if (moneyIsBold){
            utils.setBold()
        }
        if (isMinPrice == true) {
            utils.append("起").setFontSize(labelSize, true).setForegroundColor(moneyLabelColor)
        }
        utils.create()
    }

    /**
     * 文字后面附加小图片
     * */
    @BindingAdapter(value = ["textLastLabelStr", "textLastLabelDrawable"])
    @JvmStatic
    fun textLastLabel(
        textView: TextView,
        content: String?,
        drawableRes: Drawable?
    ) {
        val sp = SpanUtils.with(textView)
        content?.let {
            sp.append(it)
        }
        drawableRes?.let {
            sp.appendImage(drawableRes, ALIGN_CENTER)
        }
        sp.create()
    }
    /**
     * 文字后面附加小图片
     * */
    @BindingAdapter(value = ["textLastMutableLabelStr", "textLastMutableLabelRes"])
    @JvmStatic
    fun textLastLabel(
        textView: TextView,
        content: String?,
        drawableRes: MutableList<Int>?
    ) {
        val sp = SpanUtils.with(textView)
        content?.let {
            sp.append(it)
        }
        drawableRes?.forEach {
            try {
                sp.appendImage(ResourceUtils.getDrawable(it), ALIGN_CENTER)
                sp.append(" ")
            }catch (e:Exception){

            }
        }
        sp.create()
    }

    /**
     * 文字后面附加小图片
     * */
    @BindingAdapter(value = ["textContentStr","textUnitStr","textUnitLittleSize"])
    @JvmStatic
    fun textUnit(
        textView: TextView,
        content: String?,
        unitStr:String?,
        unitSize:Int = 14,
    ) {
        SpanUtils.with(textView)
            .append(content?:"")
            .append(unitStr?:"").setFontSize(unitSize,true)
            .create()
    }

    /**
     * 中间文字颜色不同
     * */
    @BindingAdapter(value = ["middleColorTextStartStr","middleColorTextMiddleStr","middleColorTextEndStr","middleColorTextColor"],)
    @JvmStatic
    fun textMiddleColor(
        textView: TextView,
        startStr: String?,
        middleStr: String?,
        endStr: String?,
        @ColorInt color:Int,
    ) {
        SpanUtils.with(textView)
            .append(startStr?:"")
            .append(middleStr?:"").setForegroundColor(color)
            .append(endStr?:"")
            .create()
    }


}