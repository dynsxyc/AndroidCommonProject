package com.dyn.base.customview

interface ICustomView<T:BaseCustomModel> {
    fun setData(data:T)
}