package com.dyn.base.ui.navigationbar

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.dyn.base.binding_adapter.BindingCommonAdapter
import com.dyn.base.customview.BaseCustomView
import com.dyn.base.customview.BaseCustomModel
import com.dyn.base.customview.ICustomViewActionListener

/**
 * 利用构建者模式创建 NavigationBar
 * */
abstract class AbsNavigationBar<B : AbsNavigationBar.Builder<B,D,T,V>,D : ViewDataBinding, T : BaseCustomModel,V:BaseCustomView<D,T>>(private val mBuilder: B) : INavigation {
    private lateinit var mNavigationBar: View

    init {
        createNavigationBar()
    }

    override fun createNavigationBar() {
        mNavigationBar = mBuilder.mNavigationBar
        attachParent(mNavigationBar, mBuilder.mParent)
        attachNavigationParams()
    }

    override fun  attachNavigationParams(){

    }

    override fun attachParent(navigationBar: View, parent: ViewGroup) {
        parent.addView(navigationBar, 0)
    }

    abstract class Builder<B : Builder<B,D,T,V>,D : ViewDataBinding, T : BaseCustomModel,V:BaseCustomView<D,T>>(val mContext: Context, val mNavigationBar: V, val mParent: ViewGroup) {

        fun bindData(data:T):B{
            mNavigationBar.setData(data)
            return this as B
        }

        fun bindAction(actionListener : ICustomViewActionListener):B{
            mNavigationBar.mActionListener = actionListener
            return this as B
        }
        fun bindStatusBar():B{
            BindingCommonAdapter.bindStatusBar(mNavigationBar,true)
            return this as B
        }

        abstract fun create(): AbsNavigationBar<B,D,T,V>
    }

    fun <T : View> findViewById(id: Int): T? {
        return mNavigationBar.findViewById(id)
    }
}