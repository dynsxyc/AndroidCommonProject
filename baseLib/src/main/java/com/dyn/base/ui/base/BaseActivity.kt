package com.dyn.base.ui.base

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
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
import com.dyn.base.utils.BaseConstant.LOG_TAG_PERMISSIONS_ACTIVITY
import com.dyn.base.utils.BaseConstant.PERMISSION_ACTIVITY_RC_CODE
import com.gyf.immersionbar.ktx.immersionBar
import com.orhanobut.logger.Logger
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import com.vmadalin.easypermissions.dialogs.DEFAULT_SETTINGS_REQ_CODE
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VM : BaseViewModel> : DataBindingActivity(), ICustomViewActionListener,
    EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks,PermissionProxyClient {
    val mShardViewModel by lazy {
        getAppViewModelProvider().get(SharedViewModel::class.java)
    }

    private val progressLoading by lazy {
        ProgressLoading(this)
    }
    val mViewModel by lazy {
        val vmClazz = (this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
        getActivityViewModel(vmClazz)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.mCustomViewActionListener = this
        lifecycle.addObserver(mViewModel)
        mViewModel.mLifeCycle.value = this
        initDialog()
        if (immersionBarEnabled()){
            initImmersionBar()
        }
    }
    protected open fun initImmersionBar() {
        immersionBar {
            autoDarkModeEnable(true, 0.5f)
            statusBarDarkFont(true)
            keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                    or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
            )
            transparentStatusBar()
        }
    }
    /**
     * 是否可以实现沉浸式，当为true的时候才可以执行initImmersionBar方法
     * Immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected open fun immersionBarEnabled(): Boolean {
        return false
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
    /**权限相关  start */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DEFAULT_SETTINGS_REQ_CODE) {
//            val yes = getString(R.string.yes)
//            val no = getString(R.string.no)
//
//            // Do something after user returned from app settings screen, like showing a Toast.
//            Toast.makeText(
//                this,
//                getString(
//                    R.string.returned_from_app_settings_to_activity,
//                    if (hasCameraPermission()) yes else no,
//                    if (hasLocationAndContactsPermissions()) yes else no,
//                    if (hasSmsPermission()) yes else no,
//                    if (hasStoragePermission()) yes else no
//                ),
//                Toast.LENGTH_LONG
//            ).show()
            Logger.i("$LOG_TAG_PERMISSIONS_ACTIVITY activity onActivityResult")
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Logger.i("$LOG_TAG_PERMISSIONS_ACTIVITY activity   onRequestPermissionsResult")
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        Logger.i("$LOG_TAG_PERMISSIONS_ACTIVITY activity onPermissionsDenied")
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(this).build().show()
        }
        permissionResultListener?.onPermissionsDenied(requestCode, perms)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Logger.i("$LOG_TAG_PERMISSIONS_ACTIVITY activity onPermissionsGranted")
        permissionResultListener?.onPermissionsGranted(requestCode, perms)
    }

    override fun onRationaleAccepted(requestCode: Int) {
        Logger.i("$LOG_TAG_PERMISSIONS_ACTIVITY activity onRationaleAccepted")
        permissionResultListener?.onRationaleAccepted(requestCode)
    }

    override fun onRationaleDenied(requestCode: Int) {
        Logger.i("$LOG_TAG_PERMISSIONS_ACTIVITY activity onRationaleDenied")
        permissionResultListener?.onRationaleDenied(requestCode)

    }

    private var mNexAction: OnRequestPermissionListener? = null
    private var mPermissions: Array<out String>? = null
    private var mTips = "当前App需要请求权限操作"

    @AfterPermissionGranted(PERMISSION_ACTIVITY_RC_CODE)
    private fun requestPermission() {
        Logger.i("$LOG_TAG_PERMISSIONS_ACTIVITY activity requestPermission")
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

    private fun requestPermissionAndInvokeAction(
        tips: String,
        vararg params: String,
        action: OnRequestPermissionListener?
    ) {
        mNexAction = action
        mPermissions = params
        mTips = tips
        requestPermission()
    }

    override fun requestPermission(
        tips: String,
        vararg params: String,
        action: OnRequestPermissionListener?
    ) {
        requestPermissionAndInvokeAction(tips, params=params, action)
    }
    private var permissionResultListener:OnPermissionResultListener? = null
    override fun registerPermissionResult(permissionResultListener: OnPermissionResultListener) {
        this.permissionResultListener = permissionResultListener
    }

    private fun hasPermission(vararg args: String): Boolean {
        return EasyPermissions.hasPermissions(this, *args)
    }
    /**权限相关  end */


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