package com.dyn.base.binding_adapter

import android.view.View
import androidx.annotation.IdRes
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.PageTransformer
import com.dyn.base.common.findViewByParent
import com.dyn.base.ui.bannerindicator.NumIndicator
import com.orhanobut.logger.Logger
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.indicator.BaseIndicator
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.indicator.Indicator
import com.youth.banner.indicator.RectangleIndicator
import com.youth.banner.listener.OnPageChangeListener

object BindingBannerAdapter {

    @BindingAdapter(value = ["bannerLifecycle"], requireAll = false)
    @JvmStatic
    fun bannerLifecycle(view: Banner<*, BannerAdapter<*, *>>, owner: LifecycleOwner?) {
        owner?.let {
            view.addBannerLifecycleObserver(it)
        }
    }
    @BindingAdapter(value = ["bannerIsUserInputEnabled"], requireAll = false)
    @JvmStatic
    fun bannerIsUserInputEnabled(view: Banner<*, BannerAdapter<*, *>>, isUserInputEnabled: Boolean) {
        view.setUserInputEnabled(isUserInputEnabled)
    }
    @BindingAdapter(value = ["bannerToPosition","bannerSmoothScroll"], requireAll = false)
    @JvmStatic
    fun bannerToPosition(view: Banner<*, BannerAdapter<*, *>>, position: Int,smoothScroll:Boolean?) {
        view.setCurrentItem(position,smoothScroll?:true)
    }
    @InverseBindingAdapter(event = "OnBannerPageChangeListener" , attribute = "bannerToPosition")
    @JvmStatic
    fun getBannerPosition(view: Banner<*, BannerAdapter<*, *>>):Int= view.getCurrentItem()

