package com.dyn.base.binding_adapter

import android.animation.ValueAnimator
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.view.doOnLayout
import androidx.core.view.doOnPreDraw
import androidx.core.view.updateLayoutParams
import androidx.core.widget.NestedScrollView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.orhanobut.logger.Logger

object BindingScrollViewAdapter {

    @JvmStatic
    @BindingAdapter("scrollYTo")
    fun scrollYTo(
        sBt: NestedScrollView,
        newValue: Int
    ) {
        if (sBt.scrollY != newValue)
            sBt.scrollTo(0, newValue)
    }

    @JvmStatic
    @InverseBindingAdapter(
        attribute = "scrollYTo",
        event = "scrollChangeListener"
    )
    fun getOnScrollYChanged(scrollView: NestedScrollView): Int = scrollView.scrollY

    @JvmStatic
    @BindingAdapter("scrollXTo")
    fun scrollXTo(
        sBt: NestedScrollView,
        newValue: Int
    ) {
        if (sBt.scrollY != newValue)
            sBt.scrollTo(newValue, 0)
    }

    @JvmStatic
    @InverseBindingAdapter(
        attribute = "scrollXTo",
        event = "scrollChangeListener"
    )
    fun getOnScrollXChanged(scrollView: NestedScrollView): Int = scrollView.scrollX

    @JvmStatic
    @BindingAdapter(
        "scrollChangeListener",
        requireAll = false
    )
    fun setOnScrollChangeListener(
        scrollView: NestedScrollView,
        bindingListener: InverseBindingListener?
    ) {
        if (bindingListener != null) {
            scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                bindingListener.onChange()
            })
        }
    }

    @JvmStatic
    @BindingAdapter("viewTop")
    fun viewTop(
        sBt: View,
        newValue: Int
    ) {
//        if (sBt.scrollY != newValue)
//            sBt.scrollTo(0, newValue)
    }

    @JvmStatic
    @InverseBindingAdapter(
        attribute = "viewTop",
        event = "viewTopCallBack"
    )
    fun viewTop(view: View): Int = view.top


    @JvmStatic
    @BindingAdapter(
        "viewTopCallBack",
        requireAll = false
    )
    fun viewTopCallBack(
        view: View,
        bindingListener: InverseBindingListener?
    ) {
        if (bindingListener != null) {
            view.post {
                bindingListener.onChange()
            }
        }
    }

    @JvmStatic
    @BindingAdapter("viewHeight")
    fun viewHeight(
        view: View,
        newValue: Int
    ) {
        val params = view.layoutParams
        params?.let {
            if (it.height != newValue) {
                it.height = newValue
                view.layoutParams = it
            }
        }
    }

    @JvmStatic
    @InverseBindingAdapter(
        attribute = "viewHeight",
        event = "viewHeightCallBack"
    )
    fun getViewHeight(view: View): Int {
        return view.measuredHeight
    }

    @JvmStatic
    @BindingAdapter(
        "viewHeightCallBack",
        "viewHeightRequestCallBack",
        requireAll = false
    )
    fun viewHeightCallBack(
        view: View,
        bindingListener: InverseBindingListener?,
        request: Boolean
    ) {
        if (bindingListener != null) {
            view.postDelayed({
                view.doOnPreDraw {
                    Log.i("dyn", "-----------------post----------${it.measuredHeight}--------")
                    bindingListener.onChange()
                }
            }, 300)

        }
    }


    @JvmStatic
    @BindingAdapter(value = ["animationHeightShow", "animationHeight"], requireAll = true)
    fun animationHeight(view: View, isShow: Boolean, viewHeight: Int) {
        if (viewHeight == 0) {
            return
        }
        Logger.d("----update-------开始isShow=$isShow viewHeight=$viewHeight")
        val startHeight = if (isShow) {
            0f
        } else {
            1f
        }
        val endHeight = if (isShow) {
            1f
        } else {
            0f
        }
        val valueAnimator = ValueAnimator.ofFloat(startHeight, endHeight)
        valueAnimator.addUpdateListener {
            view.updateLayoutParams<ConstraintLayout.LayoutParams> {
                Logger.d("----update-------" + it.animatedValue)
                this.height = ((it.animatedValue as Float) * viewHeight).toInt()
                val value = it.animatedValue as Float
                if (value > 0 && view.visibility == View.GONE) {
                    view.visibility = View.VISIBLE
                }
            }
        }
        valueAnimator.doOnEnd {
            view.visibility = if (isShow) View.VISIBLE else View.GONE
        }
        valueAnimator.duration = 300
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.start()
    }


    @JvmStatic
    @BindingAdapter("viewMinHeight")
    fun viewMinHeight(
        view: ViewGroup,
        newValue: Int
    ) {
        if (view.minimumHeight != newValue) {
            Logger.i("----------------------minHeight->$newValue")
            view.minimumHeight = newValue
        }
    }


    /*** view bottom  start*/
    @JvmStatic
    @BindingAdapter("viewBottom")
    fun setViewBottom(
        sBt: View,
        newValue: Int
    ) {

    }

    @JvmStatic
    @InverseBindingAdapter(
        attribute = "viewBottom",
        event = "viewLayoutCallBack"
    )
    fun getViewBottom(view: View): Int = view.bottom

    @JvmStatic
    @BindingAdapter("viewLayoutCallBack")
    fun viewLayoutCallBack(view: View, listener: InverseBindingListener?) {
        listener?.let { bindingListener ->
            view.viewTreeObserver.addOnDrawListener {
//                Logger.i("onLayout ->onchange")
                bindingListener.onChange()
            }
        }
    }

    /*** view bottom  end*/
}