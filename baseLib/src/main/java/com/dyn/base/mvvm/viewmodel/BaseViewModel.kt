package com.dyn.base.mvvm.viewmodel

import android.graphics.Color
import androidx.annotation.FloatRange
import androidx.annotation.StringRes
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import autodispose2.autoDispose
import com.dyn.base.common.handlerThread
import com.dyn.base.customview.BaseCustomModel
import com.dyn.base.customview.ICustomViewActionListener
import com.dyn.base.ui.base.AutoDisposeLifecycleScopeProvider
import com.dyn.base.ui.base.DialogStatus
import com.dyn.base.ui.weight.CustomToastView
import com.dyn.base.ui.weight.header.CommonHeaderModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * viewModel 基类
 * */
open class BaseViewModel : ViewModel(), LifecycleObserver, BaseCustomModel {
    protected open val autoDisposeLifecycleScopeProvider = AutoDisposeLifecycleScopeProvider()
    val mShortToastStr = MutableLiveData<CustomToastView.CustomToastBean>()
    val mLongToastStr = MutableLiveData<CustomToastView.CustomToastBean>()
    val mLoadingDialogStatus = MutableLiveData<@DialogStatus.EnumDialogStatus Int>()
    var mCustomViewActionListener: ICustomViewActionListener? = null
    val mHasTitle = MutableLiveData<Boolean>(true)
    val mLifeCycle = MutableLiveData<LifecycleOwner>()
    val mBackAction = MutableLiveData(false)
    open val mBannerIndicatorType = 0
    val mContentScrollYSize = MutableLiveData<Int>()
    open val mCommonHeaderModel = CommonHeaderModel()

    fun startTimer(count: Long, data: MutableLiveData<Int>,isPost :Boolean = false): Disposable {
        return Observable.intervalRange(0, count, 0, 1, TimeUnit.SECONDS).handlerThread()
            .autoDispose(autoDisposeLifecycleScopeProvider).subscribe {
                val c = (count - it - 1).toInt()
                if (isPost){
                    data.postValue(c)
                }else{
                    data.value = c
                }
            }
    }

    fun startTimer(count: Long, data: ObservableField<Int>): Disposable {
        return Observable.intervalRange(0, count, 0, 1, TimeUnit.SECONDS).handlerThread()
            .autoDispose(autoDisposeLifecycleScopeProvider).subscribe {
                data.set((count - it - 1).toInt())
            }
    }

    fun setHeaderTitle(title: String) {
        mCommonHeaderModel.title.set(title)
    }

    fun showHeaderFinish() {
        mCommonHeaderModel.hasFinish.set(true)
    }

    fun hideHeaderFinish() {
        mCommonHeaderModel.hasFinish.set(false)
    }

    fun showHeaderLastRight() {
        mCommonHeaderModel.hasRightLast.set(true)
    }

    fun hideHeaderLastRight() {
        mCommonHeaderModel.hasRightLast.set(false)
    }

    open fun setHeaderOffset(@FloatRange(from = 0.0, to = 1.0) scrollOffset: Float) {
        val color = mCommonHeaderModel.bg.get()
        color?.let {
            val rColor = Color.argb(
                (scrollOffset * 255).toInt(),
                Color.red(color),
                Color.green(color),
                Color.blue(color)
            )
            mCommonHeaderModel.bg.set(rColor)
        }
        if (scrollOffset >= 0.5) {
            //offset 0.5-1  alpha 0->1
            val offset = scrollOffset * 2f - 1
            mCommonHeaderModel.titleAlpha.set(offset)
            mCommonHeaderModel.backStyle.drawableTint.set(
                Color.argb(
                    (255 * offset).toInt(),
                    0,
                    0,
                    0
                )
            )
        } else if (scrollOffset in 0f..0.5f) {
            //offset 0->0.5  alpha  1->0
            val offset = 1 - scrollOffset * 2f
            mCommonHeaderModel.titleAlpha.set(offset)
            mCommonHeaderModel.backStyle.drawableTint.set(
                Color.argb(
                    (255 * offset).toInt(),
                    255,
                    255,
                    255
                )
            )
        }
    }

    fun showShortToast(
        str: String,
        @CustomToastView.ToastType type: Int = CustomToastView.TYPE_TIPS
    ) {
        mShortToastStr.value = CustomToastView.CustomToastBean(str, -1, type)
    }

    fun showShortToast(
        @StringRes str: Int,
        @CustomToastView.ToastType type: Int = CustomToastView.TYPE_TIPS
    ) {
        mShortToastStr.value = CustomToastView.CustomToastBean(null, str, type)
    }

    fun showLongToast(
        str: String,
        @CustomToastView.ToastType type: Int = CustomToastView.TYPE_TIPS
    ) {
        mLongToastStr.value = CustomToastView.CustomToastBean(str, -1, type)
    }

    fun showLongToast(
        @StringRes str: Int,
        @CustomToastView.ToastType type: Int = CustomToastView.TYPE_TIPS
    ) {
        mLongToastStr.value = CustomToastView.CustomToastBean(null, str, type)
    }

    fun showDialogCancel() {
        mLoadingDialogStatus.value = DialogStatus.LOADING_DIALOG_STATUS_SHOW_CANCEL
    }

    fun showDialogUnCancel() {
        mLoadingDialogStatus.value = DialogStatus.LOADING_DIALOG_STATUS_SHOW_UNCANCEL
    }

    fun hideDialog() {
        mLoadingDialogStatus.value = DialogStatus.LOADING_DIALOG_STATUS_HIDE
    }

    fun postBackAction() {
        mBackAction.value = true
    }

    override fun onCleared() {
        autoDisposeLifecycleScopeProvider.onCleared()
        hideDialog()
        super.onCleared()
    }
}