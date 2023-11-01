package com.dyn.base.ui.weight

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.dyn.base.R
import com.dyn.base.utils.StrUtils
import com.orhanobut.logger.Logger

/**
 * Created by dyn on 2018/7/17.
 */
class ProgressLoading private constructor(context: Context, theme: Int) : Dialog(context, theme) {
    //        @Volatile
//        private var instance: ProgressLoading? = null
//        fun getInstance(context: Context) = create(context)
//            instance ?: synchronized(context) {
//                instance ?: create(context).also { instance = it }
//            }

    private var loadingView: ImageView? = null
    private var loadingTv: TextView? = null
    constructor(context: Context): this(context,R.style.LightProgressDialog) {
        setContentView(R.layout.progress_dialog)
        window?.attributes?.gravity = Gravity.CENTER
        val lp = window?.attributes
        lp?.dimAmount = 0.2f
        window?.attributes = lp
        loadingView = findViewById(R.id.iv_loading)
        loadingTv = findViewById(R.id.loadingTv)
    }

    private fun showLoading() {
        try {
            if (this.isShowing.not()) {
                Logger.i("加载弹框显示")
                val an = ObjectAnimator.ofFloat(loadingView!!, "rotation", 0f, 360f)
                an.duration = 800
                an.repeatCount = ValueAnimator.INFINITE
                an.interpolator = LinearInterpolator()
                an.start()
                this.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

   fun showCancelableLoading() {
        Logger.i("加载弹框show 1")
        this.setCancelable(true)
        this.setCanceledOnTouchOutside(true)
        showLoading()
    }

    fun showUnCancelableLoading(tipsStr: String? = null) {
        Logger.i("加载弹框show 2")
        this.setCancelable(false)
        this.setCanceledOnTouchOutside(false)
        if (tipsStr.isNullOrEmpty().not()) {
            loadingTv?.text = tipsStr
        } else {
            loadingTv?.text = StringUtils.getString(R.string.loading)

        }
        showLoading()
    }

    fun hideLoading() {
        this.dismiss()
    }
}