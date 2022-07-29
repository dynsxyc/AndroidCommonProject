package com.dyn.webview

import androidx.lifecycle.MutableLiveData
import com.dyn.base.mvvm.viewmodel.BaseLoadSirAndRefreshViewModel
import com.dyn.webview.utils.OnBackListener

class WebViewModelView : BaseLoadSirAndRefreshViewModel() {
    var webUrl = MutableLiveData<String>()
    var interfaceName = MutableLiveData<String>()
    var header = MutableLiveData<HashMap<String,String>?>()
    var isReLoad = MutableLiveData<Boolean>() //重新加载网页
    var canGoBackStatus = MutableLiveData<Boolean>()//通过改变这个状态动态获取webView  是否是回退
    var canGoBack = MutableLiveData<Boolean>()
    val backListener = object : OnBackListener {
        override fun onBack(isGoBack: Boolean) {
            canGoBack.postValue(isGoBack)
        }
    }
    //webView 回调监听
    lateinit var webCallback:WebCallback
    //分发给H5 的页面生命周期回调
    var dispatchEvent = MutableLiveData<DispatchWebEvent?>()
    init {
        isReLoad.value = false
        canGoBackStatus.value = false
    }

    override fun loadSirReload() {
        showPageLoading()
        isReLoad.value = true
    }

    override fun refresh() {
        isReLoad.value = true
    }

    override fun loadNextPage() {

    }


}