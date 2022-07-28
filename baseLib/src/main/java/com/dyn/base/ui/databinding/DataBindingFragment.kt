package com.dyn.base.ui.databinding

import android.app.Activity
import android.app.Application
import android.content.pm.ApplicationInfo
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.util.keyIterator
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.blankj.utilcode.util.ToastUtils
import com.dyn.base.R
import com.dyn.base.ui.base.BaseApplication
import com.dyn.base.ui.weight.CustomToastView
import com.dyn.base.utils.BaseToastUtils
import java.lang.Exception

abstract class DataBindingFragment : Fragment() {
    private var mFragmentProvider: ViewModelProvider? = null
    private var mActivityProvider: ViewModelProvider? = null
    private var mFactory: ViewModelProvider.Factory? = null
    private var mBinding: ViewDataBinding? = null
    private var mDataBindingConfig: DataBindingConfig? = null
    private var mTvStrictModeTip: TextView? = null


    /**
     * TODO tip: 警惕使用。非必要情况下，尽可能不在子类中拿到 binding 实例乃至获取 view 实例。使用即埋下隐患。
     * 目前的方案是在 debug 模式下，对获取实例的情况给予提示。
     *
     *
     * 如果这样说还不理解的话，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910
     *
     * @return binding
     */
    protected open fun getBinding(): ViewDataBinding? {
        if (isDebug() && mBinding != null) {
            if (mTvStrictModeTip == null) {
                mTvStrictModeTip = TextView(context)
                mTvStrictModeTip!!.alpha = 0.5f
                mTvStrictModeTip!!.textSize = 16f
                mTvStrictModeTip!!.setBackgroundColor(Color.WHITE)
                mTvStrictModeTip!!.setText(R.string.debug_fragment_databinding_warning)
                (mBinding!!.root as ViewGroup).addView(mTvStrictModeTip)
            }
        }
        return mBinding
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mBinding == null) {
            mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
            mBinding!!.lifecycleOwner = this
        }
        //        if (rootView.background == null){
//            rootView.setBackgroundColor(ColorUtils.getColor(R.color.common_bg))
//        }
        return mBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mDataBindingConfig = getDataBindingConfig()
        mBinding!!.setVariable(
            mDataBindingConfig!!.vmVariableId,
            mDataBindingConfig!!.stateViewModel
        )
        val bindingParams = mDataBindingConfig!!.bindingParams
        bindingParams.keyIterator().forEach {
            mBinding!!.setVariable(it, bindingParams.get(it))
        }
        mBinding!!.executePendingBindings()
    }


    fun isDebug(): Boolean {
        return context?.let {
            it.applicationContext != null && it.applicationContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } ?: false
    }

    fun showToast(
        text: String,
        @CustomToastView.ToastType type: Int = CustomToastView.TYPE_TIPS,
        duration: Int = Toast.LENGTH_SHORT
    ) {
        BaseToastUtils.showToast(text,type,duration)
    }


    fun showToast(@StringRes text: Int, duration: Int = Toast.LENGTH_SHORT) {
        val str = try {
            getString(text)
        } catch (e: Exception) {
            null
        }
        if (str.isNullOrBlank().not()) {
            showToast(str!!, duration = duration)
        }
    }


    fun <T : ViewModel> getFragmentViewModel(clazz: Class<T>): T {
        return ViewModelProvider(
            this,
            getViewModelFactory() ?: defaultViewModelProviderFactory
        ).get(clazz)
    }

    protected open fun getViewModelFactory(): ViewModelProvider.Factory? {
        return defaultViewModelProviderFactory
    }


    fun <T : ViewModel> getActivityViewModel(clazz: Class<T>): T {
        if (mActivityProvider == null) {
            mActivityProvider = ViewModelProvider(requireActivity())
        }
        return mActivityProvider!!.get(clazz)
    }

    fun getAppViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(
            requireActivity().applicationContext as ViewModelStoreOwner,
            getAppFactory(requireActivity())
        )
    }

    private fun getAppFactory(activity: Activity): ViewModelProvider.Factory {
        checkActivity(this)
        val application = checkApplication(activity)
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        }
        return mFactory!!
    }

    private fun checkApplication(activity: Activity): Application {
        return activity.application
            ?: throw IllegalStateException(
                "Your activity/fragment is not yet attached to \"\n" +
                        "                    + \"Application. You can't request ViewModel before onCreate call."
            )
    }

    private fun checkActivity(fragment: Fragment) {
        if (fragment.activity == null) {
            throw IllegalStateException("Can't create ViewModelProvider for detached fragment")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding?.unbind()
        mBinding = null
    }

    abstract fun getDataBindingConfig(): DataBindingConfig

    @LayoutRes
    abstract fun getLayoutId(): Int
}