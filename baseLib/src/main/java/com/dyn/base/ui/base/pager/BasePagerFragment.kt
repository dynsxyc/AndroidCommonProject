package com.dyn.base.ui.base.pager

import androidx.fragment.app.Fragment
import com.dyn.base.BR
import com.dyn.base.R
import com.dyn.base.mvvm.view.BaseFragment
import com.dyn.base.ui.base.recycler.BasePager2Adapter
import com.dyn.base.ui.databinding.DataBindingConfig
import com.dyn.base.ui.magicindicator.IMagicItem

abstract class BasePagerFragment<B: BasePagerViewModel> : BaseFragment<B>() {
    override fun getDataBindingConfig(): DataBindingConfig {
        return super.getDataBindingConfig().addBindingParam(BR.pager2Adapter,createPagerAdapter(this))
    }
    companion object{
        const val POSITION = "pager_position"
    }
    override fun onLazyAfterView() {
        super.onLazyAfterView()
        val position = arguments?.getInt(POSITION)
        position?.let {
            if (mViewModel.pagerPosition.value != it){
                mViewModel.pagerPosition.postValue(it)
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_base_pager
    }
    override fun onVisible() {
        super.onVisible()
        val currentFragment = childFragmentManager.findFragmentByTag("f${mViewModel.pagerPosition.value}")
        currentFragment?.let {
            if (it is BaseFragment<*> ) {
                it.onVisible()
            }
        }
    }

    abstract fun createPagerAdapter(fragment: Fragment):BasePager2Adapter<out IMagicItem>
}