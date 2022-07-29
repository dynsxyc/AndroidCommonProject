package com.dyn.webview

import android.os.Bundle
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ScreenUtils
import com.dyn.base.mvvm.viewmodel.BaseViewModel
import com.dyn.base.ui.base.BaseActivity
import com.dyn.base.ui.databinding.DataBindingConfig
import com.dyn.webview.utils.WebConstants
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk

class WebViewActivity : BaseActivity<BaseViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_webview
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val loadUrl = intent.getStringExtra(WebConstants.INTENT_TAG_URL)
//        val interfaceName = intent.getStringExtra(WebConstants.INTENT_INTERFACE_NAME)
//        val title = intent.getStringExtra(WebConstants.INTENT_TAG_TITLE)
//        val isShowActionBar = intent.getBooleanExtra(WebConstants.WEB_IS_SHOW_ACTION_BAR, true)
//        val isSyncCookie = intent.getBooleanExtra(WebConstants.WEB_IS_SYNC_COOKIE, false)
//        val header = intent.getSerializableExtra(WebConstants.INTENT_TAG_HEADERS)?.let {
//            it as HashMap<String, String>
//        }

        QbSdk.initX5Environment(this,  object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {
                // 内核初始化完成，可能为系统内核，也可能为系统内核
            }

            /**
             * 预初始化结束
             * 由于X5内核体积较大，需要依赖网络动态下发，所以当内核不存在的时候，默认会回调false，此时将会使用系统内核代替
             * @param isX5 是否使用X5内核
             */
            @Override
            override fun onViewInitFinished(isX5: Boolean) {

            }
        })
        QbSdk.setDownloadWithoutWifi(true)
        // 在调用TBS初始化、创建WebView之前进行如下配置
        val map = mutableMapOf(
            TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER to  true,
            TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE to true)
        QbSdk.initTbsSettings(map.toMap())


//        val bundle = Bundle()
//        bundle.putParcelable("args",WebViewArgs("http://121.28.104.30:8390/aidl.html","webview","打卡", isSyncCookie = false, isShowActionBar = true,null))
////        bundle.putParcelable("args",WebViewArgs("http://121.28.104.30:8398/#/home","webview","", isSyncCookie = false, isShowActionBar = false,null))
//        val findNavController = Navigation.findNavController(this, R.id.web_host_fragment)
//        findNavController.setGraph(R.navigation.web_navigation,bundle)
    }


}