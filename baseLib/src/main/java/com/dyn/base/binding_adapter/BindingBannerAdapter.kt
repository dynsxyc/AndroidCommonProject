package com.dyn.base.binding_adapter

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import com.dyn.base.ui.bannerindicator.NumIndicator
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.indicator.RectangleIndicator
import com.youth.banner.listener.OnBannerListener

object BindingBannerAdapter {

    @BindingAdapter(value = ["bannerLifecycle"], requireAll = false)
    @JvmStatic
    fun bannerLifecycle(view: Banner<*, BannerAdapter<*, *>>, owner: LifecycleOwner) {
        view.addBannerLifecycleObserver(owner)
    }

    @BindingAdapter(value = ["bannerData", "bannerAdapter","customBannerListener"], requireAll = false)
    @JvmStatic
    fun bannerData(
        view: Banner<*, BannerAdapter<*, *>>,
        data: List<Nothing>?,
        adapter: BannerAdapter<*, *>?,
        listener: OnCustomBannerListener?
    ) {
        if (view.adapter == null && adapter != null) {
            view.adapter = adapter
            listener?.let {
                adapter.setOnBannerListener { data, position ->
                    listener.onClick(view,data,position)
                }
            }
        }
        data?.let {
            if (it.isNotEmpty()) {
                view.setDatas(it)
            }
        }
    }

    @BindingAdapter(value = ["bannerIsIntercept"], requireAll = false)
    @JvmStatic
    fun bannerIsIntercept(view: Banner<*, BannerAdapter<*, *>>, data: Boolean) {
        view?.let { banner ->
            banner.setIntercept(data)
        }
    }

    @BindingAdapter(value = ["bannerTouchSlop"], requireAll = false)
    @JvmStatic
    fun bannerTouchSlop(view: Banner<*, BannerAdapter<*, *>>, data: Float) {
        view?.let { banner ->
            banner.setTouchSlop(data.toInt())
        }
    }

    @BindingAdapter(value = ["bannerListener"], requireAll = false)
    @JvmStatic
    fun bannerListener(banner: Banner<*, BannerAdapter<*, *>>, listener: OnBannerListener<*>?) {
        banner?.let {
            it.setOnBannerListener(listener)

        }
    }

//    @BindingAdapter(value = ["customBannerListener"], requireAll = false)
//    @JvmStatic
//    fun customBannerListener(banner: Banner<*, BannerAdapter<*, *>>, listener: OnCustomBannerListener) {
//        banner.setOnBannerListener { data, position ->
//            listener.onClick(
//                banner,
//                data,
//                position
//            )
//        }
//    }
//
    interface OnCustomBannerListener{
        fun onClick(v:View,data:Any,position:Int)
    }

    @BindingAdapter(value = ["bannerIndicator"], requireAll = false)
    @JvmStatic
    fun bannerIndicator(view: Banner<*, BannerAdapter<*, *>>, data: Int) {
        view?.let {
            when (data) {
                0 ->
                    it.setIndicator(RectangleIndicator(it.context))
                1 -> {
                    it.setIndicator(NumIndicator(it.context))
                        .setIndicatorGravity(IndicatorConfig.Direction.RIGHT)
                }
                else ->
                    it.setIndicator(CircleIndicator(it.context))
            }
        }
    }
}