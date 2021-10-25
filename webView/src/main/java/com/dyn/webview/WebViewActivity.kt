package com.dyn.webview

import android.os.Bundle
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ScreenUtils
import com.dyn.base.mvvm.viewmodel.BaseViewModel
import com.dyn.base.ui.base.BaseActivity
import com.dyn.base.ui.databinding.DataBindingConfig
import com.dyn.webview.utils.WebConstants

class WebViewActivity : BaseActivity<BaseViewModel>() {

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig( BR.vm, mViewModel)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_webview
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ScreenUtils.setNonFullScreen(this)
        val loadUrl = intent.getStringExtra(WebConstants.INTENT_TAG_URL)
        val interfaceName = intent.getStringExtra(WebConstants.INTENT_INTERFACE_NAME)
        val title = intent.getStringExtra(WebConstants.INTENT_TAG_TITLE)
        val isShowActionBar = intent.getBooleanExtra(WebConstants.WEB_IS_SHOW_ACTION_BAR, true)
        val isSyncCookie = intent.getBooleanExtra(WebConstants.WEB_IS_SYNC_COOKIE, false)
        val header = intent.getSerializableExtra(WebConstants.INTENT_TAG_HEADERS)?.let {
            it as HashMap<String, String>
        }
        val bundle = Bundle()
        bundle.putParcelable("args",WebViewArgs(loadUrl,interfaceName,title,isSyncCookie,isShowActionBar,header))
        val findNavController = Navigation.findNavController(this, R.id.web_host_fragment)
        findNavController.setGraph(R.navigation.web_navigation,bundle)
    }


}