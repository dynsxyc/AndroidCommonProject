package com.dyn.base.binding_adapter

import android.graphics.Typeface
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.SpanUtils
import com.dyn.base.R
import java.lang.Exception
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*

object BindingPriceStyleAdapter {

    @BindingAdapter(
        value = ["priceStyleText", "priceStyleLabelSize", "priceStyleNumberSize", "priceStyleFractionSize","priceIsBold"],
        requireAll = false
    )
    @JvmStatic
    fun style(
        textView: TextView,
        originalStr: String?,
        labelSize: Int,
        numberSize: Int,
        fractionSize: Int,
        isBold:Boolean = false,
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
        }else{
            minPrice = format(originalStr)
        }
        val typeface = ResourcesCompat.getFont(textView.context, R.font.medium)?: Typeface.DEFAULT
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
//        val spannedStr = SpannableString(formatStr)
//        spannedStr.setSpan(AbsoluteSizeSpan(sp2px(labelSize)), 0, 1, SPAN_INCLUSIVE_EXCLUSIVE)
//        spannedStr.setSpan(
//            AbsoluteSizeSpan(sp2px(numberSize)),
//            1,
//            formatStr.length - 3,
//            SPAN_INCLUSIVE_EXCLUSIVE
//        )
//        spannedStr.setSpan(
//            AbsoluteSizeSpan(sp2px(fractionSize)),
//            formatStr.length - 3,
//            formatStr.length,
//            SPAN_INCLUSIVE_EXCLUSIVE
//        )
        textView.text = result.create()
    }

    @JvmStatic
    fun format(number: String): String {
       val vaStr =  if (number.indexOf(",")!=-1){
            number.replace(",","")
        }else{
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
}