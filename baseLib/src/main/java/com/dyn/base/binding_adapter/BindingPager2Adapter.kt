package com.dyn.base.binding_adapter

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.dyn.base.ui.base.recycler.BasePager2Adapter
import com.dyn.base.ui.base.recycler.BaseRecyclerAdapter

object BindingPager2Adapter {

    @JvmStatic
    @InverseBindingAdapter(
        attribute = "pager2Position",
        event = "pager2Listener"
    )
    fun getPager2Position(sBt: ViewPager2): Int = sBt.currentItem


    @BindingAdapter(
        value = ["pager2Adapter"],
        requireAll = false
    )
    @JvmStatic
    fun initViewPager2Adapter(
        view: ViewPager2,
        adapter: RecyclerView.Adapter<*>?
    ) {
        adapter?.let {
            if (view.adapter != adapter) {
                view.adapter = adapter
            }
        }
    }

    @BindingAdapter(
        value = ["pager2Data"],
        requireAll = false
    )
    @JvmStatic
    fun initViewPager2Adapter(
        view: ViewPager2,
        pager2Data: MutableList<Nothing>?
    ) {
        view.adapter?.let { adapter ->
            if (adapter is BasePager2Adapter<*>) {
                pager2Data?.let {
                    adapter.setList(it)
                }
            }
            if (adapter is BaseRecyclerAdapter<*, *>) {
                pager2Data?.let {
                    adapter.submitList(it)
                }
            }
        }
    }

    @BindingAdapter(
        value = ["pager2Position", "pagerSmoothScroll"],
        requireAll = false
    )
    @JvmStatic
    fun initViewPager2Adapter(
        view: ViewPager2,
        position: Int,
        smoothScroll: Boolean = false
    ) {
        if (view.currentItem != position) {
            view.setCurrentItem(position, smoothScroll)
        }
    }

    //
    @BindingAdapter(
        value = ["pager2PositionListener"],
        requireAll = false
    )
    @JvmStatic
    fun initViewPager2Adapter(
        view: ViewPager2,
        listener: BindingCommonAdapter.OnPositionListener?
    ) {
        view.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                listener?.let {
                    it.onSelect(position)
                }
            }
        })
    }

    //
    @BindingAdapter(
        value = ["pager2OnPageChangeCallback"],
        requireAll = false
    )
    @JvmStatic
    fun registerViewPager2ChangeCallback(
        view: ViewPager2,
        listener: ViewPager2.OnPageChangeCallback?
    ) {
        listener?.let {
            view.registerOnPageChangeCallback(it)
        }
    }

    @BindingAdapter(
        value = ["pagerChangeCallback"],
        requireAll = false
    )
    @JvmStatic
    fun initViewPager2Adapter(
        view: ViewPager2,
        callback: ViewPager2.OnPageChangeCallback?,
    ) {
        callback?.let {
            view.registerOnPageChangeCallback(it)
        }
    }

    @BindingAdapter(
        value = ["pagerLimitSize"],
        requireAll = false
    )
    @JvmStatic
    fun initViewPager2Adapter(
        view: ViewPager2,
        limitSize: Int
    ) {
        if (limitSize > 0) {
            view.offscreenPageLimit = limitSize
        }
    }

    @BindingAdapter(
        value = ["pager2Listener"],
        requireAll = false
    )
    @JvmStatic
    fun initViewPager2Adapter(
        view: ViewPager2,
        pager2Listener: InverseBindingListener?
    ) {
        view.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                pager2Listener?.onChange()
            }
        })
    }
//    @BindingAdapter(
//        value = ["pager2Adapter", "pager2Data", "pager2Position", "pager2PositionListener", "pagerChangeCallback", "pagerLimitSize", "pagerSmoothScroll","pager2Listener"],
//        requireAll = false
//    )
//    @JvmStatic
//    fun initViewPager2Adapter(
//        view: ViewPager2,
//        adapter: RecyclerView.Adapter<*>?,
//        pager2Data: MutableList<Nothing>?,
//        position: Int,
//        listener: BindingCommonAdapter.OnPositionListener?,
//        callback: ViewPager2.OnPageChangeCallback?,
//        limitSize: Int,
//        smoothScroll: Boolean,
//        pager2Listener: InverseBindingListener?
//    ) {
//        if (limitSize > 0) {
//            view.offscreenPageLimit = limitSize
//        }
//        if (view.adapter != adapter && adapter != null) {
//            view.adapter = adapter
//        }
//        view.adapter?.let { adapter ->
//            if (adapter is BasePager2Adapter<*>) {
//                pager2Data?.let {
//                    adapter.setList(it)
//                }
//            }
//            if (adapter is BaseRecyclerAdapter<*, *>) {
//                pager2Data?.let {
//                    adapter.setList(it)
//                }
//            }
//        }
//        if (view.currentItem != position) {
//            view.setCurrentItem(position, smoothScroll)
//        }
//        view.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                listener?.let {
//                    it.onSelect(position)
//                }
//                pager2Listener?.onChange()
//            }
//        })
//        callback?.let {
//            view.registerOnPageChangeCallback(it)
//        }
//    }


    @BindingAdapter(
        value = ["pager2ChildPadding_L", "pager2ChildPadding_T", "pager2ChildPadding_R", "pager2ChildPadding_B"],
        requireAll = false
    )
    @JvmStatic
    fun pager2ChildPadding(
        view: ViewPager2,
        l: Float,
        t: Float,
        r: Float,
        b: Float,
    ) {
        val recyclerView = view.getChildAt(0) as RecyclerView
        recyclerView.apply {
//            val padding = resources.getDimensionPixelOffset(R.dimen.halfPageMargin) +
//                    resources.getDimensionPixelOffset(R.dimen.peekOffset)
            // setting padding on inner RecyclerView puts overscroll effect in the right place
            // TODO: expose in later versions not to rely on getChildAt(0) which might break
            setPadding(l.toInt(), t.toInt(), r.toInt(), b.toInt())
            clipToPadding = false
        }
    }

    @BindingAdapter(value = ["pagerIsUserInputEnabled"], requireAll = false)
    @JvmStatic
    fun pagerIsUserInputEnabled(
        view: ViewPager2,
        isUserInputEnable: Boolean = true
    ) {

        view.isUserInputEnabled = isUserInputEnable
    }
    @BindingAdapter(value = ["pager2Transformer"], requireAll = false)
    @JvmStatic
    fun pagerIsUserInputEnabled(
        view: ViewPager2,
        transformer: CompositePageTransformer? = null
    ) {
        transformer?.let {
            view.setPageTransformer(it)
        }
    }
    @BindingAdapter(value = ["pager2RecyclerViewPadding_L","pager2RecyclerViewPadding_R"], requireAll = false)
    @JvmStatic
    fun setPager2RecyclerViewPadding(
        view: ViewPager2,
        leftItemPadding: Int? = 0,
        rightItemPadding: Int? = 0,
    ) {
        val recyclerView = view.getChildAt(0) as RecyclerView
        if (view.orientation == ViewPager2.ORIENTATION_VERTICAL) {
            recyclerView.setPadding(
                view.paddingLeft,
                leftItemPadding?:0,
                view.paddingRight,
                rightItemPadding?:0
            )
        } else {
            recyclerView.setPadding(
                leftItemPadding?:0,
                view.paddingTop,
                rightItemPadding?:0,
                view.paddingBottom
            )
        }
        recyclerView.clipToPadding = false
    }
}