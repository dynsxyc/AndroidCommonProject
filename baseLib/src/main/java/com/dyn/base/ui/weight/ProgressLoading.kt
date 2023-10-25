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

/**
 * Created by dyn on 2018/7/17.
 */
class ProgressLoading private constructor(context: Context, theme: Int) : Dialog(context, theme) {
    companion object {
//        @Volatile
//        private var instance: ProgressLoading? = null
        fun getInstance(context: Context) = create(context)
//            instance ?: synchronized(context) {
//                instance ?: create(context).also { instance = it }
//            }

        /**
         * 非单例创建
         * 解决 选择图片后 图片上传中多个接口请求导致弹框不显示的问题
         * */
        fun createNew(context: Context): ProgressLoading {
            return create(context)
        }


        private lateinit var mDialog: ProgressLoading
        private var loadingView: ImageView? = null
        private var loadingTv: TextView? = null
        private fun create(context: Context): ProgressLoading {
            mDialog = ProgressLoading(context, R.style.LightProgressDialog)
            mDialog.setContentView(R.layout.progress_dialog)
            mDialog.window?.attributes?.gravity = Gravity.CENTER
            val lp = mDialog.window?.attributes
            lp?.dimAmount = 0.2f
            mDialog.window?.attributes = lp
            loadingView = mDialog.findViewById(R.id.iv_loading)
            loadingTv = mDialog.findViewById(R.id.loadingTv)
            return mDialog
        }
    }

    private fun showLoading() {
        try {
            if (isShowing.not()) {
                val an = ObjectAnimator.ofFloat(loadingView!!,"rotation",0f,360f)
                an.duration = 800
                an.repeatCount = ValueAnimator.INFINITE
                an.interpolator = LinearInterpolator()
                an.start()
                mDialog.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showCancelableLoading() {
        mDialog.setCancelable(true)
        mDialog.setCanceledOnTouchOutside(true)
        showLoading()
    }

    fun showUnCancelableLoading(tipsStr:String? = null) {
        mDialog.setCancelable(false)
        mDialog.setCanceledOnTouchOutside(false)
        if (tipsStr.isNullOrEmpty().not()){
            loadingTv?.text = tipsStr
        }else{
            loadingTv?.text = StringUtils.getString(R.string.loading)

        }
        showLoading()
    }

    fun hideLoading() {
        if (isShowing) {
            mDialog.dismiss()
        }
    }
}