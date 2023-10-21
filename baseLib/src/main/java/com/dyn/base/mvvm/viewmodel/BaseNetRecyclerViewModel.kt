package com.dyn.base.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.dyn.base.mvvm.model.BaseModel
import com.dyn.base.mvvm.model.IBaseModelListener
import com.dyn.base.mvvm.model.ResultPageInfo

/**
 * loadSir + smartRefreshLayout + recyclerView
 * */
abstract class BaseNetRecyclerViewModel<PAGE_MODEL : BaseModel<*, MutableList<PAGE_DATA>>, PAGE_DATA> :
    BaseRecyclerViewModel<PAGE_DATA>(), IBaseModelListener<Any?> {


    private var mPageModel: PAGE_MODEL? = null

    /**
     * 框架加载页面数据对应的model
     * */
    abstract fun createPageModel(): PAGE_MODEL

    private fun createAndRegisterPageModel() {
        if (mPageModel == null) {
            mPageModel = createPageModel()
            mPageModel?.registerListener(object : IBaseModelListener<MutableList<PAGE_DATA>> {
                /**
                 * 分页 缓存 包括下拉刷新，上拉加载更多 请求成功后的逻辑
                 * */
                override fun onSuccess(
                    model: BaseModel<*, MutableList<PAGE_DATA>>,
                    resultData: MutableList<PAGE_DATA>?,
                    pageInfo: ResultPageInfo?
                ) {
                    if (model.isPaging()) {
                        //是分页类型
                        pageInfo?.let {
                            if (it.isFirstPage) {
                                //第一页
                                if (it.isEmpty) {
                                    dataList.value = null
                                    showPageEmpty()
                                } else {
                                    showPageSuccess()
                                    dataList.value = resultData

                                }
                            } else {
                                //加载更多
                                if (it.isEmpty.not() && resultData is MutableList && resultData.size > 0) {
                                    //更多不为空
                                    addDataList.value = resultData
                                }

                            }
                            if (it.hasMore.not()) {
                                //没有更多
                                showPageNoMoreData()
                            } else {
                                showPageHasMoreData()
                            }
                        }
                    } else {
                        //不是分页类型
                        showPageSuccess()
                        if (resultData?.size ?: 0 > 0) {
                            data.value = resultData!![0]
                        }
                    }
                    finishSmartRefreshStatus()
                    onPageSuccess(model, resultData, pageInfo)
                }

                override fun onFail(
                    model: BaseModel<*, MutableList<PAGE_DATA>>,
                    throwable: Throwable,
                    pageInfo: ResultPageInfo?
                ) {
                    if (model.isPaging()) {
                        if (pageInfo?.isFirstPage == true) {
                            showPageFail(throwable)
                        } else {
                            showPageMoreFail()
                        }
                    } else {
                        showPageFail(throwable)
                    }
                    finishSmartRefreshStatus()
                    onPageFail(model, throwable, pageInfo)
                }

            }
            )
        }
    }

    fun getCurrentPageModel(): PAGE_MODEL {
        createAndRegisterPageModel()
        return mPageModel!!
    }

    /**
     * 获取缓存数据并加载
     */
    override fun loadSirReload() {
        createAndRegisterPageModel()
        mPageModel?.getCachedDataAndLoad()
    }

    /**
     * 刷新
     */
    override fun refresh() {
        createAndRegisterPageModel()
        mPageModel?.refresh()
    }

    /**
     * 加载下一页
     */
    override fun loadNextPage() {
        createAndRegisterPageModel()
        mPageModel?.loadNextPage()
    }

    /**
     * 没有更多数据
     * */
    protected open fun showPageNoMoreData() {
        enableLoadMore.value = false
        enableAutoLoadMore.value = false
    }

    /**
     * 有更多数据
     * */
    protected open fun showPageHasMoreData() {
        enableLoadMore.value = true
        enableAutoLoadMore.value = true
    }

}