    @BindingAdapter("OnBannerPageChangeListener")
    @JvmStatic
    fun inBannerListener(view: Banner<*, BannerAdapter<*, *>>,listener: InverseBindingListener?){
        view.addOnPageChangeListener(object :OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                listener?.onChange()
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    @BindingAdapter(value = ["bannerLoopTime"], requireAll = false)
    @JvmStatic
    fun bannerLoopTime(view: Banner<*, BannerAdapter<*, *>>, loopTime: Long) {
        if (loopTime>0){
            view.isAutoLoop(true)
            view.setLoopTime(loopTime)
            view.start()
        }else{
            view.isAutoLoop(true)
            view.stop()
        }
    }
    @BindingAdapter(value = ["bannerStartPosition"], requireAll = false)
    @JvmStatic
    fun bannerStartPosition(view: Banner<*, BannerAdapter<*, *>>, startPosition: Int) {
            view.setStartPosition(startPosition)
    }

    @BindingAdapter(value = ["bannerLimitCount"], requireAll = false)
    @JvmStatic
    fun bannerLimitCount(view: Banner<*, BannerAdapter<*, *>>, count: Int) {
        view.getViewPager2().offscreenPageLimit = count
    }

    @BindingAdapter(value = ["bannerData"], requireAll = false)
    @JvmStatic
    fun bannerData(
        view: Banner<*, BannerAdapter<*, *>>,
        data: List<Nothing>?,
    ) {
        data?.let {
            if (it.isNotEmpty()) {
                view.setDatas(it)
            }
        }
    }

    @BindingAdapter(
        value = ["bannerDataWidthToFirst", "bannerDataWidthToPosition"],
        requireAll = false
    )
    @JvmStatic
    fun bannerDataWidthToFirst(
        view: Banner<*, BannerAdapter<*, *>>,
        data: List<Nothing>?,
        position: Int = 0,
    ) {
        data?.let {
            if (it.isNotEmpty()) {
                view.stop()
                view.setDatas(it)
                view.postDelayed({
                    val pager = view.getViewPager2()
                    pager.requestTransform()
                },100)
            }
        }
    }

    @BindingAdapter(value = ["bannerAdapter", "customBannerListener"], requireAll = false)
    @JvmStatic
    fun bannerData(
        view: Banner<*, BannerAdapter<*, *>>,
        adapter: BannerAdapter<*, *>?,
        listener: OnCustomBannerListener?
    ) {
        if (view.getAdapter() == null && adapter != null) {
            view.setAdapter(adapter)
            listener?.let {
                adapter.setOnBannerListener { data, position ->
                    listener.onClick(view, data, position)
                }
            }
        }
    }


    /**
     * 给banner添加画廊效果
     * */
    @BindingAdapter(
        value = ["bannerGalleryStartWidth", "bannerGalleryEndWidth", "bannerGalleryMarginSpanSize", "bannerGalleryScale"],
        requireAll = false
    )
    @JvmStatic
    fun bannerGalleryStyle(
        banner: Banner<*, BannerAdapter<*, *>>,
        startWidth: Int? = 0,
        endWidth: Int? = 0,
        marginSpanSize: Int? = 0,
        scale: Float? = 1f
    ) {
        banner?.let {
            it.setBannerGalleryEffect(
                startWidth ?: 0,
                endWidth ?: 0,
                marginSpanSize ?: 0,
                scale ?: 1f
            )

        }
    }
    /**
     * 给banner添加画廊效果
     * */
    @BindingAdapter(
        value = ["bannerAddCustomTransformer"],
        requireAll = false
    )
    @JvmStatic
    fun bannerAddCustomTransformer(
        banner: Banner<*, BannerAdapter<*, *>>,
        transformer: MutableList<*>? = null,
    ) {
        transformer?.let { list ->
            list.forEach {
               banner.addPageTransformer(it as PageTransformer)
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

    @BindingAdapter(value = ["bannerAutoLoop"], requireAll = false)
    @JvmStatic
    fun bannerAutoLoop(view: Banner<*, BannerAdapter<*, *>>, data: Boolean) {
        view?.let { banner ->
            banner.isAutoLoop(data)
        }
    }

    @BindingAdapter(value = ["bannerUserInputEnabled"], requireAll = false)
    @JvmStatic
    fun bannerUserInputEnabled(view: Banner<*, BannerAdapter<*, *>>, data: Boolean) {
        view?.let { banner ->
            banner.setUserInputEnabled(data)
        }
    }

    @BindingAdapter(value = ["bannerTouchSlop"], requireAll = false)
    @JvmStatic
    fun bannerTouchSlop(view: Banner<*, BannerAdapter<*, *>>, data: Float) {
        view?.let { banner ->
            banner.setTouchSlop(data.toInt())
        }
    }

//    @BindingAdapter(value = ["bannerListener"], requireAll = false)
//    @JvmStatic
//    fun bannerListener(banner: Banner<*, BannerAdapter<*, *>>, listener: OnBannerListener<*>?) {
//        banner?.let {
//            it.setOnBannerListener(listener)
//
//        }
//    }

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
    interface OnCustomBannerListener {
        fun onClick(v: View, data: Any, position: Int)
    }

    @BindingAdapter(value = ["setBannerIndicator"], requireAll = false)
    @JvmStatic
    fun setBannerIndicator(view: Banner<*, BannerAdapter<*, *>>, data: Indicator? = null) {
        data?.let {
            view.setIndicator(it)
        }
    }

    @BindingAdapter(value = ["bannerIndicator"], requireAll = false)
    @JvmStatic
    fun bannerIndicator(view: Banner<*, BannerAdapter<*, *>>, data: Int) {
        view?.let {
            when (data) {
                0 ->
                    it.setIndicator(RectangleIndicator(it.getContext()))

                1 -> {
                    it.setIndicator(NumIndicator(it.getContext()))
                        .setIndicatorGravity(IndicatorConfig.Direction.RIGHT)
                }

                else ->
                    it.setIndicator(CircleIndicator(it.getContext()))
            }
        }
    }


    @BindingAdapter(value = ["bannerIndicatorByViewId"], requireAll = false)
    @JvmStatic
    fun bannerIndicatorByViewId(view: Banner<*, BannerAdapter<*, *>>, @IdRes viewId: Int) {
        val baseIndicator = view.findViewByParent<BaseIndicator>(viewId)
        baseIndicator?.let {
            view.setIndicator(it, false)
        }
    }

}