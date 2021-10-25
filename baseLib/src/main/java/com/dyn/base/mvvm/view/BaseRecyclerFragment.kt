package com.dyn.base.mvvm.view

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.dyn.base.BR
import com.dyn.base.R
import com.dyn.base.mvvm.viewmodel.BaseNetRecyclerViewModel
import com.dyn.base.mvvm.viewmodel.BaseRecyclerViewModel
import com.dyn.base.ui.databinding.DataBindingConfig

/**
 * loadSir + smartRefreshLayout + recyclerView
 * 对应ViewModel ->  BasePageViewModel
 * */
abstract class BaseRecyclerFragment<VM: BaseRecyclerViewModel<*>> : BaseFragment<VM>() {

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(BR.vm,mViewModel)
            .addBindingParam(BR.layoutManager,getLayoutManager())
            .addBindingParam(BR.view,addBottomView())
            .addBindingParam(BR.adapter,createAdapter())

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_base_recycler
    }

    protected open fun addBottomView():View?{
        return null
    }

    abstract fun createAdapter():BaseQuickAdapter<*,*>

    protected open fun getLayoutManager():RecyclerView.LayoutManager{
        return LinearLayoutManager(requireContext())
    }

}