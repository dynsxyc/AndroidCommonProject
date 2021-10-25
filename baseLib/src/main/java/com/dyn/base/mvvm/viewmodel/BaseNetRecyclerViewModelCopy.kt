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
 * loadSir + smartRefreshLayout + recyclerView
 * */
abstract class BaseNetRecyclerViewModelCopy<PAGE_MODEL : BaseModel<*, MutableList<PAGE_DATA?>>, PAGE_DATA> : BaseLoadSirAndRefreshViewModel(), IBaseModelListener<Any?> {


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
     * 分页数据
     * */
    var dataList: MutableLiveData<MutableList<*>> = MutableLiveData()
    var data: MutableLiveData<PAGE_DATA?> = MutableLiveData()
    var mRemoveItemPosition = MutableLiveData(-1)
    var mRemoveItemData = MutableLiveData<Any?>(null)
    private var mPageModel: PAGE_MODEL? = null

    /**
     * 框架加载页面数据对应的model
     * */
    abstract fun createPageModel(): PAGE_MODEL

    private fun createAndRegisterPageModel(){
        if (mPageModel == null){
            mPageModel = createPageModel()
            mPageModel?.registerListener(object : IBaseModelListener<MutableList<PAGE_DATA?>> {
                /**
                 * 分页 缓存 包括下拉刷新，上拉加载更多 请求成功后的逻辑
                 * */
                override fun onSuccess(
                    model: BaseModel<*, MutableList<PAGE_DATA?>>,
                    resultData: MutableList<PAGE_DATA?>?,
                    pageInfo: ResultPageInfo?
                ) {
                    if (model.isPaging()) {
                        //是分页类型
                        pageInfo?.let {
                            if (it.isFirstPage) {
                                //第一页
                                if (it.isEmpty) {
                                    dataList.postValue(null)
                                    showPageEmpty()
                                } else {
                                    showPageSuccess()
                                    dataList.postValue(resultData)

                                }
                            } else {
                                //加载更多
                                if (it.isEmpty.not() && resultData is MutableList && resultData.size>0) {
                                    //更多不为空
                                    dataList.value?.let { rData->
                                        rData as MutableList<PAGE_DATA?>
                                        rData.addAll(resultData!!)
                                    }
                                    dataList.postValue(dataList.value)
                                }

                            }
                            if (it.hasMore.not()){
                                //没有更多
                                showPageNoMoreData()
                            }else{
                                showPageHasMoreData()
                            }
                        }

//                    if (pageInfo?.isEmpty == true) {
//                        if (pageInfo?.isFirstPage) {
//                            showPageEmpty()
//                        } else {
//                            showPageNoMoreSuccess()
//                        }
//                    } else {
//                        if (pageInfo?.isFirstPage == true) {
//                            dataList.postValue(resultData)
//                        } else {
//                            dataList.value?.let {
//                                it as MutableList<PAGE_DATA?>
//                                it.addAll(resultData!!)
//                            }
//                            dataList.postValue(dataList.value)
//                        }
//                        showPageSuccess()
//                    }
                    } else {
                        //不是分页类型
                        showPageSuccess()
                        if (resultData?.size ?: 0 > 0) {
                            data.postValue(resultData!![0])
                        }
                    }
                    finishSmartRefreshStatus()
                    onPageSuccess(model, resultData, pageInfo)
                }

                override fun onFail(
                    model: BaseModel<*, MutableList<PAGE_DATA?>>,
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
    fun getCurrentPageModel():PAGE_MODEL{
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

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected open fun onResume() {
        checkPageStatusLoadPage()
    }

    /**
     * 留给子类操作页面的回调  start
     * */
    protected open fun onPageSuccess(
        model: BaseModel<*, MutableList<PAGE_DATA?>>,
        resultData: MutableList<PAGE_DATA?>?,
        pageInfo: ResultPageInfo?
    ) {
        hideDialog()
    }

    protected open fun onPageFail(
        model: BaseModel<*, MutableList<PAGE_DATA?>>,
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
        if (message.message.isNullOrEmpty().not()){
            showShortToast(message.message!!)
        }
    }
    /**
     * 通用的回调结果处理 end
     * */
    open fun createRecyclerBottomView(parent: ViewGroup,vm: ViewModel):View?{
        return null
    }
}