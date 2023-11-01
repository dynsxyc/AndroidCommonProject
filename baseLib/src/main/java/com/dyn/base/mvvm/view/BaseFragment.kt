package com.dyn.base.mvvm.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ResourceUtils
import com.dyn.base.BR
import com.dyn.base.R
import com.dyn.base.bus.SharedViewModel
import com.dyn.base.common.customNav
import com.dyn.base.customview.BaseCustomModel
import com.dyn.base.customview.ICustomViewActionListener
import com.dyn.base.mvvm.viewmodel.BaseViewModel
import com.dyn.base.ui.OnRequestPermissionListener
import com.dyn.base.ui.base.BaseImmersionFragment
import com.dyn.base.ui.base.DialogStatus.Companion.LOADING_DIALOG_STATUS_HIDE
import com.dyn.base.ui.base.DialogStatus.Companion.LOADING_DIALOG_STATUS_SHOW_CANCEL
import com.dyn.base.ui.base.DialogStatus.Companion.LOADING_DIALOG_STATUS_SHOW_UNCANCEL
import com.dyn.base.ui.base.DialogStatus.Companion.LOADING_DIALOG_STATUS_SHOW_UNCANCEL_STR
import com.dyn.base.ui.base.OnPermissionResultListener
import com.dyn.base.ui.base.PermissionProxyClient
import com.dyn.base.ui.databinding.DataBindingConfig
import com.dyn.base.ui.weight.CustomToastView
import com.dyn.base.ui.weight.ProgressLoading
import com.dyn.base.utils.BaseActionConstant
import com.dyn.base.utils.BaseConstant.LOG_TAG_PERMISSIONS
import com.gyf.immersionbar.ImmersionBar
import com.orhanobut.logger.Logger
import com.vmadalin.easypermissions.EasyPermissions
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<VM : BaseViewModel> : BaseImmersionFragment(),
    OnPermissionResultListener, ICustomViewActionListener {
    lateinit var mShardViewModel: SharedViewModel
    private val viewModelClazz =
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
    lateinit var mViewModel: VM
    private var mOnBackPressedCallback: OnBackPressedCallback? = null

    private val progressLoading by lazy {
        ProgressLoading(requireContext())
    }
    protected var lifecycleIndex = 0

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(BR.vm, mViewModel)
    }

    /**
     *
     * 如果拦截了 back回退事件  结合requestBackPressed 使用
     * 在onBackPressed 做自定义回退处理
     * @return  false 不调用系统回退
     *          true 调用系统回退
     * */
    protected open fun onBackPressed(): Boolean {
        /**
         * 子类处理的时候注意一个问题
         * 子类如果实现了该方法  如果页面再进行深入切换的时候 会有返回问题  因为子类监听了返回操作 必要时加入如下代码
         *
         * val stack = NavHostFragment.findNavController(this).currentBackStackEntry
         *    if (stack != null && stack.destination.id != R.id.global_search) { //这里的R.id 是当前页ID 如果不是当前页 不拦截返回操作
         *         return true
         *     }
         * */
        Logger.i("-----------------回退监听--------------")
        return true
    }

    /**
     * 是否拦截 back 回退事件  结合 onBackPressed使用
     * @return true 拦截  false 不拦截 默认不拦截
     * */
    protected open fun requestBackPressed(): Boolean {
        return false
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
    }

    var time = 0L
    override fun onAttach(context: Context) {
        super.onAttach(context)
        time = System.currentTimeMillis()
        Logger.d(
            "生命周期%s",
            "Fragment tag:${this.tag + "_" + this.javaClass.simpleName}: onAttach ${++lifecycleIndex}"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d(
            "生命周期%s",
            "Fragment tag:${this.tag + "_" + this.javaClass.simpleName}: onCreate ${++lifecycleIndex}"
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Logger.d(
            "生命周期%s",
            "Fragment tag:${this.tag + "_" + this.javaClass.simpleName}: onCreateView ${++lifecycleIndex}"
        )
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.d(
            "生命周期%s",
            "Fragment tag:${this.tag + "_" + this.javaClass.simpleName}: onViewCreated ${++lifecycleIndex}"
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Logger.d(
            "生命周期%s",
            "Fragment tag:${this.tag + "_" + this.javaClass.simpleName}: onActivityCreated ${++lifecycleIndex}"
        )
    }

    override fun onStart() {
        if (isLazyInitView.not()) {
            isLazyInitView = true
            mViewModel = getFragmentViewModel(viewModelClazz)
            lifecycle.addObserver(mViewModel)
            mShardViewModel = getAppViewModelProvider()[SharedViewModel::class.java]

            if (requestBackPressed()) {
                mOnBackPressedCallback = object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (onBackPressed()) {
                            mOnBackPressedCallback!!.isEnabled = false
                            requireActivity().onBackPressed()
                        }
                    }

                }
                requireActivity().onBackPressedDispatcher
                    .addCallback(viewLifecycleOwner, mOnBackPressedCallback!!)
            }
            initToast()
            initDialog()
            mViewModel.mLifeCycle.value = this
            mViewModel.mCustomViewActionListener = this
            mViewModel.headerStatusBarHeight.value = ImmersionBar.getStatusBarHeight(this)
                .plus(resources.getDimension(R.dimen.common_header_bar_height).toInt())
            mViewModel.mBackAction.observe(viewLifecycleOwner) {
                if (it) {
                    pop(false)
                }
            }
            onLazyAfterView()
        }
        super.onStart()
        Logger.d(
            "生命周期%s",
            "Fragment tag:${this.tag + "_" + this.javaClass.simpleName}: onStart ${++lifecycleIndex}"
        )
    }

    private var isLazyInitView = false
    override fun onResume() {
        super.onResume()
//        if (com.umeng.lib.BuildConfig.IS_INIT_UMENG_STATISTICS) { //自动采集模式不需要设置onResume和onPause 的手动调用
//            Log.i("dyn", "友盟统计onVisible - tag>${this.tag + "_" + this.javaClass.simpleName}")
//            MobclickAgent.onPageStart(this.tag + "_" + this.javaClass.simpleName)
//        }
        Logger.d(
            "生命周期%s",
            "Fragment tag:${this.tag + "_" + this.javaClass.simpleName}: onResume ${++lifecycleIndex}  time->${System.currentTimeMillis() - time}"
        )
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        Logger.d(
            "生命周期%s",
            "Fragment tag:${this.tag + "_" + this.javaClass.simpleName}: setUserVisibleHint isVisibleToUser=$isVisibleToUser ${++lifecycleIndex} "
        )
        super.setUserVisibleHint(isVisibleToUser)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        Logger.d(
            "生命周期%s",
            "Fragment tag:${this.tag + "_" + this.javaClass.simpleName}: onHiddenChanged hidden=$hidden ${++lifecycleIndex} "
        )
        super.onHiddenChanged(hidden)
    }

    override fun onPause() {
        super.onPause()
//        if (com.umeng.lib.BuildConfig.IS_INIT_UMENG_STATISTICS) { //自动采集模式不需要设置onResume和onPause 的手动调用
//            Log.i("dyn", "友盟统计onInvisible - tag>${this.tag + "_" + this.javaClass.simpleName}")
//            MobclickAgent.onPageEnd(this.tag + "_" + this.javaClass.simpleName)
//        }
        Logger.d(
            "生命周期%s",
            "Fragment tag:${this.tag + "_" + this.javaClass.simpleName}: onPause ${++lifecycleIndex}"
        )
    }

    override fun onStop() {
        super.onStop()
        Logger.d(
            "生命周期%s",
            "Fragment tag:${this.tag + "_" + this.javaClass.simpleName}: onStop ${++lifecycleIndex}"
        )
    }

    override fun onDestroyView() {
        isLazyInitView = false
        super.onDestroyView()
        Logger.d(
            "生命周期%s",
            "Fragment tag:${this.tag + "_" + this.javaClass.simpleName}: onDestroyView ${++lifecycleIndex}"
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d(
            "生命周期%s",
            "Fragment tag:${this.tag + "_" + this.javaClass.simpleName}: onDestroy ${++lifecycleIndex}"
        )
    }

    override fun onDetach() {
        super.onDetach()
        Logger.d(
            "生命周期%s",
            "Fragment tag:${this.tag + "_" + this.javaClass.simpleName}: onDetach ${++lifecycleIndex}"
        )
    }

    override fun onVisible() {
        super.onVisible()
        Logger.d(
            "生命周期%s",
            "Fragment tag:${this.tag + "_" + this.javaClass.simpleName}: onVisible ${++lifecycleIndex}"
        )
    }

    override fun onInvisible() {
        super.onInvisible()
        Logger.d(
            "生命周期%s",
            "Fragment tag:${this.tag + "_" + this.javaClass.simpleName}: onInvisible ${++lifecycleIndex}"
        )
    }

    override fun onLazyAfterView() {
        super.onLazyAfterView()
        Logger.d(
            "生命周期%s",
            "Fragment tag:${this.tag + "_" + this.javaClass.simpleName}: onLazyAfterView ${++lifecycleIndex}"
        )
    }

    override fun onLazyBeforeView() {
        super.onLazyBeforeView()
        Logger.d(
            "生命周期%s",
            "Fragment tag:${this.tag + "_" + this.javaClass.simpleName} onLazyBeforeView ${++lifecycleIndex}"
        )
    }


    /**
     * 初始化弹框相应监听
     * */
    private fun initDialog() {
        mViewModel.mLoadingDialogStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                LOADING_DIALOG_STATUS_SHOW_CANCEL -> {
                    progressLoading.showCancelableLoading()
                }

                LOADING_DIALOG_STATUS_SHOW_UNCANCEL -> {
                    progressLoading.showUnCancelableLoading()
                }
                LOADING_DIALOG_STATUS_SHOW_UNCANCEL_STR -> {
                    progressLoading.showUnCancelableLoading(mViewModel.mLoadingDialogTipsStr)
                }

                LOADING_DIALOG_STATUS_HIDE -> {
                    progressLoading.hideLoading()
                }
            }
        })
    }

    fun showCancelableLoading() {
        progressLoading.showCancelableLoading()
    }

    fun showUnCancelableLoading() {
        progressLoading.showUnCancelableLoading()
    }

    fun hideLoading() {
        progressLoading.hideLoading()
    }


    /**
     * 初始化toast 操作监听
     * */
    private fun initToast() {
        mViewModel.mShortToastStr.observe(this.viewLifecycleOwner, Observer<Any?> { data ->
            when (data) {
                is String? -> {
                    data?.let {
                        showToast(data, duration = Toast.LENGTH_SHORT)
                    }
                }

                is Int -> {
                    data?.let {
                        showToast(data, Toast.LENGTH_SHORT)
                    }
                }

                is CustomToastView.CustomToastBean -> {
                    val toast = if (TextUtils.isEmpty(data.toast)) {
                        try {
                            getString(data.res)
                        } catch (e: java.lang.Exception) {
                            null
                        }
                    } else {
                        data.toast
                    }
                    toast?.let {
                        showToast(it, type = data.type, duration = Toast.LENGTH_SHORT)
                    }
                }
            }
        })
        mViewModel.mLongToastStr.observe(this.viewLifecycleOwner, Observer<Any?> { data ->
            when (data) {
                is String? -> {
                    data?.let {
                        showToast(data, duration = Toast.LENGTH_LONG)
                    }
                }

                is Int -> {
                    data?.let {
                        showToast(data, Toast.LENGTH_LONG)
                    }
                }

                is CustomToastView.CustomToastBean -> {
                    val toast = if (TextUtils.isEmpty(data.toast)) {
                        getString(data.res)
                    } else {
                        data.toast
                    }
                    toast?.let {
                        showToast(it, type = data.type, duration = Toast.LENGTH_LONG)
                    }
                }
            }
        })
    }

    /**
     * 获取当前fragment对应的 controller控制器
     * */
    protected open fun fragmentController(fragment: Fragment = this): NavController {
        return NavHostFragment.findNavController(fragment)
    }

    /**
     * 获取activity的根路由
     * 通过activity页  根fragment 的 ID 获取对应的 controller控制器
     * */
    private fun activityController(@IdRes id: Int): NavController {
        return Navigation.findNavController(requireActivity(), id)
    }

    /**
     * 通过activity 的根控制器去路由到新页面
     * */
    fun activityNav(@IdRes rootViewId: Int, @IdRes destinationId: Int, bundle: Bundle? = null) {
        navWithBundle(activityController(rootViewId), destinationId, bundle, null, false)
    }

    /**
     * 和根据activity获取到根路由 实现的功能一致
     * 调用父类 fragment 获取控制器（调用的目的还是获取到activity的根控制器）
     * */
    fun parentNav(@IdRes destinationId: Int, bundle: Bundle? = null) {
        var fragment = requireParentFragment()
//        if (fragment is MNavHostFragment) {
//            navWithBundle(
//                fragmentController(fragment.requireParentFragment()),
//                destinationId,
//                bundle,
//                null,
//                false
//            )
//        } else {
        navWithBundle(fragmentController(fragment), destinationId, bundle, null, false)
//        }
    }

    fun fragmentNav(@IdRes destinationId: Int) {
        navWithBundle(fragmentController(), destinationId, null)
    }

    fun fragmentNav(@IdRes destinationId: Int, bundle: Bundle?) {
        navWithBundle(fragmentController(), destinationId, bundle)
    }

    fun fragmentNav(@IdRes destinationId: Int, bundle: Bundle?, isLaunchSingleTop: Boolean) {
        navWithBundle(
            fragmentController(),
            destinationId,
            bundle,
            isLaunchSingleTop = isLaunchSingleTop
        )
    }

    /**
     * 把当前页退出进入新的页面
     * */
    fun fragmentNavWidthPop(@IdRes destinationId: Int) {
        navWithBundle(
            fragmentController(),
            destinationId,
            null,
            fragmentController().currentDestination?.id,
            true
        )
    }

    /**
     * 把当前页退出进入新的页面
     * */
    fun fragmentNavWidthPop(@IdRes destinationId: Int, bundle: Bundle) {
        navWithBundle(
            fragmentController(),
            destinationId,
            bundle,
            fragmentController().currentDestination?.id,
            true
        )
    }

    /**
     * 退出到指定页面并进入新的页面
     * @param popUpToId 退出到指定的页面
     * @param inclusive 是否包含退出到的指定页面
     * */
    fun fragmentNavWidthPopToId(
        @IdRes destinationId: Int,
        popUpToId: Int,
        inclusive: Boolean, bundle: Bundle?
    ) {
        navWithBundle(
            fragmentController(),
            destinationId,
            bundle,
            popUpToId,
            inclusive
        )
    }

    /**
     * 退出到指定页面并进入新的页面
     * @param popUpToId 退出到指定的页面
     * @param inclusive 是否包含退出到的指定页面
     * */
    fun fragmentNavWidthPopToId(
        @IdRes destinationId: Int,
        popUpToId: Int,
        inclusive: Boolean
    ) {
        fragmentNavWidthPopToId(
            destinationId,
            popUpToId,
            inclusive,
            null
        )
    }

    /**
     *最终navigation 调用的方法
     * @param navController 路由控制器
     * @param destinationId 目的地Id
     * @param bundle 携带参数
     * @param popUpToId 回退Id
     * @param inclusive 回退是否包含传入的popupToIdId
     * @param isLaunchSingleTop 是否顶部只能存在一个实例
     * */
    private fun navWithBundle(
        navController: NavController,
        @IdRes destinationId: Int,
        bundle: Bundle? = null,
        popUpToId: Int? = null,
        inclusive: Boolean = false,
        isLaunchSingleTop: Boolean = false
    ) {
        try {
            navController.customNav(destinationId, bundle, popUpToId, inclusive, isLaunchSingleTop)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun pop(isCheckKeyBoard: Boolean = true): Boolean {
        return try {
            var id: Int = 0
            if (fragmentController().currentDestination?.id?.also { id = it } != null) {
                pop(destinationId = id, inclusive = true, isCheckKeyBoard)
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    /**
     * pop 最终回调
     * 会退到 destinationId 节点
     * @param inclusive 是否包含 destinationId 节点
     * */
    protected open fun pop(
        @IdRes destinationId: Int,
        inclusive: Boolean = false,
        isCheckKeyBoard: Boolean = true
    ): Boolean {
        Logger.i("------------------------------回退最终回调----------------$isCheckKeyBoard------------------")
        return try {
            if (isCheckKeyBoard && ifKeyboardShowToHide()) {
                return true
            } else {
                ifKeyboardShowToHide()
            }
            fragmentController().popBackStack(destinationId, inclusive)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun onAction(view: View, action: String, viewModel: BaseCustomModel) {
        when (action) {
            BaseActionConstant.ACTION_BACK -> {
                onClickHeaderBack()
            }

            BaseActionConstant.ACTION_FINISH -> {
                onClickHeaderFinish()
            }

            BaseActionConstant.ACTION_RIGHT -> {
                onClickHeaderRight()
            }

            BaseActionConstant.ACTION_RIGHT_LAST -> {
                onClickHeaderRightLast(view)
            }
        }
    }

    protected open fun onClickHeaderRight() {

    }

    protected open fun onClickHeaderFinish() {

    }

    protected open fun onClickHeaderRightLast(view: View) {

    }

    protected open fun onClickHeaderBack() {
        if (ifKeyboardShowToHide().not())
            requireActivity().onBackPressed()
    }

    /***权限相关API   start ****/
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Logger.i("$LOG_TAG_PERMISSIONS fragment   onPermissionsGranted")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        Logger.i("$LOG_TAG_PERMISSIONS fragment   onPermissionsDenied")
    }

    override fun onRationaleAccepted(requestCode: Int) {
        Logger.i("$LOG_TAG_PERMISSIONS fragment   onRationaleAccepted")
    }

    override fun onRationaleDenied(requestCode: Int) {
        Logger.i("$LOG_TAG_PERMISSIONS fragment   onRationaleDenied")
    }

    private var mNexAction: OnRequestPermissionListener? = null
    private var mPermissions: Array<out String>? = null
    private var mTips = "当前App需要请求权限操作"

//    @AfterPermissionGranted(PERMISSION_FRAGMENT_RC_CODE)
//    private fun requestPermission() {
//        mPermissions?.let {
//            if (hasPermission(*it)) {
//                // Have permission, do the thing!
//                mNexAction?.onInvoke()
//            } else {
//                // Ask for one permission
//                EasyPermissions.requestPermissions(this,
//                    PermissionRequest.Builder(this.requireContext())
//                        .code(PERMISSION_FRAGMENT_RC_CODE)
//                        .perms(it)
//                        .theme(com.dyn.base.R.style.PermissionsThemeDialog)
//                        .rationale(mTips).build()
//                )
//            }
//        }
//    }

    /**
     * 请求权限操作，如果有继续后续操作
     * */
    fun requestPermissionAndInvokeAction(
        tips: String,
        vararg params: String,
        action: OnRequestPermissionListener?
    ) {
        mNexAction = action
        mPermissions = params
        mTips = tips
        Logger.i("$LOG_TAG_PERMISSIONS fragment   requestPermission")
        val activity = requireActivity()
        if (activity is PermissionProxyClient) {
            activity.registerPermissionResult(this)
            activity.requestPermission(tips, params = params, action = action)
        }
    }

    fun hasPermission(vararg args: String): Boolean {
        return EasyPermissions.hasPermissions(requireContext(), *args)
    }

    /***权限相关API   end ****/

    /**
     * @return true 软件盘切换成功  false 软键盘没显示
     * */
    private fun ifKeyboardShowToHide(): Boolean {
        return if (KeyboardUtils.isSoftInputVisible(requireActivity())) {
            KeyboardUtils.hideSoftInput(requireActivity())
            true
        } else {
            false
        }
    }


}