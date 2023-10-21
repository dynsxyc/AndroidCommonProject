package com.dyn.base.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.StringUtils
import com.dyn.base.R
import com.dyn.base.binding_adapter.BindingLoadSirAdapter
import com.kingja.loadsir.callback.Callback
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 页面包含loadsir 和下拉刷新的 viewModel
 * */
abstract class BaseLoadSirAndRefreshViewModel : BaseViewModel() {
    /************************loadSir  start ************************/
    open var pageStatus = MutableLiveData(
        BindingLoadSirAdapter.LoadPageStatus.LOADING
    )//loadSir 加载当前状态

    open val emptyTitleStr = MutableLiveData(StringUtils.getString(R.string.emptyTitleStr))
    open val emptyDesStr = MutableLiveData(StringUtils.getString(R.string.emptyDesStr))
    open val emptyImgDrawable = MutableLiveData(R.drawable.img_data_empty)

    val onReloadListener = Callback.OnReloadListener {
        onLoadSirReload()
    }

    /**
     * 显示加载状态 并重新加载
     * */
    open fun onLoadSirReload() {
        showPageLoading()
        loadSirReload()
    }

    /**
     * 显示加载进度  并调用刷新
     * */
    open fun onPageReload() {
        showPageLoading()
        refresh()
    }

    open val mItemDecoration = MutableLiveData<RecyclerView.ItemDecoration>()

    val mHasFixedSize = MutableLiveData(true)

    /**
     * 校验页面加载状态 并重新加载数据
     * */
    open fun checkPageStatusLoadPage() {
        if (pageStatus.value != BindingLoadSirAdapter.LoadPageStatus.SUCCESS) {
            loadSirReload()
        }
    }

    open fun showPageLoading() {
        pageStatus.value = BindingLoadSirAdapter.LoadPageStatus.LOADING
    }

    open fun showPageSuccess() {
        pageStatus.value = BindingLoadSirAdapter.LoadPageStatus.SUCCESS
    }

    open fun showPageFail(exception: Throwable? = null) {
        emptyTitleStr.value = StringUtils.getString(R.string.customErrorTitleStr)
        emptyDesStr.value = StringUtils.getString(R.string.customErrorDesStr)
        emptyImgDrawable.value = R.drawable.img_data_nonet_error
        pageStatus.value = BindingLoadSirAdapter.LoadPageStatus.EMPTY
    }

    open fun showPageEmpty() {
        emptyTitleStr.value = StringUtils.getString(R.string.emptyTitleStr)
        emptyDesStr.value = StringUtils.getString(R.string.emptyDesStr)
        emptyImgDrawable.value = R.drawable.img_data_empty
        pageStatus.value = BindingLoadSirAdapter.LoadPageStatus.EMPTY
    }

    /**
     * 获取缓存数据并加载
     */
    abstract fun loadSirReload()

    /************************loadSir  end ************************/

    /************************SmartRefreshLayout  start ************************/
    val mOnRefreshLoadMoreListener = object : OnRefreshLoadMoreListener {
        override fun onRefresh(refreshLayout: RefreshLayout) {
            isStartRefreshing.value = true
            refresh()
        }

        override fun onLoadMore(refreshLayout: RefreshLayout) {
            isStartLoadMore.value = true
            loadNextPage()
        }

    }


    val finishLoadMore = MutableLiveData<Boolean>() //smartRefresh 标记加载更多结束
    val finishRefresh = MutableLiveData<Boolean>()//smartRefresh 标记刷新结束
    val autoRefresh = MutableLiveData<Boolean>()//smartRefresh 标记自动刷新
    val enableLoadMore = MutableLiveData<Boolean>(true)//是否启用上啦加载更多
    val enableRefresh = MutableLiveData<Boolean>(true)//是否启用刷新
    val enableAutoLoadMore = MutableLiveData<Boolean>(true)//上啦到底部是否自动加载更多
    val setNoMoreData = MutableLiveData<Boolean>(false)//是否显示底部已没有更多数据
    val isStartRefreshing = MutableLiveData(false)//是否开始刷新
    val isStartLoadMore = MutableLiveData(false)//是否开始加载更多

    open fun finishSmartRefreshStatus() {
        finishRefresh.value = true
        finishLoadMore.value = true
    }

    /**
     * 刷新
     */
    abstract fun refresh()

    /**
     * 加载下一页
     */
    abstract fun loadNextPage()

    /************************SmartRefreshLayout  end ************************/
}