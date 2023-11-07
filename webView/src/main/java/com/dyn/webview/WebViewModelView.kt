package com.dyn.webview

import androidx.lifecycle.MutableLiveData
import com.dyn.base.mvvm.viewmodel.BaseLoadSirAndRefreshViewModel
import com.dyn.webview.jsbridge.BridgeHandler
import com.dyn.webview.utils.CallHandlerData
import com.dyn.webview.utils.OnBackListener

class WebViewModelView : BaseLoadSirAndRefreshViewModel() {
    var webUrl = MutableLiveData<String>()
    var interfaceName = MutableLiveData<String>()
    var loadJsStr = MutableLiveData<String>()//原生调用js 代码
    var bridgeHandler = MutableLiveData<BridgeHandler>()
    var callHandlerData = MutableLiveData<CallHandlerData>()
    var header = MutableLiveData<HashMap<String,String>?>()
    var isReLoad = MutableLiveData<Boolean>(false) //重新加载网页
    var canGoBackStatus = MutableLiveData<Boolean>(false)//通过改变这个状态动态获取webView  是否是回退
    var canGoBack = MutableLiveData<Boolean>()
    val backListener = object : OnBackListener {
        override fun onBack(isGoBack: Boolean) {
            canGoBack.postValue(isGoBack)
        }
    }
    //webView 回调监听
//    val webCallback = MutableLiveData<WebCallback>()
    init {
        enableRefresh.value = false
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