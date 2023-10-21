package com.dyn.net.api.errorhandler

import com.dyn.net.api.ServerException
import com.dyn.net.api.beans.IBaseResponse
import io.reactivex.rxjava3.functions.Function
/**
 * HandleFuc处理以下网络错误：
 * 1、应用数据的错误会抛RuntimeException；
 */
class ApiDataHandler<T> : Function<T,T> {
    override fun apply(t: T): T {
        if (t is IBaseResponse<*> && t.isSuccess().not()){
            throw ServerException(t.getStatus(),t.getShowMessage())
        }
        return t
    }
}