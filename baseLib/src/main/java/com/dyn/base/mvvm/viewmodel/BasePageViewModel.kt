package com.dyn.base.mvvm.viewmodel

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.dyn.base.mvvm.model.BaseModel
import com.dyn.base.mvvm.model.IBaseModelListener
import com.dyn.base.mvvm.model.ResultPageInfo

/**
 * loadSir + smartRefreshLayout + 单页
 * */
abstract class BasePageViewModel<PAGE_MODEL : BaseModel<*, PAGE_DATA>, PAGE_DATA> :
    BaseLoadSirAndRefreshViewModel(), IBaseModelListener<Any?> {


    private var mOtherModels = createOtherDataModels()
    open fun createOtherDataModels(): MutableList<BaseModel<*, Any?>>? {
        return null
    }

    init {
        mOtherModels?.forEach {
            it.registerListener(this)
        }
    }

    /**
     * 页面数据
     * */
    var data: MutableLiveData<PAGE_DATA?> = MutableLiveData()
    var mRemoveItemPosition = MutableLiveData(-1)
    var mRemoveItemData = MutableLiveData<Any?>(null)
    private var mPageModel: PAGE_MODEL? = null

    /**
     * 框架加载页面数据对应的model
     * */
    abstract fun createPageModel(): PAGE_MODEL

    private fun createAndRegisterPageModel() {
        if (mPageModel == null) {
            mPageModel = createPageModel()
            mPageModel?.registerListener(object : IBaseModelListener<PAGE_DATA> {
                /**
                 * 分页 缓存 包括下拉刷新，上拉加载更多 请求成功后的逻辑
                 * */
                override fun onSuccess(
                    model: BaseModel<*, PAGE_DATA>,
                    resultData: PAGE_DATA?,
                    pageInfo: ResultPageInfo?
                ) {
                    showPageSuccess()
                    data.value = resultData
                    finishSmartRefreshStatus()
                    onPageSuccess(model, resultData, pageInfo)
                }

                override fun onFail(
                    model: BaseModel<*, PAGE_DATA>,
                    throwable: Throwable,
                    pageInfo: ResultPageInfo?
                ) {
                    onPageFail(model, throwable, pageInfo)
//                this@BasePageViewModel.onFail(model as BaseModel<*, Any>, throwable, pageInfo)
                    finishSmartRefreshStatus()
                    if (model.isPaging()) {
                        if (pageInfo?.isFirstPage == true) {
                            showPageFail(throwable)
                        } else {
                            showPageMoreFail()
                        }
                    } else {
                        showPageFail(throwable)
                    }
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
     * 没有更多数据
     * */
    protected open fun showPageNoMoreData() {
        enableLoadMore.postValue(false)
        enableAutoLoadMore.postValue(false)
    }

    /**
     * 有更多数据
     * */
    protected open fun showPageHasMoreData() {
        enableLoadMore.postValue(true)
        enableAutoLoadMore.postValue(true)
    }

    /**
     * 更多  加载失败
     * */
    protected open fun showPageMoreFail() {
        showShortToast("更多数据加载失败")
    }

    /**
     * 获取缓存数据并加载
     */
    override fun loadSirReload() {
        createAndRegisterPageModel()
        mPageModel!!.getCachedDataAndLoad()
    }

    /**
     * 刷新
     */
    override fun refresh() {
        createAndRegisterPageModel()
        mPageModel!!.refresh()
    }

    /**
     * 加载下一页
     */
    override fun loadNextPage() {
        createAndRegisterPageModel()
        mPageModel!!.loadNextPage()
    }

    override fun onCleared() {
        super.onCleared()
        mPageModel?.onClear()
        mOtherModels?.forEach {
            it.onClear()
        }
    }

    fun getPageModel(): PAGE_MODEL {
        createAndRegisterPageModel()
        return mPageModel!!
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected open fun onResume() {
        checkPageStatusLoadPage()
    }

    /**
     * 留给子类操作页面的回调  start
     * */
    protected open fun onPageSuccess(
        model: BaseModel<*, PAGE_DATA>,
        resultData: PAGE_DATA?,
        pageInfo: ResultPageInfo?
    ) {
        hideDialog()
    }

    protected open fun onPageFail(
        model: BaseModel<*, PAGE_DATA>,
        message: Throwable,
        pageInfo: ResultPageInfo?
    ) {
        hideDialog()
        message.message?.let {
            showShortToast(it)
        }
    }
    /**
     * 留给子类操作页面的回调  end
     * */
    /**
     * 通用的回调结果处理 start
     * */
    override fun onSuccess(model: BaseModel<*, Any?>, resultData: Any?, pageInfo: ResultPageInfo?) {
        hideDialog()
    }

    override fun onFail(model: BaseModel<*, Any?>, message: Throwable, pageInfo: ResultPageInfo?) {
        hideDialog()
        if (message.message.isNullOrEmpty().not()) {
            showShortToast(message.message!!)
        }
    }

    /**
     * 通用的回调结果处理 end
     * */
    open fun createRecyclerBottomView(parent: ViewGroup, vm: ViewModel): View? {
        return null
    }
}