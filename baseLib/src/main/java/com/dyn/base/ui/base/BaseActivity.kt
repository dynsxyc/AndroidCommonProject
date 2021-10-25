package com.dyn.base.ui.base

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.dyn.base.BR
import com.dyn.base.R
import com.dyn.base.bus.SharedViewModel
import com.dyn.base.customview.BaseCustomModel
import com.dyn.base.customview.ICustomViewActionListener
import com.dyn.base.mvvm.viewmodel.BaseViewModel
import com.dyn.base.ui.OnRequestPermissionListener
import com.dyn.base.ui.databinding.DataBindingActivity
import com.dyn.base.ui.databinding.DataBindingConfig
import com.dyn.base.ui.weight.ProgressLoading
import com.dyn.base.ui.weight.header.CommonHeaderModel
import com.dyn.base.utils.BaseActionConstant
import com.gyf.immersionbar.ktx.immersionBar
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VM : BaseViewModel> : DataBindingActivity(), ICustomViewActionListener,
    EasyPermissions.PermissionCallbacks {
    val mShardViewModel by lazy {
        getAppViewModelProvider().get(SharedViewModel::class.java)
    }

    companion object {
        const val PERMISSION_RC_CODE = 200
        const val PERMISSION_ACTIVITY_RC_CODE = 201
    }

    private val vmClazz =
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
    private val progressLoading by lazy {
        ProgressLoading.createNew(this)
    }
    val mViewModel by lazy {
        getActivityViewModel(vmClazz)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.mCustomViewActionListener = this
        initDialog()
        immersionBar {
            fullScreen(false)
            autoDarkModeEnable(true, 0.5f)
            statusBarColor(R.color.transparent, 0.6f)
        }
    }
    /**
     * 初始化弹框相应监听
     * */
    private fun initDialog() {
        mViewModel.mLoadingDialogStatus.observe(this, Observer {
            when (it) {
                DialogStatus.LOADING_DIALOG_STATUS_SHOW_CANCEL -> {
                    progressLoading.showCancelableLoading()
                }
                DialogStatus.LOADING_DIALOG_STATUS_SHOW_UNCANCEL -> {
                    progressLoading.showUnCancelableLoading()
                }
                DialogStatus.LOADING_DIALOG_STATUS_HIDE -> {
                    progressLoading.hideLoading()
                }
            }
        })
    }
    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(BR.vm, mViewModel)
    }

    /**
     * 显示一个点击其它区域可以取消的菊花弹框
     * */
    private fun showCancelableLoading() {
        progressLoading.showCancelableLoading()
    }

    override fun onAction(view: View, action: String, viewModel: BaseCustomModel) {
        when (viewModel) {
            is CommonHeaderModel -> {
                when (action) {
                    BaseActionConstant.ACTION_BACK -> {
                        onHeaderBack()
                    }
                    BaseActionConstant.ACTION_FINISH -> {
                        onHeaderFinish()
                    }
                    BaseActionConstant.ACTION_RIGHT -> {
                        onHeaderRight()
                    }
                    BaseActionConstant.ACTION_RIGHT_LAST -> {
                        onHeaderRightLast()
                    }
                }
            }
        }
    }

    protected open fun onHeaderRight() {

    }

    protected open fun onHeaderFinish() {
    }

    protected open fun onHeaderRightLast() {

    }

    protected open fun onHeaderBack() {
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i(BaseActionConstant.LOG_TAG_PERMISSIONS,"baseActivity   onRequestPermissionsResult")
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

        Log.i(BaseActionConstant.LOG_TAG_PERMISSIONS,"baseActivity   onPermissionsDenied")
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms) && (EasyPermissions.hasPermissions(this,*perms.toTypedArray()).not())) {
            AppSettingsDialog.Builder(this).setThemeResId(R.style.PermissionsThemeDialog).build().show()
        }
    }


    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.i(BaseActionConstant.LOG_TAG_PERMISSIONS,"baseActivity   onPermissionsGranted")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            //设置中心请求权限回来了
            requestPermission()
        }
    }

    private var mNexAction: OnRequestPermissionListener? = null
    private var mPermissions: Array<out String>? = null
    private var mTips = "当前App需要请求权限操作"

    @AfterPermissionGranted(PERMISSION_ACTIVITY_RC_CODE)
    private fun requestPermission() {
        mPermissions?.let {
            if (hasPermission(*it)) {
                // Have permission, do the thing!
                mNexAction?.onInvoke()
            } else {
                // Ask for one permission
                EasyPermissions.requestPermissions(
                    this,
                    mTips,
                    PERMISSION_ACTIVITY_RC_CODE, *it
                )
            }
        }
    }

    fun requestPermissionAndInvokeAction(
        tips: String,
        vararg params: String,
        action: OnRequestPermissionListener?
    ) {
        mNexAction = action
        mPermissions = params
        mTips = tips
        requestPermission()
    }

    private fun hasPermission(vararg args: String): Boolean {
        return EasyPermissions.hasPermissions(this, *args)
    }

/*
//打开这段代码  实现APP字体大小固定，不跟随系统字体大小变化而变化
override fun getResources(): Resources {
        val resources = super.getResources()
        val configuration = Configuration()
        configuration.setToDefaults()
//        configuration
//        val context = createConfigurationContext(configuration)
        resources.updateConfiguration(configuration,resources.displayMetrics)
        return resources;

    }*/
}