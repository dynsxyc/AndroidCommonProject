package com.zhenpinji.lib.base.utils.recyclerdivider

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * 九宫格样式
 * */
class GridSpacingItemNotBothDecoration constructor(spanCount: Int, spacing: Int, includeEdge: Boolean, isRemoveBoth: Boolean):RecyclerView.ItemDecoration(){

    private var spanCount = 0
    private var spacing = 0
    private var includeEdge = false
    private var isRemoveBoth = false

    init {
        this.spanCount = spanCount
        this.spacing = spacing
        this.includeEdge = includeEdge
        this.isRemoveBoth = isRemoveBoth
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view!!) // item position
        val column = position % spanCount // item column
        if (includeEdge) {
            if (isRemoveBoth) { // 去掉两边的spacing
                outRect.left = if (column == 0) 0 else spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = if (column == spanCount - 1) 0 else (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
            } else {
                outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
            }
            if (position < spanCount) { // top edge
                outRect.top = spacing
            }
            outRect.bottom = spacing // item bottom
        } else {
            outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing // item top
            }
        }
    }
}