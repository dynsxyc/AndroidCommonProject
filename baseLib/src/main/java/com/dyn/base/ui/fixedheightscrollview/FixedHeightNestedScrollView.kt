package com.dyn.base.ui.fixedheightscrollview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.dyn.base.R

/**
 * 固定高度的ScrollView 适应滑动到顶部停靠的需求
 *
 * */
class FixedHeightNestedScrollView constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : NestedScrollView(context, attributeSet, defStyleAttr) {
    private val LOG_TAG = "NestedScrollView"

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    private var mTopOffset: Int
    private var mContentTopMargin:Int

    init {
        val a = context.obtainStyledAttributes(attributeSet, R.styleable.FixedHeightNestedScrollView)
        mTopOffset = a.getDimension(R.styleable.FixedHeightNestedScrollView_topOffset, 0f).toInt()
        mContentTopMargin = a.getDimension(R.styleable.FixedHeightNestedScrollView_contentTopMargin, 0f).toInt()
        a.recycle()
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        Log.i(LOG_TAG, "MyNestedScrollView  父控件   onNestedScrollAccepted child = ${child.javaClass} target = ${target.javaClass} ")
        super.onNestedScrollAccepted(child, target, axes, type)

    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        Log.i(LOG_TAG, "MyNestedScrollView  父控件  onNestedScrollAccepted ")
        super.onNestedScrollAccepted(child, target, axes)
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        val result = super.onStartNestedScroll(child, target, axes, type)
        Log.i(LOG_TAG, "MyNestedScrollView  父控件  onStartNestedScroll return = $result")
        return result
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int): Boolean {
        val result = super.onStartNestedScroll(child, target, axes)
        Log.i(LOG_TAG, "MyNestedScrollView  父控件  onStartNestedScroll return = $result")
        return result
    }

    override fun onNestedFling(target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        Log.i(LOG_TAG, "MyNestedScrollView  父控件   onNestedFling consumed = $consumed")
        return super.onNestedFling(target, velocityX, velocityY, consumed)
    }

