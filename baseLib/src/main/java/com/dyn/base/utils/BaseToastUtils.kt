package com.dyn.base.utils

import android.app.Application
import android.content.res.Resources
import android.text.TextUtils
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.DrawableRes
import com.blankj.utilcode.util.ToastUtils
import com.dyn.base.ui.base.BaseApplication
import com.dyn.base.ui.weight.CustomToastView
import com.dyn.base.ui.weight.CustomToastView.Companion.TYPE_FAIL
import com.dyn.base.ui.weight.CustomToastView.Companion.TYPE_SUCCESS
import com.dyn.base.ui.weight.CustomToastView.Companion.TYPE_TIPS
import com.orhanobut.logger.Logger
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

object BaseToastUtils {
    private var currentToastStr: String? = null
    fun showToast(
        text: String,
        @DrawableRes iconRes: Int = -1,
        @CustomToastView.ToastType type: Int = CustomToastView.TYPE_TIPS,
        duration: Int = Toast.LENGTH_SHORT
    ) {
        if (text.isNotBlank()) {
            if (TextUtils.equals(text, currentToastStr)) {
                return
            }
            currentToastStr = text
            val mToastView =
                (BaseApplication.AppContext.applicationContext as BaseApplication).createToastView(
                    BaseApplication.AppContext
                )
            mToastView.setText(text)
            try {
                if (iconRes != -1) {
                    mToastView.setImgRes(iconRes)
                } else {
                    mToastView.setImg(type)
                }
            } catch (e: Exception) {
                mToastView.setImg(type)
            }
            Toast.makeText(BaseApplication.AppContext, text, duration).let {
                it.view = mToastView
                it.setGravity(Gravity.CENTER, 0, 0)
                it.show()
            }
            Logger.i("toast 显示内容->$currentToastStr")
            Observable.intervalRange(0, 4, 0, 1, TimeUnit.SECONDS).subscribe {
                Logger.i("toast timer->$it")
                if (it == 3L){
                    currentToastStr = null
                }
            }
        }
    }
}