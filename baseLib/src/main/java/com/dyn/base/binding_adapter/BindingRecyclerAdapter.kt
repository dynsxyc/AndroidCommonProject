package com.dyn.base.binding_adapter

import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.view.MotionEvent
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.blankj.utilcode.util.ConvertUtils.dp2px
import com.chad.library.adapter.base.BaseQuickAdapter
import com.dyn.base.ui.base.recycler.BaseRecyclerAdapter
import com.dyn.base.ui.recyclerdivider.GridCustomDividerItemDecoration
import com.dyn.base.ui.recyclerdivider.GridOffsetsItemDecoration
import com.dyn.base.ui.recyclerdivider.LinearDividerItemDecoration
import com.dyn.base.ui.recyclerdivider.LinearOffsetsItemDecoration

object BindingRecyclerAdapter {
//    @BindingAdapter(
//        value = ["adapter"],
//        requireAll = false
//    )
//    @JvmStatic
//    fun initRecyclerAdapter(
//        view: RecyclerView,
//        adapter: RecyclerView.Adapter<*>?
//    ) {
//        if (view.adapter != adapter && adapter != null) {
//            view.adapter = adapter
//        }
//    }
//
//    @BindingAdapter(
//        value = ["childAttachStateChangeListener"],
//        requireAll = false
//    )
//    @JvmStatic
//    fun childAttachStateChangeListener(
//        view: RecyclerView,
//        adapter: BaseQuickAdapter<*, *>?
//    ) {
//        view.addOnChildAttachStateChangeListener(object :
//            RecyclerView.OnChildAttachStateChangeListener {
//            override fun onChildViewAttachedToWindow(view: View) {
//            }
//
//            override fun onChildViewDetachedFromWindow(view: View) {
//            }
//
//        })
//        view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                Log.i("changed->", "onScrollStateChanged")
//            }
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                Log.i("changed->", "onScrolled")
//            }
//        })
//    }

    @BindingAdapter(
        value = ["itemData","itemDataNotify"],
        requireAll = false
    )
    @JvmStatic
    fun initRecyclerData(
        view: RecyclerView,
        itemData: List<Nothing>?,
        itemDataNotify:Boolean? = false
    ) {
        when (view.adapter) {
            is BaseQuickAdapter<*, *> -> {
                (view.adapter as BaseQuickAdapter<*, *>).submitList(itemData)
                if (itemDataNotify == true){
                    (view.adapter as BaseQuickAdapter<*, *>).notifyDataSetChanged()
                }
            }
        }
    }
    @BindingAdapter(
        value = ["itemRestData"],
        requireAll = false
    )
    @JvmStatic
    fun initBaseRecyclerData(
        view: RecyclerView,
        itemData: List<Nothing>?
    ) {
        when (val adapter = view.adapter) {
            is BaseQuickAdapter<*, *> -> {
                adapter.submitList(itemData)
                adapter.notifyDataSetChanged()
            }
        }
    }

    @BindingAdapter(
        value = ["addMoreData"],
        requireAll = false
    )
    @JvmStatic
    fun addMoreRecyclerData(
        view: RecyclerView,
        itemData: Collection<Nothing>?
    ) {
        when (view.adapter) {
            is BaseQuickAdapter<*, *> -> {
                if (itemData?.isNotEmpty() == true) {
                    (view.adapter as BaseQuickAdapter<*, *>).addAll(itemData)
                }
            }
        }
    }

    @BindingAdapter(
        value = ["insertData", "insertDataByPosition"],
        requireAll = false
    )
    @JvmStatic
    fun <T : Any> insertData(
        view: RecyclerView,
        itemData: T?,
        insertDataByPosition: Int,
    ) {
        when (val adapter = view.adapter) {
            is BaseQuickAdapter<*, *> -> {
                itemData?.let {
                    val size = adapter.itemCount
                    if (insertDataByPosition in 0 until size) {
                        (view.adapter as BaseQuickAdapter<T, *>).add(insertDataByPosition, it)
                    } else {
                        (view.adapter as BaseQuickAdapter<T, *>).add(it)

                    }
                }
            }
        }
    }
//    @BindingAdapter(
//        value = ["addSingleData","addSingleDataToPosition"]
//    )
//    @JvmStatic
//    fun addMoreRecyclerData(
//        view: RecyclerView,
//        data: Any?,
//        position:Int
//    ) {
//        val adapter = view.adapter
//        if (adapter is BaseQuickAdapter<*,*>){
//            if (data != null && adapter.items.contains(data).not()) {
//                adapter.add(position, data)
//            }
//        }
//    }