    override fun getNestedScrollAxes(): Int {
        val result = super.getNestedScrollAxes()
        Log.i(LOG_TAG, "MyNestedScrollView  父控件  getNestedScrollAxes return $result")
        return result
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        //子View滑动之前 先掉用父亲
        // 向上滑动。若当前topview可见，需要将topview滑动至不可见
        val hideTop = dy > 0 && scrollY < (topView!!.measuredHeight - mTopOffset - mContentTopMargin)
        Log.i(LOG_TAG, "MyNestedScrollView  父控件  onNestedPreScroll  --1-- ${target.javaClass} hideTop = $hideTop  scrollY = $scrollY  比较= ${ topView!!.measuredHeight - mTopOffset - mContentTopMargin}")
        if (hideTop) {
            scrollBy(0, dy)
            consumed[1] = dy
        }
    }

//    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
//        val hideTop = dy > 0 && scrollY < (topView!!.measuredHeight - mTopOffset - mContentTopMargin)
//        Log.i(LOG_TAG, "MyNestedScrollView  父控件  onNestedPreScroll ---2---  ${target.javaClass} hideTop = $hideTop  scrollY = $scrollY  比较= ${ topView!!.measuredHeight - mTopOffset - mContentTopMargin}")
//        if (hideTop) {
//            scrollBy(0, dy)
//            consumed[1] = dy
//        }
//    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        Log.i(LOG_TAG, "MyNestedScrollView  父控件   onNestedScroll ${target.javaClass}")
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        Log.i(LOG_TAG, "MyNestedScrollView  父控件   onNestedScroll ${target.javaClass}")
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        Log.i(LOG_TAG, "MyNestedScrollView  父控件   onNestedPreFling ${target.javaClass}")
        return super.onNestedPreFling(target, velocityX, velocityY)
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        Log.i(LOG_TAG, "MyNestedScrollView  父控件   onStopNestedScroll ${target.javaClass}")
        super.onStopNestedScroll(target, type)
    }

    override fun onStopNestedScroll(target: View) {
        Log.i(LOG_TAG, "MyNestedScrollView  父控件   onStopNestedScroll ${target.javaClass}")
        super.onStopNestedScroll(target)
    }

    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, offsetInWindow: IntArray?, type: Int): Boolean {
        val boolean = super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type)
        Log.d(LOG_TAG, "MyNestedScrollView  子空间  dispatchNestedScroll has type return = $boolean")
        return boolean
    }

    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, offsetInWindow: IntArray?): Boolean {
        val boolean = super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow)
        Log.d(LOG_TAG, "MyNestedScrollView  子空间  dispatchNestedScroll return = $boolean")
        return boolean
    }

    override fun isNestedScrollingEnabled(): Boolean {
        val result = super.isNestedScrollingEnabled()
        Log.d(LOG_TAG, "MyNestedScrollView  子空间  isNestedScrollingEnabled return = $result")
        return result
    }

    override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?, type: Int): Boolean {
        //将孩子的事件分发给父亲
        val result = super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)
        Log.d(LOG_TAG, "MyNestedScrollView  子空间 孩子分发给父亲 dispatchNestedPreScroll return = $result")
        return result
    }

    override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?): Boolean {
        val result = super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
        Log.d(LOG_TAG, "MyNestedScrollView  子空间  dispatchNestedPreScroll return = $result")
        return result
    }

    override fun stopNestedScroll(type: Int) {
        Log.d(LOG_TAG, "MyNestedScrollView  子空间 停止滑动 stopNestedScroll has type = $type")
        super.stopNestedScroll(type)
    }

    override fun stopNestedScroll() {
        Log.d(LOG_TAG, "MyNestedScrollView  子空间 停止滑动 stopNestedScroll")
        super.stopNestedScroll()
    }

    override fun hasNestedScrollingParent(type: Int): Boolean {
        //检查孩子是否存在可以嵌套滚动的父亲
        val has = super.hasNestedScrollingParent(type)
        Log.d(LOG_TAG, "MyNestedScrollView  子空间 检查自己是否存在可以嵌套滚动的父亲 hasNestedScrollingParent = $has ")
        return has
    }

    override fun hasNestedScrollingParent(): Boolean {
        val result = super.hasNestedScrollingParent()
        Log.d(LOG_TAG, "MyNestedScrollView  子空间  hasNestedScrollingParent return = $result")
        return result
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        val result = super.dispatchNestedPreFling(velocityX, velocityY)
        Log.d(LOG_TAG, "MyNestedScrollView  子空间  dispatchNestedPreFling return = $result")
        return result
    }

    override fun setNestedScrollingEnabled(enabled: Boolean) {
        Log.d(LOG_TAG, "MyNestedScrollView  子空间  setNestedScrollingEnabled  enable = $enabled")
        super.setNestedScrollingEnabled(enabled)
    }

    override fun dispatchNestedFling(velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        val result = super.dispatchNestedFling(velocityX, velocityY, consumed)
        Log.d(LOG_TAG, "MyNestedScrollView  子空间  dispatchNestedFling return = $result")
        return result
    }

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        val result = super.startNestedScroll(axes, type)
        Log.d(LOG_TAG, "MyNestedScrollView  子空间 开始滑动 startNestedScroll return = $result")
        return result
    }

//    override fun startNestedScroll(axes: Int): Boolean {
//        val result = super.startNestedScroll(axes)
//        Log.d(LOG_TAG, "MyNestedScrollView  子空间 开始滑动 startNestedScroll return = $result")
//        return result
//    }

    var mFlingHelper: FlingHelper? = null

    //滑动的总长度
    var totalDy = 0

    /**
     * 用于判断RecyclerView是否在fling
     */
    var isStartFling = false
    private var velocityY: Int = 0
    private var isMeasure = false
    private var topView: View? = null
    private var contentView: ViewGroup? = null
    private var mChildRecyclerView: RecyclerView? = null
//    var mChildSmartRefreshLayout: SmartRefreshLayout? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        topView = (getChildAt(0) as ViewGroup).getChildAt(0)
        contentView = (getChildAt(0) as ViewGroup).getChildAt(1) as ViewGroup?
        mChildRecyclerView = getChildRecyclerView(contentView)
