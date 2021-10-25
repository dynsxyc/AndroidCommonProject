package com.dyn.base.mvvm.viewmodel

abstract class BaseLocalRecyclerViewModel<T>  : BaseRecyclerViewModel<T>(){
    init {
        enableRefresh.value = false
        enableLoadMore.value = false
    }

    override fun loadSirReload() {
        showPageSuccess()
        dataList.value = requestListData()
    }

    override fun refresh() {

    }

    override fun loadNextPage() {

    }
    abstract fun requestListData():MutableList<T>
}