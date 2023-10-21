package com.dyn.base.common

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.IntRange
import androidx.navigation.AnimBuilder
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.blankj.utilcode.util.KeyboardUtils
import com.dyn.base.R
import com.dyn.base.common.sheduler.AppRxSchedulerProvider
import com.dyn.base.common.sheduler.AppSchedulerProvider
import com.dyn.base.customview.BaseCustomView
import com.jakewharton.rxbinding4.view.clicks
import com.orhanobut.logger.Logger
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

/**
 * rxJava 切换线程
 * */
fun <T : Any> Observable<T>.handlerThread(): Observable<T> {
    return this.subscribeOn(AppSchedulerProvider.instance.io())
        .observeOn(AppSchedulerProvider.instance.ui())
}

fun <T : Any> Single<T>.handlerThread(): Single<T> {
    return this.subscribeOn(AppSchedulerProvider.instance.io())
        .observeOn(AppSchedulerProvider.instance.ui())
}

fun <T> io.reactivex.Flowable<T>.handlerThread(): io.reactivex.Flowable<T> {
    return this.subscribeOn(AppRxSchedulerProvider.instance.io())
        .observeOn(AppRxSchedulerProvider.instance.ui())
}

fun <T> io.reactivex.Single<T>.handlerThread(): io.reactivex.Single<T> {
    return this.subscribeOn(AppRxSchedulerProvider.instance.io())
        .observeOn(AppRxSchedulerProvider.instance.ui())
}


fun View.throttleFirstClick(clickListener: () -> Unit) {
    this.clicks().throttleFirst(500, TimeUnit.MILLISECONDS).subscribe {
        try {
            clickListener()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun View.invalidateAllCustomView() {
    if (this is ViewGroup) {
        for (i in 0 until this.childCount) {
            val child = this.getChildAt(i)
            if (child is BaseCustomView<*, *>) {
                child.invalidateAll()
            }
            if (child is ViewGroup) {
                child.invalidateAllCustomView()
            }
        }
    }
}

fun View.invalidateAllDataCustomView(map: MutableMap<Class<out BaseCustomView<*, *>>, Any>) {
    if (this is ViewGroup) {
        for (i in 0 until this.childCount) {
            val child = this.getChildAt(i)
//            Logger.i("child ->$child")
            if (child is BaseCustomView<*, *>) {
                child.invalidateAll()
                map.forEach {
                    if (child.javaClass == it.key) {
                        child.setNothingData(it.value)
                    }
                }
            }
            if (child is ViewGroup) {
                child.invalidateAllDataCustomView(map)
            }
        }
    }
}

/***
 *@param millis 剩余时间 单位 毫秒级
 * @param precision 显示位数
 **/
fun millis2Data(millis: Long): String {
    var millis = millis
    val sb = StringBuilder()
    val unitLen = intArrayOf(60000, 1000)
    val min = if (millis >= unitLen[0]) {
        val mode = millis / unitLen[0]
        millis -= mode * unitLen[0]
        if (mode < 10) {
            "0".plus(mode)
        } else {
            mode
        }
    } else {
        "00"
    }
    val second = if (millis >= unitLen[1]) {
        val mode = millis / unitLen[1]
        if (mode < 10) {
            "0".plus(mode)
        } else {
            mode
        }
    } else {
        "00"
    }
    sb.append(min).append(":").append(second)
    return sb.toString()
}

/**
 * 从逗号分隔的字符串中  提取第一个字符串
 * */
fun String.getFirstStrByComma(): String? {
    return this?.split(",".toRegex())?.get(0)
}

fun NavController.customNav(
    @IdRes destinationId: Int,
    bundle: Bundle? = null,
    popUpToId: Int? = null,
    inclusive: Boolean = false,
    isLaunchSingleTop: Boolean = false,
    animBuilder: AnimBuilder.() -> Unit = ::createDefaultNavAnim
) {
    this.navigate(
        destinationId,
        bundle,
        navOptions {
            this.launchSingleTop = isLaunchSingleTop
            popUpTo(popUpToId ?: -1) {
                this.inclusive = if (popUpToId == null) {
                    false
                } else
                    inclusive
            }
            this.anim {
                animBuilder.invoke(this)
            }
        }
    )
}

fun NavController.pop(
    @IdRes destinationId: Int,
    inclusive: Boolean = false
) {
    this.popBackStack(destinationId, inclusive)
}

private fun hideKeyboard(controller: NavController) {
    try {
        val activityField = NavController::class.java.getDeclaredField("mActivity")
        activityField.isAccessible = true
        val activity = activityField.get(controller)
        if (activity is Activity) {
            Logger.i("-----------------------隐藏软键盘-------------------------------")
            KeyboardUtils.hideSoftInputByToggle(activity)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


val units = arrayOf(":", ":", "", "")
val unitLen = arrayOf(3600000L, 60000L, 1000L, 1L)

val sb = StringBuilder()
fun millis2FitTimeSpan(m: Long, p: Int): String? {
    if (p <= 0) return null
    var precision = p.coerceAtMost(4);
    var millis = m
    if (millis == 0L) return "0" + units[precision - 1]
    sb.clear()
    if (millis < 0) {
        sb.append("-");
        millis = -millis;
    }
    for (i in 0 until precision) {
        if (millis >= unitLen[i]) {
            val mode = millis / unitLen[i];
            millis -= mode * unitLen[i];
            sb.append(if (mode < 10) "0$mode" else mode).append(units[i]);
        } else {
            sb.append("00${units[i]}")
        }
    }
    return sb.toString();
}

val timeList = mutableListOf<String>()
val unitLenDay = arrayOf(86400000L, 3600000L, 60000L, 1000L, 1L)
fun millis2FitTimeSpanToList(m: Long, time: Array<Long>): MutableList<String> {
    timeList.clear()
    var millis = m
    if (millis == 0L) {
        return timeList
    }
    if (millis < 0) {
        millis = -millis;
    }
    for (i in time.indices) {
        if (millis >= time[i]) {
            val mode = millis / time[i];
            millis -= mode * time[i]
            timeList.add(if (mode < 10) "0$mode" else mode.toString())
        } else {
            timeList.add("00")
        }
    }
    return timeList
}

/**
 * 创建默认的fragment 路由动画
 * */
private fun createDefaultNavAnim(animBuilder: AnimBuilder) {
    animBuilder.apply {
        enter = R.anim.in_from_right
        exit = R.anim.out_to_left
        popEnter = R.anim.in_from_left
        popExit = R.anim.out_to_right
    }
}

fun createFromBottomNavAnim(animBuilder: AnimBuilder) {
    animBuilder.apply {
        enter = R.anim.in_from_bottom
        exit = R.anim.anim_alpha_out
        popEnter = R.anim.anim_alpha_in
        popExit = R.anim.out_to_bottom
    }
}

fun <T> View.findViewByParent(pagerId: Int): T? {
    var result: T? = null
    var p: ViewParent? = parent
    while (result == null) {
        if (p != null && p is ViewGroup) {
            result = p.findViewById(pagerId)
            if (p.id == android.R.id.content) {
                break
            }
            p = p?.parent
        }else{
            break
        }
    }
    return result
}