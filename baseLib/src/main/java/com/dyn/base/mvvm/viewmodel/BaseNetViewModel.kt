package com.dyn.base.mvvm.viewmodel

import com.dyn.base.mvvm.model.BaseModel
import com.dyn.base.mvvm.model.IBaseModelListener
import com.dyn.base.mvvm.model.ResultPageInfo
import java.lang.ref.WeakReference
/**
 * 有网络请求的viewMOdel
 * */
abstract class BaseNetViewModel : BaseViewModel(),IBaseModelListener<Any?> {
    private var models = createDataModels()
    protected abstract fun createDataModels() : MutableList<BaseModel<*,Any?>>?
    init {
        models?.forEach {
            it?.registerListener(this)
        }
    }
    override fun onCleared() {
        super.onCleared()
        models?.forEach {
            it?.onClear()
        }
    }

    override fun onSuccess(model: BaseModel<*, Any?>, resultData: Any?, pageInfo: ResultPageInfo?) {
        hideDialog()
    }

    override fun onFail(model: BaseModel<*, Any?>, message: Throwable, pageInfo: ResultPageInfo?) {
        hideDialog()
        message.message?.let {
            showShortToast(it)
        }
    }
}