    @BindingAdapter(
        value = ["addMoreDataAndNotify"],
        requireAll = false
    )
    @JvmStatic
    fun addMoreDataAndNotify(
        view: RecyclerView,
        itemData: Collection<Nothing>?
    ) {
        when (view.adapter) {
            is BaseQuickAdapter<*, *> -> {
                itemData?.let { list ->
                    (view.adapter as BaseQuickAdapter<*, *>).let {
                        try {
                            it.addAll(list)
                            it.notifyDataSetChanged()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    @BindingAdapter(
        value = ["removeItemByData"],
        requireAll = false
    )
    @JvmStatic
    fun <T : Any> removeItemByData(
        view: RecyclerView,
        removeData: T?
    ) {
        view.adapter?.let { adapter ->
            if (adapter is BaseQuickAdapter<*, *>) {
                removeData?.let {
                    (adapter as BaseQuickAdapter<T, *>).remove(removeData)
                }
            }
        }
    }

    @BindingAdapter(
        value = ["notifyItemByPosition"],
        requireAll = false
    )
    @JvmStatic
    fun notifyItemByPosition(
        view: RecyclerView,
        position: Int?
    ) {
        view.adapter?.let { adapter ->
            if (position != null && position != -1) {
                adapter.notifyItemChanged(position)
            }
        }
    }

    @BindingAdapter(
        value = ["notifyAll"],
        requireAll = false
    )
    @JvmStatic
    fun notifyAll(
        view: RecyclerView,
        isNotify: Boolean?
    ) {
        view.adapter?.let { adapter ->
            if (isNotify != null && isNotify) {
                adapter.notifyDataSetChanged()
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
            recyclerView.removeItemDecoration(itemDecoration)
            recyclerView.addItemDecoration(itemDecoration)
        } else {
            for (i in 0 until recyclerView.itemDecorationCount) {
                recyclerView.removeItemDecorationAt(i)
            }
        }
    }

    @BindingAdapter(value = ["onScrolled"], requireAll = false)
    @JvmStatic
    fun onScrolled(recyclerView: RecyclerView, scrolled: Boolean? = false) {
//        recyclerView.addOnScrollListener(object :OnScrollListener(){
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                Logger.i("onScrolled->")
//                if (!recyclerView.canScrollVertically(1)) {
//                    //滑动到底部
//                    recyclerView.parent.requestDisallowInterceptTouchEvent(false)
//                }
//                if (!recyclerView.canScrollVertically(-1)) {
//                    //滑动到顶部
//                    recyclerView.parent.requestDisallowInterceptTouchEvent(false)
//                }
//            }
//        })
        recyclerView.addOnItemTouchListener(object : OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_MOVE -> rv.parent.requestDisallowInterceptTouchEvent(
                        true
                    )
                }
                return false
            }


            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

            }

        })
    }

    /**
     * 线性offset分割线
     * */
    @BindingAdapter(
        value = ["linerOffsetDecorationSize", "linerOffsetDecorationOrientation", "linerOffsetDecorationHasEdge", "linerOffsetDecorationFirstTopSize", "linerOffsetDecorationHasFirstTop", "linerOffsetDecorationHasLast"],
        requireAll = false
    )
    @JvmStatic
    fun itemLinerDecoration(
        recyclerView: RecyclerView,
        width: Float,
        orientation: Int = LinearOffsetsItemDecoration.LINEAR_OFFSETS_VERTICAL,
        linerOffsetDecorationHasEdge: Boolean = false,
        firstTop: Float,
        hasFirstTop: Boolean? = false,
        hasLast: Boolean? = false
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
        itemDecoration.setFirstTopOffset(hasFirstTop?:false)
        itemDecoration.setOffsetLast(hasLast?:false)
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
        recyclerView.scrollToPosition(position)
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

    @BindingAdapter(
        value = ["addItemDataAndScrollBottom"],
        requireAll = false
    )
    @JvmStatic
    fun addItemDataAndScrollBottom(
        view: RecyclerView,
        itemData: Any?
    ) {
        when (val adapter = view.adapter) {
            is BaseQuickAdapter<*, *> -> {
                if (itemData != null) {
                    (adapter as BaseQuickAdapter<Any,*>).add(itemData!!)
                    val layoutManager = view.layoutManager
                    if (layoutManager is LinearLayoutManager){
                        val lastPosition = layoutManager.findLastVisibleItemPosition()
                        if (lastPosition == (layoutManager.itemCount-2)){
                            view.smoothScrollToPosition(adapter.itemCount)
                        }
                    }
                }
            }
        }
    }
}