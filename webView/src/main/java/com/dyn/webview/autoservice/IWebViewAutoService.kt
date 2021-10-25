package com.dyn.webview.autoservice

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.NavGraph

interface IWebViewAutoService {
    fun startWebActivity(context: Context,interfaceName:String = "webview",
                         url: String, title: String = "",
                         isShowActionBar: Boolean = true,
                         header: HashMap<String, String>? = null)
    /**
     * @param navController 通过原有路由控制器 创建当前模块路由控制表
     * @return 返回当前模块路由
     * */
    fun getNavGraph(navController:NavController):NavGraph
    /**
     * webFragment 节点对应的navigation Id
     * */
    fun getWebStartId():Int
}