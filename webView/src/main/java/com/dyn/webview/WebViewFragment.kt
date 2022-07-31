package com.dyn.webview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.dyn.base.mvvm.view.BaseFragment
import com.dyn.base.ui.databinding.DataBindingConfig
import com.dyn.webview.utils.WebConstants
import com.dyn.webview.utils.WebConstants.REQUEST_CODE_LOLIPOP
import com.tencent.smtt.sdk.CookieManager
import com.tencent.smtt.sdk.ValueCallback
import com.tencent.smtt.sdk.WebView

class WebViewFragment : BaseFragment<WebViewModelView>(), WebCallback {
    private var mIsError = false //判断页面是否加载成功

    //    private val fragmentArgs: WebViewFragmentArgs by navArgs()
    companion object {
        fun newInstance(
            url: String,
            title: String,
            isSyncCookie: Boolean = false,
            isShowActionBar: Boolean = true,
            header: HashMap<String, String>? = null
        ): WebViewFragment {
            val fragment = WebViewFragment()
            val bundle = Bundle()
            bundle.putBoolean(WebConstants.WEB_IS_SHOW_ACTION_BAR, isShowActionBar)
            bundle.putBoolean(WebConstants.WEB_IS_SYNC_COOKIE, isSyncCookie)
            bundle.putString(WebConstants.INTENT_TAG_URL, url)
            bundle.putString(WebConstants.INTENT_TAG_TITLE, title)
            bundle.putSerializable(WebConstants.INTENT_TAG_HEADERS, header)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onLazyAfterView() {
        super.onLazyAfterView()
        arguments?.getParcelable<WebViewArgs>("args")?.let { b ->
//        val b = WebViewArgs("http://121.28.104.30:8390/aidl.html","webview","打卡", isSyncCookie = false, isShowActionBar = true,null)
//        val b = WebViewArgs(
//            "http://121.28.104.30:8398/#/home",
//            "webview",
//            "",
//            isSyncCookie = false,
//            isShowActionBar = false,
//            null
//        )
            mViewModel.webUrl.value = b.loadUrl
            mViewModel.interfaceName.value = b.interfaceName
            mViewModel.mCommonHeaderModel.title.set(b.title)
            mViewModel.mCommonHeaderModel.finishStyle.drawableStart.set(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_nav_close
                )
            )
//        mViewModel.isShowActionBar.value = b.isShowActionBar
            val isSyncCookie = b.isSyncCookie
            val header = b.header
            header?.let {
                if (isSyncCookie) {
                    syncCookie(mViewModel.webUrl.value, it)
                }
            }
            mViewModel.header.value = header
//        }
            mViewModel.webCallback = this
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        CommandDispatcher.instance.initAidlConnect(requireContext().applicationContext)
        mViewModel.canGoBack.observe(viewLifecycleOwner, Observer {
            if (it.not()) {//直接返回
                onClickHeaderFinish()
            } else {
                mViewModel.mCommonHeaderModel.hasFinish.set(true)
            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_webview
    }

    /**
     * 判断是否是web回退
     * */
    val canGoBack: (Boolean) -> Unit = {
        if (it.not()) {//直接返回
            onClickHeaderFinish()
        } else {
            mViewModel.mCommonHeaderModel.hasFinish.set(true)
        }
    }

    override fun onClickHeaderBack() {
        mViewModel.canGoBackStatus.value = true
    }

    override fun onClickHeaderFinish() {
        if (pop().not() && requireActivity() is WebViewActivity) {
            requireActivity().finish()
        }
    }

    override fun onReceivedTitle(title: String) {
        mViewModel.mCommonHeaderModel.title.set(title)
    }

    override fun onPageStarted(url: String?) {
        mViewModel.finishRefresh.value?.let {
            if (it.not()) {
                mViewModel.showPageLoading()
            }
        }

    }

    override fun onPageFinished(url: String?) {
        if (mIsError) {
            mViewModel.showPageFail()
            mViewModel.enableRefresh.value = true
        } else {
            mViewModel.showPageSuccess()
        }
        mViewModel.finishRefresh.postValue(true)
        mIsError = false
    }

    override fun onPageError() {
        mIsError = true
    }

    override fun requestBackPressed(): Boolean {
        return true
    }

    override fun onBackPressed(): Boolean {
        mViewModel.canGoBackStatus.value = true
        return false
    }

    /**
     * 把我的生命周期分发给webView start
     * */
    override fun onResume() {
        super.onResume()
        mViewModel.dispatchEvent.value = DispatchWebEvent.ONRESUME
    }

    override fun onPause() {
        super.onPause()
        mViewModel.dispatchEvent.value = DispatchWebEvent.ONPAUSE
    }

    override fun onStop() {
        super.onStop()
        mViewModel.dispatchEvent.value = DispatchWebEvent.ONSTOP
    }

    override fun onDestroyView() {
        mViewModel.dispatchEvent.value = DispatchWebEvent.ONDESTROYVIEW
        super.onDestroyView()
    }

    /**
     * 把我的生命周期分发给webView end
     * */


    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    private var mCameraPhotoPath: String? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_LOLIPOP -> {
                var results: Array<Uri>? = null
                // Check that the response is a good one
                if (resultCode == Activity.RESULT_OK) {
                    if (data == null) {
                        // If there is not data, then we may have taken a photo
                        if (mCameraPhotoPath != null) {
                            results = arrayOf(Uri.parse(mCameraPhotoPath))
                        }
                    } else {
                        val dataString = data.dataString
                        if (dataString != null) {
                            results = arrayOf(Uri.parse(dataString))
                        }
                    }
                }
                mFilePathCallback?.onReceiveValue(results)
                mFilePathCallback = null
            }
        }
    }

    /**
     * web选择文件
     * */
    override fun onShowFileChooser(
        cameraIntent: Intent?,
        filePathCallback: ValueCallback<Array<Uri>>?
    ) {
        //整个弹出框为:相机、相册、文件管理
        //如果安装了其他的相机、文件管理程序，也有可能会弹出
        //selectionIntent(相册、文件管理)
        //Intent selectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        //selectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        //selectionIntent.setType("image/*");
        mFilePathCallback = filePathCallback
        //------------------------------------
        //如果通过下面的方式，则弹出的选择框有:相机、相册(Android9.0,Android8.0)
        //如果是小米Android6.0系统上，依然是：相机、相册、文件管理
        //如果安装了其他的相机(百度魔拍)、文件管理程序(ES文件管理器)，也有可能会弹出
        val selectionIntent = Intent(Intent.ACTION_PICK, null)
        selectionIntent.type = "image/*"
        //------------------------------------
//        val intentArray: Array<Intent?>
//        intentArray = cameraIntent?.let { arrayOf(it) } ?: arrayOfNulls(0)
        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
        chooserIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.file_chooser))
        chooserIntent.putExtra(Intent.EXTRA_INTENT, selectionIntent)
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
        startActivityForResult(chooserIntent, REQUEST_CODE_LOLIPOP)
    }


    /**
     * js 调用native 开始的地方
     * */
    override fun exec(
        context: Context,
        commandLevel: Int,
        cmd: String,
        params: String?,
        webView: WebView
    ) {
        CommandDispatcher.instance.exec(
            context,
            commandLevel,
            cmd,
            params,
            webView,
            getCommandDispatcher()
        )
    }

    private fun getCommandDispatcher(): CommandDispatcher.DispatcherCallBack? {
        return object : CommandDispatcher.DispatcherCallBack {
            override fun preHandleBeforeCallback(
                responseCode: Int,
                actionName: String?,
                response: String?
            ): Boolean {
                Log.d(
                    "dyn",
                    "preHandleBeforeCallback  responseCode->$responseCode  actionName->$actionName  response->$response"
                )
                return true
            }

        }
    }

    /**
     * 将cookie同步到WebView
     *
     * @param url    WebView要加载的url
     * @return true 同步cookie成功，false同步cookie失败
     * @Author JPH
     */
    private fun syncCookie(url: String?, map: Map<String, String?>): Boolean {
        val cookieManager = CookieManager.getInstance()
        for (key in map.keys) {
            cookieManager.setCookie(url, key + "=" + map[key])
        }
        val newCookie = cookieManager.getCookie(url)
        return !TextUtils.isEmpty(newCookie)
    }

}