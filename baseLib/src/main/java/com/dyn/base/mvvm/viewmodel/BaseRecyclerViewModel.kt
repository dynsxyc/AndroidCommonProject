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

abstract class BaseRecyclerViewModel<ITEM> : BaseLoadSirAndRefreshViewModel(), IBaseModelListener<Any?> {


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
    var data: MutableLiveData<ITEM?> = MutableLiveData()
    var mRemoveItemPosition = MutableLiveData(-1)
    var mRemoveItemData = MutableLiveData<Any?>(null)


    /**
     * 更多  加载失败
     * */
    protected open fun showPageMoreFail() {
        showShortToast("更多数据加载失败")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected open fun onResume() {
        checkPageStatusLoadPage()
    }

    /**
     * 留给子类操作页面的回调  start
     * */
    protected open fun onPageSuccess(
        model: BaseModel<*, MutableList<ITEM>>,
        resultData: MutableList<ITEM>?,
        pageInfo: ResultPageInfo?
    ) {
        hideDialog()
    }

    protected open fun onPageFail(
        model: BaseModel<*, MutableList<ITEM>>,
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