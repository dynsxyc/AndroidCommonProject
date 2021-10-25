package com.dyn.webview.autoservice

import android.content.Context
import android.content.Intent
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import com.dyn.webview.R
import com.dyn.webview.WebViewActivity
import com.dyn.webview.utils.WebConstants
import com.google.auto.service.AutoService

@AutoService(value = [IWebViewAutoService::class])
class WebViewAutoServiceImpl : IWebViewAutoService {
    override fun startWebActivity(context: Context,interFaceName:String, url: String, title: String, isShowActionBar: Boolean, header: HashMap<String, String>?) {
        val intent = Intent(context, WebViewActivity::class.java)
        intent.putExtra(WebConstants.INTENT_TAG_TITLE, title)
        intent.putExtra(WebConstants.INTENT_INTERFACE_NAME, interFaceName)
        intent.putExtra(WebConstants.INTENT_TAG_URL, url)
        intent.putExtra(WebConstants.WEB_IS_SHOW_ACTION_BAR, isShowActionBar)
        intent.putExtra(WebConstants.INTENT_TAG_HEADERS, header)
        context.startActivity(intent)
    }

    override fun getNavGraph(navController: NavController): NavGraph {
        return navController.navInflater.inflate(R.navigation.web_navigation)
    }

    override fun getWebStartId(): Int {
        return R.id.web_fragment
    }


}