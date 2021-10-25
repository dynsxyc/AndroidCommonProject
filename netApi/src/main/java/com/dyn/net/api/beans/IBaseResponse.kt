package com.dyn.net.api.beans


interface IBaseResponse<T>{
    fun getStatus():Int
    fun isSuccess():Boolean
    fun getShowMessage():String
    fun getData():T?
}