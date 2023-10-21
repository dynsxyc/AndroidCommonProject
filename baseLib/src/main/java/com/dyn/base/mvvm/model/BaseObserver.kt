package com.dyn.base.mvvm.model

import com.dyn.base.ui.base.AutoDisposeLifecycleScopeProvider
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseObserver<T>(
    val autoDisposeProvider: AutoDisposeLifecycleScopeProvider,
    private val listener: INetDataObserver<T>?
) : Observer<T> {
    override fun onSubscribe(d: Disposable?) {
    }

    override fun onNext(t: T) {
        listener?.onSuccess(t, false)
        onSuccess(t)
    }

    override fun onError(e: Throwable) {
        listener?.onFail(e)
        onFail(e)
    }

    override fun onComplete() {
    }

    abstract fun onFail(e: Throwable)

    abstract fun onSuccess(t: T)
}