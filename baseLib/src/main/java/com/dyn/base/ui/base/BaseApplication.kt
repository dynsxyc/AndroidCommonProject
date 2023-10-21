package com.dyn.base.ui.base

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.StrictMode
import android.webkit.WebView
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.multidex.MultiDex
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.blankj.utilcode.util.ProcessUtils
import com.dyn.base.BuildConfig
import com.dyn.base.loadsir.CustomCallback
import com.dyn.base.loadsir.EmptyCallback
import com.dyn.base.loadsir.ErrorCallback
import com.dyn.base.loadsir.LoadingCallback
import com.dyn.base.location.AndroidLocationUtils
import com.dyn.base.ui.weight.CustomToastView
import com.kingja.loadsir.core.LoadSir
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout


open class BaseApplication : Application(), ViewModelStoreOwner {
    private var mViewModelStore: ViewModelStore =ViewModelStore()

    companion object {
        lateinit var instance: BaseApplication
    }

    object AppContext : ContextWrapper(instance)

    open fun createToastView(context: Context): CustomToastView {
        return  CustomToastView(context)
    }
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        init()
    }

    protected open fun init() {
        LoadSir.beginBuilder()
            .addCallback(CustomCallback())
            .addCallback(LoadingCallback())
            .addCallback(ErrorCallback())
            .addCallback(EmptyCallback())
//                .setDefaultCallback(LoadingCallback::class.java) 自己手动改状态 不加默认状态
            .commit()
//        openStrictMode()
        initSmartRefreshLayout()
        initLogger()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val processName = ProcessUtils.getCurrentProcessName()
            val packageName = this.packageName
            if (packageName != processName) {
                WebView.setDataDirectorySuffix(processName)
            }
        }
        AndroidLocationUtils.init(this)
//        异常页加载
        CaocConfig.Builder.create()
//            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
//            .enabled(false) //default: true
//            .showErrorDetails(false) //default: true
//            .showRestartButton(false) //default: true
//            .logErrorOnRestart(false) //default: true
//            .trackActivities(true) //default: false
            .minTimeBetweenCrashesMs(2000) //default: 3000
//            .errorDrawable(R.drawable.ic_custom_drawable) //default: bug image
//            .restartActivity(YourCustomActivity::class.java) //default: null (your app's launch activity)
//            .errorActivity(YourCustomErrorActivity::class.java) //default: null (default error activity)
//            .eventListener(YourCustomEventListener()) //default: null
            .apply()
    }



    private fun initLogger() {
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }

    /**
     * debug模式下开启严苛模式,校验代码中是否存在违规的错误
     * 例如：UI访问网络 UI操作 IO等
     * */
    private fun openStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()//违规打印日志
//                    .penaltyDeath()//违规崩溃
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectAll()
//                    .penaltyDeath()
                    .penaltyLog()
                    .build()
            )
        }
    }

    private fun initSmartRefreshLayout() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            ClassicsHeader(context).setDrawableSize(20f)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator() { context, _ ->
            ClassicsFooter(context).setDrawableSize(20f)
        }
    }

    override val viewModelStore: ViewModelStore
        get() = mViewModelStore

}