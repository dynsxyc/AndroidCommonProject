package com.dyn.base.ui.weight

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.IntDef
import androidx.annotation.StringRes
import com.dyn.base.R

class CustomToastView(context: Context):LinearLayout(context) {
    private var mToastImg:ImageView
    private var mToastTv:TextView
    init {
        inflate(context, R.layout.view_custom_toast,this)
        mToastImg = findViewById(R.id.mToastImg)
        mToastTv = findViewById(R.id.mToastTv)
    }
    fun setText(str:String){
        mToastTv.text = str
    }
    fun setImg(@ToastType type:Int= TYPE_TIPS){
        mToastImg.setImageResource(when(type){
            TYPE_SUCCESS->{
                R.drawable.ic_toast_success
            }
            TYPE_FAIL->{
                R.drawable.ic_toast_error
            }
            else ->{
                R.drawable.ic_toast_tips
            }
        }
        )
    }

    companion object{
        const val TYPE_SUCCESS = 1
        const val TYPE_FAIL = 2
        const val TYPE_TIPS = 0
    }

    @IntDef(value = [TYPE_TIPS,TYPE_FAIL,TYPE_SUCCESS])
    annotation class ToastType

    data class CustomToastBean(val toast:String?= null, @StringRes val res: Int = -1, @ToastType val  type: Int = TYPE_TIPS)
}