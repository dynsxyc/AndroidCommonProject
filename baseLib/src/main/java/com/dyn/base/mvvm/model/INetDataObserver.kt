package com.dyn.base.mvvm.model

/**
 * 网络获取数据回调，缓存数据回调
 * */
interface INetDataObserver<NET_DATA> {
    fun onSuccess(netData: NET_DATA,isFromCached:Boolean)
    fun onFail(e:Throwable)
}