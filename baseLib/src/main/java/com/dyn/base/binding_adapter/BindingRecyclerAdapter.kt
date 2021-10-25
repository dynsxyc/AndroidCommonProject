package com.dyn.base.binding_adapter

import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ConvertUtils.dp2px
import com.chad.library.adapter.base.BaseQuickAdapter
import com.dyn.base.ui.base.recycler.BaseMultiRecyclerAdapter
import com.dyn.base.ui.base.recycler.BaseRecyclerAdapter
import com.dyn.base.ui.recyclerdivider.*

object BindingRecyclerAdapter {
    @BindingAdapter(
        value = ["adapter"],
        requireAll = false
    )
    @JvmStatic
    fun initRecyclerAdapter(
        view: RecyclerView,
        adapter: BaseQuickAdapter<*, *>?
    ) {
        if (view.adapter != adapter && adapter != null) {
            view.adapter = adapter
        }
    }
    @BindingAdapter(
        value = ["childAttachStateChangeListener"],
        requireAll = false
    )
    @JvmStatic
    fun childAttachStateChangeListener(
        view: RecyclerView,
        adapter: BaseQuickAdapter<*, *>?
    ) {
        view.addOnChildAttachStateChangeListener(object :RecyclerView.OnChildAttachStateChangeListener{
            override fun onChildViewAttachedToWindow(view: View) {
            }

            override fun onChildViewDetachedFromWindow(view: View) {
            }

        })
        view.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.i("changed->","onScrollStateChanged")
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.i("changed->","onScrolled")
            }
        })
    }

    @BindingAdapter(
        value = ["itemData"],
        requireAll = false
    )
    @JvmStatic
    fun initRecyclerData(
        view: RecyclerView,
        itemData: Collection<Nothing>?
    ) {
        when (val adapter = view.adapter) {
            is BaseQuickAdapter<*, *> -> {
                adapter.setList(itemData)
            }
        }
    }

    @BindingAdapter(
        value = ["removeItemByData"],
        requireAll = false
    )
    @JvmStatic
    fun removeItemByData(
        view: RecyclerView,
        removeData: Any?
    ) {
        view.adapter?.let { adapter ->
            if (adapter is BaseQuickAdapter<*, *>) {
                removeData?.let {
                    val index = adapter.data?.indexOf(it)
                    if (index != -1) {
                        adapter.removeAt(index)
                    }
                }
            }
        }
    }

    /**
     * true if adapter changes cannot affect the size of the RecyclerView.
     * 如果适配器更改不能影响RecyclerView的大小，则为true。
     * */
    @BindingAdapter(
        value = ["hasFixedSize"],
        requireAll = false
    )
    @JvmStatic
    fun initRecyclerAdapter(
        view: RecyclerView,
        hasFixedSize: Boolean = false
    ) {
        view?.let {
            it.setHasFixedSize(hasFixedSize)
        }
    }

    @BindingAdapter(
        value = ["layoutManager"],
        requireAll = false
    )
    @JvmStatic
    fun layoutManager(
        view: RecyclerView,
        layoutManager: RecyclerView.LayoutManager?,
    ) {
        layoutManager?.let {
            view.layoutManager = layoutManager
        }
    }

    @BindingAdapter(value = ["itemDecoration"], requireAll = false)
    @JvmStatic
    fun itemDecoration(recyclerView: RecyclerView, itemDecoration: RecyclerView.ItemDecoration?) {
        if (itemDecoration != null) {
            recyclerView.addItemDecoration(itemDecoration)
        } else {
            for (i in 0 until recyclerView.itemDecorationCount) {
                recyclerView.removeItemDecorationAt(i)
            }
        }
    }

    /**
     * 线性offset分割线
     * */
    @BindingAdapter(
        value = ["linerOffsetDecorationSize", "linerOffsetDecorationOrientation", "linerOffsetDecorationHasEdge", "linerOffsetDecorationFirstTopSize", "linerOffsetDecorationHasFirstTop"],
        requireAll = false
    )
    @JvmStatic
    fun itemLinerDecoration(
        recyclerView: RecyclerView,
        width: Float,
        orientation: Int = LinearOffsetsItemDecoration.LINEAR_OFFSETS_VERTICAL,
        linerOffsetDecorationHasEdge: Boolean = false,
        firstTop: Float,
        hasFirstTop: Boolean
    ) {
        for (i in 0 until recyclerView.itemDecorationCount) {
            val decoration = recyclerView.getItemDecorationAt(i)
            if (decoration is LinearOffsetsItemDecoration) {
                return
            }
        }
        val itemDecoration = LinearOffsetsItemDecoration(orientation)
        itemDecoration.setItemOffsets(dp2px(width))
        itemDecoration.setItemFirstTopOffsets(dp2px(firstTop))
        itemDecoration.setOffsetEdge(linerOffsetDecorationHasEdge)
        itemDecoration.setFirstTopOffset(hasFirstTop)
        recyclerView.addItemDecoration(itemDecoration)
    }

    /**
     * 线性offset分割线
     * */
    @BindingAdapter(
        value = ["linerDividerSize", "linerDividerOrientation", "linerDividerNoHasLast", "linerDividerColor", "linerDividerLeftOffset", "linerDividerRightOffset"],
        requireAll = false
    )
    @JvmStatic
    fun itemLinerDividerDecoration(
        recyclerView: RecyclerView,
        size: Float,
        orientation: Int = LinearDividerItemDecoration.LINEAR_DIVIDER_VERTICAL,
        lastNotHasLine: Boolean = true,
        @ColorInt dividerColor: Int,
        leftOffset: Float,
        rightOffset: Float
    ) {

        for (i in 0 until recyclerView.itemDecorationCount) {
            val decoration = recyclerView.getItemDecorationAt(i)
            if (decoration is LinearDividerItemDecoration) {
                return
            }
        }
        val itemDecoration = LinearDividerItemDecoration(recyclerView.context)
        itemDecoration.setLastNoHasLine(lastNotHasLine)
        itemDecoration.setOrientation(orientation)
        itemDecoration.setLeftOffsets(leftOffset.toInt())
        itemDecoration.setRightOffsets(rightOffset.toInt())
        val drawable = ShapeDrawable(OvalShape())
        if (orientation == LinearDividerItemDecoration.LINEAR_DIVIDER_VERTICAL) {
            drawable.intrinsicWidth = -1
            drawable.intrinsicHeight = dp2px(size)
        } else {
            drawable.intrinsicWidth = dp2px(size)
            drawable.intrinsicHeight = -1
        }
        drawable.paint.color = dividerColor
        itemDecoration.setDivider(drawable)
        recyclerView.addItemDecoration(itemDecoration)
    }

    /**
     * 水平分割线
     * */
    @BindingAdapter(
        value = ["gridOffsetDecorationSizeH", "gridOffsetDecorationSizeV", "gridOffsetDecorationOrientation", "gridOffsetDecorationHasEdge", "gridOffsetDecorationHasLast", "gridOffsetDecorationHasFirst"],
        requireAll = false
    )
    @JvmStatic
    fun itemGridDecoration(
        recyclerView: RecyclerView,
        hSize: Float,
        vSize: Float,
        orientation: Int = GridOffsetsItemDecoration.GRID_OFFSETS_VERTICAL,
        gridOffsetDecorationHasEdge: Boolean = false,
        gridOffsetDecorationHasLast: Boolean = false,
        gridOffsetDecorationHasFirst: Boolean = false,
    ) {
        for (i in 0 until recyclerView.itemDecorationCount) {
            val decoration = recyclerView.getItemDecorationAt(i)
            if (decoration is GridOffsetsItemDecoration) {
                return
            }
        }
        val itemDecoration = GridOffsetsItemDecoration(orientation)
        itemDecoration.setHorizontalItemOffsets(dp2px(hSize))
        itemDecoration.setVerticalItemOffsets(dp2px(vSize))
        itemDecoration.setOffsetEdge(gridOffsetDecorationHasEdge)
        recyclerView.addItemDecoration(itemDecoration)
    }

    /**
     * 水平分割线
     * */
    @BindingAdapter(
        value = ["gridDividerDecorationOrientation", "gridDividerDecorationDrawableV", "gridDividerDecorationDrawableH"],
        requireAll = false
    )
    @JvmStatic
    fun itemGridDividerDecoration(
        recyclerView: RecyclerView,
        orientation: Int = GridCustomDividerItemDecoration.GRID_DIVIDER_VERTICAL,
        drawableV: Drawable? = null,
        drawableH: Drawable? = null
    ) {
        for (i in 0 until recyclerView.itemDecorationCount) {
            val decoration = recyclerView.getItemDecorationAt(i)
            if (decoration is GridCustomDividerItemDecoration) {
                return
            }
        }
        val itemDecoration = GridCustomDividerItemDecoration(recyclerView.context, orientation)
        itemDecoration.setOrientation(orientation)
        itemDecoration.setVerticalDivider(drawableV)
        itemDecoration.setHorizontalDivider(drawableH)
        recyclerView.addItemDecoration(itemDecoration)
    }

    @BindingAdapter(value = ["changePosition"], requireAll = false)
    @JvmStatic
    fun changePosition(recyclerView: RecyclerView, position: Int) {
        recyclerView.smoothScrollToPosition(position)
    }

    @BindingAdapter(value = ["changeScrollToPosition"], requireAll = false)
    @JvmStatic
    fun changeScrollToPosition(recyclerView: RecyclerView, position: Int) {
    }

    @InverseBindingAdapter(attribute = "changeScrollToPosition", event = "scrollPositionListener")
    @JvmStatic
    fun getScrollToPosition(recyclerView: RecyclerView): Int {
        val layoutManager = recyclerView.layoutManager
        layoutManager?.let {
            if (it is LinearLayoutManager) {
                return it.findFirstVisibleItemPosition()
            }
        }
        return RecyclerView.NO_POSITION
    }

    @BindingAdapter("scrollPositionListener", requireAll = false)
    @JvmStatic
    fun scrollPositionListener(
        recyclerView: RecyclerView,
        inverseBindingListener: InverseBindingListener?
    ) {
        inverseBindingListener?.let {
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    it.onChange()
                }
            })
        }
    }
}