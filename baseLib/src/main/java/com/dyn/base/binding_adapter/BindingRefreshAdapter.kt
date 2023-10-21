package com.dyn.base.binding_adapter

import androidx.databinding.BindingAdapter
import com.dyn.base.R
import com.orhanobut.logger.Logger
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.scwang.smart.refresh.layout.listener.ScrollBoundaryDecider

object BindingRefreshAdapter {
    @BindingAdapter(value = ["refreshListener"])
    @JvmStatic
    fun initRefreshLayout(view: SmartRefreshLayout, refreshListener: OnRefreshLoadMoreListener?) {
        Logger.i( "initRefreshLayout ")
        if (view.getTag(R.id.smart_refresh_listener) == null && refreshListener!= null) {
            view.setOnRefreshLoadMoreListener(refreshListener)
            view.setTag(R.id.smart_refresh_listener, 1)
        }
    }

    @BindingAdapter(value = ["finishLoadMore"])
    @JvmStatic
    fun finishLoadMore(view: SmartRefreshLayout, finishLoadMore: Boolean) {
        if (finishLoadMore) view.finishLoadMore()
    }
    @BindingAdapter(value = ["setNoMoreData"])
    @JvmStatic
    fun setNoMoreData(view: SmartRefreshLayout, noMoreData: Boolean) {
        if (noMoreData) view.setNoMoreData(noMoreData)
    }

    @BindingAdapter(value = ["finishRefresh"])
    @JvmStatic
    fun finishRefresh(view: SmartRefreshLayout, finishRefresh: Boolean) {
        if (finishRefresh) view.finishRefresh()
    }

    @BindingAdapter(value = ["enableLoadMore"])
    @JvmStatic
    fun enableLoadMore(view: SmartRefreshLayout, enableLoadMore: Boolean) {
        view.setEnableLoadMore(enableLoadMore)
    }

    @BindingAdapter(value = ["enableRefresh"])
    @JvmStatic
    fun enableRefresh(view: SmartRefreshLayout, enableRefresh: Boolean) {
        Logger.i("enableRefresh->${enableRefresh} ")
        view.setEnableRefresh(enableRefresh)
    }

    @BindingAdapter(value = ["enableAutoLoadMore"])
    @JvmStatic
    fun enableAutoLoadMore(view: SmartRefreshLayout, enableAutoLoadMore: Boolean) {
        view.setEnableAutoLoadMore(enableAutoLoadMore)
    }

    @BindingAdapter(value = ["autoRefresh"])
    @JvmStatic
    fun autoRefresh(view: SmartRefreshLayout, autoRefresh: Boolean) {
        if (autoRefresh) view.autoRefresh()
    }
    @BindingAdapter(value = ["scrollBoundaryDecider"])
    @JvmStatic
    fun scrollBoundaryDecider(view: SmartRefreshLayout, listener: ScrollBoundaryDecider) {
        view.setScrollBoundaryDecider(listener)
    }
}