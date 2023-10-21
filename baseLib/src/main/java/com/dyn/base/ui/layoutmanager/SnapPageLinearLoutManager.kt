package com.dyn.base.ui.layoutmanager

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * */
class SnapPageLinearLoutManager(context: Context,
                                @RecyclerView.Orientation orientation:Int, reverseLayout:Boolean) : LinearLayoutManager(context,  orientation, reverseLayout) {


    private val  mLinearSnapHelper: StartLinearSnapHelper =StartLinearSnapHelper()
    private var mOnSelectedViewListener: OnSnapSelectedViewListener? = null

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        mLinearSnapHelper.attachToRecyclerView(view)
    }

    /**
     * 当滑动停止时触发回调
     * @param state
     */
    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            if (mOnSelectedViewListener != null && mLinearSnapHelper != null) {
                val view = mLinearSnapHelper.findSnapView(this)
                view?.let {
                    val position = getPosition(view)
                    mOnSelectedViewListener?.onSelectedView(view, position)
                }
            }
        }
    }

    fun setOnSelectedViewListener(listener: OnSnapSelectedViewListener) {
        this.mOnSelectedViewListener = listener
    }

}