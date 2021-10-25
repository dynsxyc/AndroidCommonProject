package com.dyn.base.utils

import android.app.Application
import android.view.Gravity
import android.widget.Toast
import com.blankj.utilcode.util.ToastUtils
import com.dyn.base.ui.base.BaseApplication
import com.dyn.base.ui.weight.CustomToastView

object BaseToastUtils {
    fun showToast(
        text: String,
        @CustomToastView.ToastType type: Int = CustomToastView.TYPE_TIPS,
        duration: Int = Toast.LENGTH_SHORT
    ) {
        if (text.isNotBlank()) {
            val  mToastView =
                (BaseApplication.AppContext.applicationContext as BaseApplication).createToastView(
                    BaseApplication.AppContext
                )
            mToastView.setText(text)
            mToastView.setImg(type)
//            when (duration) {
//                Toast.LENGTH_LONG -> {
//                    ToastUtils.make().setDurationIsLong(true).setGravity(Gravity.CENTER, 0, 0)
//                        .show(mToastView)
//                }
//                Toast.LENGTH_SHORT -> {
//                    ToastUtils.make().setGravity(Gravity.CENTER, 0, 0).show(mToastView)
//                }
//            }
            Toast.makeText(BaseApplication.AppContext,text,duration).let {
                it.view = mToastView
                it.setGravity(Gravity.CENTER,0,0)
                it.show()
            }
        }
    }
}