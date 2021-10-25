package com.dyn.net.api.errorhandler

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.functions.Function

class HttpErrorHandler<T> :Function<Throwable, Observable<T>>{
    @Throws
    override fun apply(throwable: Throwable): Observable<T> {
        return Observable.error(ExceptionHandle.handleException(throwable))
    }
}