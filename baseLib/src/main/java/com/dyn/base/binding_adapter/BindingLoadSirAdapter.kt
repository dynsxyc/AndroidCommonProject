package com.dyn.base.binding_adapter

import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.dyn.base.R
import com.dyn.base.loadsir.EmptyCallback
import com.dyn.base.loadsir.ErrorCallback
import com.dyn.base.loadsir.LoadingCallback
import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.orhanobut.logger.Logger
import java.lang.Exception

object BindingLoadSirAdapter {
    /**
     * @param isUnRegisterPage 是否不用注册页面状态切换  默认 否false 表示注册 true 不注册
     * */
    @BindingAdapter(value = ["registerPage", "isUnRegisterPage"], requireAll = false)
    @JvmStatic
    fun registerPageView(
        view: View,
        reloadListener: Callback.OnReloadListener?,
        isUnRegisterPage: Boolean = false
    ) {
        val tag = view.getTag(R.id.loadSir_bind_service)
        if (tag == null && !isUnRegisterPage && reloadListener != null) {
            val loadService = LoadSir.getDefault().register(view, reloadListener)
            view.setTag(R.id.loadSir_bind_service, loadService)
        }

    }

    @BindingAdapter(value = ["changePage"])
    @JvmStatic
    fun changePageStatus(view: View, status: LoadPageStatus?) {
        when (status) {
            LoadPageStatus.SUCCESS -> {
                showCallback(view, SuccessCallback::class.java)
            }

            LoadPageStatus.LOADING -> {
                showCallback(view, LoadingCallback::class.java)
            }

            LoadPageStatus.EMPTY -> {
                showCallback(view, EmptyCallback::class.java)
            }

            else -> {
                showCallback(view, ErrorCallback::class.java)
            }
        }
    }

    @BindingAdapter(
        value = ["emptyTitleText", "emptyDesText", "emptyImg", "emptyMineHeight"],
        requireAll = false
    )
    @JvmStatic
    fun emptyViewData(
        view: View,
        titleText: String?,
        desText: String?,
        @DrawableRes emptyImg: Int?,
        minHeight: Float
    ) {
        val tag = view.getTag(R.id.loadSir_bind_service)
        if (tag != null && tag is LoadService<*>) {
            tag.setCallBack(EmptyCallback::class.java) { _, v ->
                val titleTv =
                    v.findViewById<androidx.appcompat.widget.AppCompatTextView>(R.id.mLayoutEmptyTitleTv)
              titleTv?.let {
                  if (TextUtils.isEmpty(titleText)) {
                      titleTv.visibility = View.GONE
                  } else {
                      titleTv.visibility = View.VISIBLE
                      titleTv.text = titleText
                  }
              }
                val content = v.findViewById<View>(R.id.ll_empty)
                content?.let { v ->
                    if (minHeight > 0f) {
                        v.layoutParams?.let {
                            Logger.i("loadSir-------->$it")
                            it.height = LayoutParams.WRAP_CONTENT
                            v.layoutParams = it
                        }
                        v.minimumHeight = minHeight.toInt()
                    }
                }
                val desTv =
                    v.findViewById<androidx.appcompat.widget.AppCompatTextView>(R.id.mLayoutEmptyDesTv)
              desTv?.let {
                  if (TextUtils.isEmpty(desText)) {
                      desTv.visibility = View.GONE
                  } else {
                      desTv.visibility = View.VISIBLE
                      desTv.text = desText
                  }
              }
                try {
                    emptyImg?.let {
                        val emptyImageV = v.findViewById<ImageView>(R.id.mLayoutEmptyImg)
                        emptyImageV?.setImageResource(it)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun showCallback(view: View, clazz: Class<out Callback>) {
        val tag = view.getTag(R.id.loadSir_bind_service)
        if (tag != null && tag is LoadService<*>) {
            tag.showCallback(clazz)
        }
    }


    enum class LoadPageStatus {
        SUCCESS,
        LOADING,
        EMPTY,
        ERROR
    }
}