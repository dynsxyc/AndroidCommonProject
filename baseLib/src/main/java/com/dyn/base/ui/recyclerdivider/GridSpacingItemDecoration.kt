package com.zhenpinji.lib.base.utils.recyclerdivider

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.ConvertUtils

class GridSpacingItemDecoration private constructor(builder: Builder) : RecyclerView.ItemDecoration() {

    companion object {
        /**
         * 包含中间和最后一行bottom  并且bottom spacing=内行间距高度的样式
         * */
        fun createHasBottomSpacing(spacingDp: Int): GridSpacingItemDecoration {
            return createHasBottomSpacing(spacingDp,hasLastRowBottomSpacing = true)
        }
        fun createHasBottomSpacing(spacingDp: Int,hasLastRowBottomSpacing:Boolean): GridSpacingItemDecoration {
            return create(spacingDp,hasLastRowBottomSpacing = hasLastRowBottomSpacing)
        }
        /**
         * 包含中间和最后一行bottom  自定义bottom spacing 高度的样式
         * */
        fun createHasBottomSpacing(spacingDp: Int,bottomSpacing:Int): GridSpacingItemDecoration {
            return create(spacingDp,hasLastRowBottomSpacing = true,lastRowBottomSpacing = bottomSpacing)
        }
        /**
         * 包含中间和最后一行bottom  自定义bottom spacing 高度的样式
         * */
        fun createHasTopBottomSpacing(spacingDp: Int,bottomSpacing:Int,topSpacing:Int): GridSpacingItemDecoration {
            return create(spacingDp,hasLastRowBottomSpacing = true,lastRowBottomSpacing = bottomSpacing,hasFirstRowTopSpacing = true,firstRowTopSpacing = topSpacing)
        }

        /**
         * 只有中间有Spacing 的情况
         * */
        fun createNoEdgeSpacing(spacingDp: Int): GridSpacingItemDecoration {
            return create(spacingDp)
        }

        fun create(spacingDp: Int,firstRowTopSpacing:Int = spacingDp,lastRowBottomSpacing:Int = spacingDp,isEdge:Boolean = false,hasFirstRowTopSpacing:Boolean = false,hasLastRowBottomSpacing: Boolean = false): GridSpacingItemDecoration {
            return Builder().setSpacing(spacingDp)
                    .setFirstTopSpacing(firstRowTopSpacing)
                    .setLastBottomSpacing(lastRowBottomSpacing)
                    .setIncludeEdge(isEdge)
                    .setHasLastRowBottomSpacing(hasLastRowBottomSpacing)
                    .setHasFirstRowRopSpacing(hasFirstRowTopSpacing)
                    .create()
        }
    }

    private var spacing = 0
    /**
     * 是否包含左右边缘
     * */
    private var includeEdge = false
    /**
     * 第一行是否包含上边缘
     * */
    private var hasFirstRowTopSpacing = false
    /**
     * 最后一行是否包含下边缘
     * */
    private var hasLastRowBottomSpacing = false

    private var firstTopSpacing = 0

    private var lastBottomSpacing = 0

    private class Builder {
        var spacing: Int = 0
        var includeEdge = false
        var hasFirstRowTopSpacing = false
        var hasLastRowBottomSpacing = false
        var firstTopSpacing = 0
        var lastBottomSpacing = 0

        fun setSpacing(value: Int): Builder {
            firstTopSpacing = value
            lastBottomSpacing = value
            spacing = value
            return this
        }

        fun setIncludeEdge(value: Boolean): Builder {
            includeEdge = value
            return this
        }

        fun setHasFirstRowRopSpacing(value: Boolean): Builder {
            hasFirstRowTopSpacing = value
            return this
        }

        fun setHasLastRowBottomSpacing(value: Boolean): Builder {
            hasLastRowBottomSpacing = value
            return this
        }

        fun setFirstTopSpacing(value: Int): Builder {
            firstTopSpacing = value
            return this
        }

        fun setLastBottomSpacing(value: Int): Builder {
            lastBottomSpacing = value
            return this
        }

        fun create(): GridSpacingItemDecoration {
            return GridSpacingItemDecoration(this)
        }
    }

    init {
        this.spacing = ConvertUtils.dp2px(builder.spacing.toFloat())
        this.includeEdge = builder.includeEdge
        this.hasFirstRowTopSpacing = builder.hasFirstRowTopSpacing
        this.hasLastRowBottomSpacing = builder.hasLastRowBottomSpacing
        this.firstTopSpacing = ConvertUtils.dp2px(builder.firstTopSpacing.toFloat())
        this.lastBottomSpacing = ConvertUtils.dp2px(builder.lastBottomSpacing.toFloat())
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val spanCount = getSpanCount(parent)
        val position = parent.getChildAdapterPosition(view)
        val childCount = parent.adapter!!.itemCount
        val column = position % spanCount
        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount
            outRect.right = (column + 1) * spacing / spanCount
            if (position < spanCount && hasFirstRowTopSpacing) {
                outRect.top = firstTopSpacing
            }
            if (isLastRow(position, spanCount, childCount)) {
                if (hasLastRowBottomSpacing) {
                    outRect.bottom = lastBottomSpacing
                } else {
                    outRect.bottom = 0
                }
            } else {
                outRect.bottom = spacing
            }
        } else {
            outRect.left = column * spacing / spanCount
            outRect.right = spacing - (column + 1) * spacing / spanCount
            if (position < spanCount && hasFirstRowTopSpacing) {
                //只对第一行做topSpacing
                outRect.top = firstTopSpacing
            }
            if (isLastRow(position, spanCount, childCount)) {
                if (hasLastRowBottomSpacing) {
                    outRect.bottom = lastBottomSpacing
                } else {
                    outRect.bottom = 0
                }
            } else {
                outRect.bottom = spacing
            }
        }
    }


    private fun isLastRow(position: Int, spanCount: Int, childCount: Int): Boolean {
        var lastColumnCount = childCount % spanCount
        lastColumnCount = if (lastColumnCount == 0) spanCount else lastColumnCount
        return position >= childCount - lastColumnCount
    }

    private fun getSpanCount(parent: RecyclerView): Int {
        return when (val layoutManager = parent.layoutManager) {
            is GridLayoutManager -> layoutManager.spanCount
            is StaggeredGridLayoutManager -> layoutManager.spanCount
            else -> throw UnsupportedOperationException("the GridDividerItemDecoration can only be used in " + "the RecyclerView which use a GridLayoutManager or StaggeredGridLayoutManager")
        }
    }
}