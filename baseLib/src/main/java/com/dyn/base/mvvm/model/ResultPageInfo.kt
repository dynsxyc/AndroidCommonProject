package com.dyn.base.mvvm.model
/**
 * 自定义分页数据 结合baseModel 使用
 * */
data class ResultPageInfo(val isFirstPage:Boolean = true, val isEmpty:Boolean = false, val hasMore:Boolean = false)