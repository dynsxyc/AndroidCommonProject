package com.dyn.base.ui.weight

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.dyn.base.R

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
        private var animation: Animation? = null
        private var loadingView: ImageView? = null
        private fun create(context: Context): ProgressLoading {
            mDialog = ProgressLoading(context, R.style.LightProgressDialog)
            mDialog.setContentView(R.layout.progress_dialog)
            mDialog.window?.attributes?.gravity = Gravity.CENTER
            val lp = mDialog.window?.attributes
            lp?.dimAmount = 0.2f
            mDialog.window?.attributes = lp
            loadingView = mDialog.findViewById(R.id.iv_loading)
            animation =
                AnimationUtils.loadAnimation(context, R.anim.progress_load)

            return mDialog
        }
    }

    private fun showLoading() {
        try {
            if (isShowing.not()) {
                super.show()
                loadingView?.startAnimation(animation)
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

    fun showUnCancelableLoading() {
        mDialog.setCancelable(false)
        mDialog.setCanceledOnTouchOutside(false)
        showLoading()
    }

    fun hideLoading() {
        if (isShowing) {
            super.dismiss()
            loadingView?.clearAnimation()
        }
    }
}