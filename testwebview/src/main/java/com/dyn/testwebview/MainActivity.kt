package com.dyn.testwebview

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.NestedScrollView
import com.blankj.utilcode.util.ConvertUtils.dp2px
import com.dyn.base.ui.base.BaseActivity
import com.dyn.base.mvvm.viewmodel.BaseViewModel
import com.dyn.base.ui.databinding.DataBindingConfig
import com.dyn.base.utils.ServiceLoaderUtils
import com.dyn.webview.autoservice.IWebViewAutoService
import com.dyn.webview.command.base.Command
import com.dyn.webview.utils.WebConstants
import java.util.*

class MainActivity : BaseActivity<MainActivity.MainActivityViewModel>() {
    class MainActivityViewModel: BaseViewModel(){
        val scrollHeight = dp2px(300f)
        init {
            setHeaderTitle("WebView测试")
            setHeaderOffset(0f)
        }
        val onScrollChangeListener = NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val result = when {
                scrollY<=0 -> {
                    0f
                }
                scrollY in 1..scrollHeight -> {
                    scrollY.toFloat()/scrollHeight
                }
                else -> {
                    1f
                }
            }
            setHeaderOffset(result)
        }
    }
    private val mWebService by lazy {
        ServiceLoaderUtils.loadFirstService(IWebViewAutoService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val services = ServiceLoaderUtils.loadServices(Command::class.java)
//        while (services.hasNext()){
//            val next = services.next()
//            Log.i("dyn","注册命令->${next.name()}")
//        }
        findViewById<View>(R.id.openWeb1).setOnClickListener {
            mWebService?.startWebActivity(this, url = "https://xw.qq.com/?f=qqcom", title = "腾讯网")
        }

        findViewById<View>(R.id.openWeb2).setOnClickListener {
            mWebService?.startWebActivity(this@MainActivity, title = "AIDL测试", url = WebConstants.CONTENT_SCHEME + "aidl.html")
        }

        findViewById<View>(R.id.openWeb3).setOnClickListener { // for account level
            val accountInfo = HashMap<String, String>()
            accountInfo["username"] = "TestAccount"
            accountInfo["access_token"] = "880fed4ca2aabd20ae9a5dd774711de2"
            accountInfo["phone"] = "+8613989898898"
            mWebService?.startWebActivity(this@MainActivity, title = "百度", url = "http://www.baidu.com",header = accountInfo)
        }
        findViewById<View>(R.id.alert_issue).setOnClickListener {
//            mWebService?.startWebActivity(this@MainActivity, title = "Alert问题",url = WebConstants.CONTENT_SCHEME + "alert_issue.html")
            mWebService?.startWebActivity(this@MainActivity, title = "bridge测试", interfaceName = "jsBridgeFn", url = WebConstants.CONTENT_SCHEME + "demo.html")
        }

        findViewById<View>(R.id.auto_zoom).setOnClickListener {
            mWebService?.startWebActivity(this@MainActivity, title = "自适应屏幕问题",url = "http://www.customs.gov.cn/customs/302249/302266/302267/2491213/index.html")
        }

        findViewById<View>(R.id.webview_pre_init).setOnClickListener {
            mWebService?.startWebActivity(this@MainActivity, title = "腾讯网",url = "https://xw.qq.com/?f=qqcom")
        }

        findViewById<View>(R.id.webview_pre_baidu).setOnClickListener {
            mWebService?.startWebActivity(this@MainActivity, title = "百度",url = "https://m.baidu.com")
        }


        findViewById<View>(R.id.file_upload).setOnClickListener {

            Log.i("dyn","${TestPreference.string}")
            Log.i("dyn","${TestPreference.intValue}")
            Log.i("dyn","${TestPreference.long}")
            Log.i("dyn","${TestPreference.boolean}")
            Log.i("dyn","${TestPreference.float}")
            Log.i("dyn","${TestPreference.spData}")
            TestPreference.string = "1"
            TestPreference.intValue = 2
            TestPreference.long = 3L
            TestPreference.float = 4f
            TestPreference.boolean = false
            TestPreference.spData = TestData("333",5,6)
            Log.i("dyn","${TestPreference.string}")
            Log.i("dyn","${TestPreference.intValue}")
            Log.i("dyn","${TestPreference.long}")
            Log.i("dyn","${TestPreference.boolean}")
            Log.i("dyn","${TestPreference.float}")
            Log.i("dyn","${TestPreference.spData}")
//            mWebService?.startWebActivity(this@MainActivity, title = "文件上传", url ="file:///android_asset/www/index.html")
        }

//        NavigationBar.Builder(this,R.layout.ui_navigation_bar,findViewById(R.id.contentLl))
//                .setText(R.id.back_tv,"我就是返回")
//                .setOnclickListener(R.id.back_tv){
//                    ToastUtils.showShort("你点我干什么？")
//                }
//                .create()

    }


    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(BR.vm,mViewModel)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }
}
