package com.dyn.base.ui.databinding

import android.app.Activity
import android.app.Application
import android.content.pm.ApplicationInfo
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.dyn.base.R

abstract class DataBindingActivity :AppCompatActivity(){
    private var mActivityProvider: ViewModelProvider? = null
    private var mFactory: ViewModelProvider.Factory? = null
    private var mBinding: ViewDataBinding? = null
    private var mTvStrictModeTip: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataBindingConfig = getDataBindingConfig()
        mBinding = DataBindingUtil.setContentView(this,getLayoutId())
        mBinding?.lifecycleOwner = this
        mBinding?.setVariable(dataBindingConfig.vmVariableId,dataBindingConfig.stateViewModel)
        val bindingParams = dataBindingConfig.bindingParams
        bindingParams?.let {
            it.forEach { key, value ->
                mBinding?.setVariable(key,value)
            }
        }
    }


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
                mTvStrictModeTip = TextView(applicationContext)
                mTvStrictModeTip!!.setAlpha(0.5f)
                mTvStrictModeTip!!.setTextSize(16f)
                mTvStrictModeTip!!.setBackgroundColor(Color.WHITE)
                mTvStrictModeTip!!.setText(R.string.debug_fragment_databinding_warning)
                (mBinding!!.root as ViewGroup).addView(mTvStrictModeTip)
            }
        }
        return mBinding
    }

    fun isDebug():Boolean{
        return applicationContext!= null && applicationContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    }

    fun showLongToast(text:String){
        Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
    }

    fun showShortToast(text:String){
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }
    fun showLongToast(@StringRes text:Int){
        Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
    }

    fun showShortToast(@StringRes text:Int){
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }


    fun <T : ViewModel> getActivityViewModel(clazz: Class<T>):T{
        if (mActivityProvider == null){
            mActivityProvider = ViewModelProvider(this)
        }
        return mActivityProvider!!.get(clazz)
    }

    fun getAppViewModelProvider():ViewModelProvider{
        return ViewModelProvider(applicationContext as ViewModelStoreOwner,getAppFactory(this))
    }

    private fun getAppFactory(activity:Activity): ViewModelProvider.Factory {
        val application = checkApplication(activity)
        if (mFactory == null){
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        }
        return mFactory!!
    }

    private fun checkApplication(activity: Activity): Application {
        return activity.application ?: throw IllegalStateException("Your activity/fragment is not yet attached to \"\n" +
                "                    + \"Application. You can't request ViewModel before onCreate call.")
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding?.unbind()
        mBinding = null
    }

    abstract fun getDataBindingConfig():DataBindingConfig
    @LayoutRes
    abstract fun getLayoutId():Int
}