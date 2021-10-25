package com.dyn.base.binding_adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
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
    @BindingAdapter(value = ["registerPage"])
    @JvmStatic
    fun registerPageView(view: View, reloadListener: Callback.OnReloadListener) {
        val tag = view.getTag(R.id.loadSir_bind_service)
        if (tag == null) {
            val loadService = LoadSir.getDefault().register(view, reloadListener)
            view.setTag(R.id.loadSir_bind_service, loadService)
        }

    }

    @BindingAdapter(value = ["changePage"])
    @JvmStatic
    fun changePageStatus(view: View, status: LoadPageStatus) {
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
            tag.setCallBack(EmptyCallback::class.java) { context, view ->
                titleText?.let {
                    val titleTv =
                        view.findViewById<androidx.appcompat.widget.AppCompatTextView>(R.id.mLayoutEmptyTitleTv)
                    titleTv.text = it
                }
                val content = view.findViewById<View>(R.id.ll_empty)
                content?.let {
                    if (minHeight > 0f)
                        it.minimumHeight = minHeight.toInt()
                }
                desText?.let {
                    val desTv =
                        view.findViewById<androidx.appcompat.widget.AppCompatTextView>(R.id.mLayoutEmptyDesTv)
                    desTv.text = it
                }
                try {
                    emptyImg?.let {
                        val emptyImageV = view.findViewById<ImageView>(R.id.mLayoutEmptyImg)
                        emptyImageV.setImageResource(it)
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
    }
}