//        mChildSmartRefreshLayout = getChildRefreshLayout(contentView)
    }

    private fun setTopOffset(offset: Int) {
        mTopOffset = offset
        val lp = contentView?.layoutParams
        lp?.height = measuredHeight - offset
        contentView?.layoutParams = lp
        Log.i(LOG_TAG, "onMeasure setTopOffset measuredHeight = $measuredHeight offset = $offset")
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 调整contentView的高度为父容器高度，使之填充布局，避免父容器滚动后出现空白
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!isMeasure || contentView?.measuredHeight != measuredHeight - mTopOffset){
            setTopOffset(mTopOffset)
            isMeasure = true
        }
        Log.i(LOG_TAG, "onMeasure scrollViewHeight = $measuredHeight contentViewHeight = ${contentView?.measuredHeight}  topViewHeight = ${topView?.measuredHeight} mTopOffset = $mTopOffset")
    }

    override fun fling(velocityY: Int) {
        super.fling(velocityY)
        if (velocityY <= 0) {
            this.velocityY = 0
        } else {
            isStartFling = true
            this.velocityY = velocityY
        }
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldScrollY: Int) {
        super.onScrollChanged(l, t, oldl, oldScrollY)
//        Log.w(LOG_TAG, "MyNestedScrollView  onScrollChanged scrollX = $l scrollY = $t oldScrollX = $oldl oldScrollY = $oldScrollY")
        if (isStartFling) {
            totalDy = 0
            isStartFling = false
        }
        //监听滑动
        mOnRefreshEnableListener?.let {
            it.onScrollChanged(l, t, oldl, oldScrollY)

        }
        mOnRefreshEnableListener?.let {
            postDelayed({
                it.onEnable(scrollY == 0)
            }, 300)

        }
        val scrollViewHeight = measuredHeight
//        Log.w(LOG_TAG, "MyNestedScrollView  自己在滑动 scrollY = $scrollY childAt0.measureHeight = ${getChildAt(0).measuredHeight} scrollViewHeight = $scrollViewHeight")
        if (scrollY == getChildAt(0).measuredHeight - scrollViewHeight) {
            dispatchChildFling()
        }
        //在RecyclerView fling情况下，记录当前RecyclerView在y轴的偏移
        totalDy += scrollY - oldScrollY
    }

    init {
        mFlingHelper = FlingHelper(context)
    }

    private fun dispatchChildFling() {
        if (velocityY !== 0) {
            val splineFlingDistance: Double = mFlingHelper?.getSplineFlingDistance(velocityY)
                    ?: 0.toDouble()
            if (splineFlingDistance > totalDy) {
                childFling(mFlingHelper?.getVelocityByDistance(splineFlingDistance - totalDy.toDouble())
                        ?: 0)
            }
        }
        totalDy = 0
        velocityY = 0
    }

    private fun childFling(velY: Int = 0) {
        mChildRecyclerView?.apply {
            Log.i(LOG_TAG, "MyNestedScrollView  --------------------------fling")
            this.fling(0, velY)
        }
    }

    private fun getChildRecyclerView(viewGroup: ViewGroup?): RecyclerView? {
        viewGroup?.let {
            for (i in 0 until viewGroup.childCount) {
                val view = viewGroup.getChildAt(i)
                if (view is RecyclerView && view.javaClass == RecyclerView::class.java && view.id === R.id.FixedHeightNestedRecyclerView) {
                    return view
                } else if (view is ViewGroup) {
                    val childRecyclerView: ViewGroup? = getChildRecyclerView(view)
                    if (childRecyclerView is RecyclerView) {
                        return childRecyclerView
                    }
                }
                continue
            }
        }
        return null
    }

    var mOnRefreshEnableListener: OnRefreshEnableListener? = null

    interface OnRefreshEnableListener {
        fun onEnable(isEnable: Boolean)
        fun onScrollChanged(l: Int, t: Int, oldl: Int, oldScrollY: Int)
    }
}