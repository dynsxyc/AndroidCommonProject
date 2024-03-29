package com.dyn.base.ui.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.blankj.utilcode.util.ColorUtils;
import com.dyn.base.R;
import com.orhanobut.logger.Logger;

import net.lucode.hackware.magicindicator.NavigatorHelper;
import net.lucode.hackware.magicindicator.abs.IPagerNavigator;
import net.lucode.hackware.magicindicator.buildins.ArgbEvaluatorHolder;
import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.ArrayList;
import java.util.List;

//                       _oo0oo_
//                      o8888888o
//                      88" . "88
//                      (| -_- |)
//                      0\  =  /0
//                    ___/`---'\___
//                  .' \\|     |// '.
//                 / \\|||  :  |||// \
//                / _||||| -:- |||||- \
//               |   | \\\  -  /// |   |
//               | \_|  ''\---/''  |_/ |
//               \  .-\__  '-'  ___/-. /
//             ___'. .'  /--.--\  `. .'___
//          ."" '<  `.___\_<|>_/___.' >' "".
//         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//         \  \ `_.   \_ __\ /__ _/   .-` /  /
//     =====`-.____`.___ \_____/___.-`___.-'=====
//                       `=---='
//
//
//     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//               佛祖保佑         永无BUG

/**
 * 类似CircleIndicator的效果
 * Created by hackware on 2016/9/3.
 */

public class RectangleNavigator extends View implements IPagerNavigator, NavigatorHelper.OnNavigatorScrollListener {
    private int mMinWidth;
    private int mMaxWidth;
    private int mMaxHeight;
    private int mRadius;
    private int mNormalCircleColor = Color.parseColor("#DBDBDB");
    private int mSelectedCircleColor = ColorUtils.getColor(R.color.colorPrimary);
    private int mCircleSpacing;
    private int mCircleCount;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private List<RectF> mCirclePoints = new ArrayList<RectF>();
    private SparseArray<Float> mCircleRadiusArray = new SparseArray<Float>();

    // 事件回调
    private boolean mTouchable;
    private OnCircleClickListener mCircleClickListener;
    private float mDownX;
    private float mDownY;
    private int mTouchSlop;

    private boolean mFollowTouch = false;    // 是否跟随手指滑动
    private NavigatorHelper mNavigatorHelper = new NavigatorHelper();
    private Interpolator mStartInterpolator = new LinearInterpolator();

    public RectangleNavigator(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMinWidth = UIUtil.dip2px(context, 15);
        mMaxWidth = UIUtil.dip2px(context, 16);
        mMaxHeight = UIUtil.dip2px(context, 2);
        mCircleSpacing = UIUtil.dip2px(context, 5);
        mNavigatorHelper.setNavigatorScrollListener(this);
        mNavigatorHelper.setSkimOver(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = width;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                if (mCircleCount <= 0) {
                    result = getPaddingLeft() + getPaddingRight();
                } else {
//                    result = (mCircleCount - 1) * mMinWidth + mMaxWidth + (mCircleCount - 1) * mCircleSpacing + getPaddingLeft() + getPaddingRight();
                    result = mCircleCount  * mMaxWidth + (mCircleCount - 1) * mCircleSpacing + getPaddingLeft() + getPaddingRight();
                }
                break;
            default:
                break;
        }
        return result;
    }

    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = height;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = mMaxHeight + getPaddingTop() + getPaddingBottom();
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0, j = mCirclePoints.size(); i < j; i++) {
            RectF point = mCirclePoints.get(i);
            float radius = mCircleRadiusArray.get(i, (float) mMinWidth);
            int color;
            float left = point.left;
            float right = point.right;
            if (radius == mMaxWidth) {
                right = left+mMaxWidth;
                color = mSelectedCircleColor;
            } else {
                left = left+(mMaxWidth-mMinWidth)/2;
                right = left+mMinWidth;
                color = mNormalCircleColor;
            }
            mPaint.setColor(color);
            canvas.drawRoundRect(left,point.top,right,point.bottom, mRadius, mRadius, mPaint);
        }
    }

    private void prepareCirclePoints() {
        mCirclePoints.clear();
        if (mCircleCount > 0) {
            int centerSpacing = mMaxWidth + mCircleSpacing;
            int startX = getPaddingLeft();
            for (int i = 0; i < mCircleCount; i++) {
                RectF pointF = new RectF(startX, 0f, startX + mMaxWidth, getHeight());
                mCirclePoints.add(pointF);
                startX += centerSpacing;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mTouchable) {
                    mDownX = x;
                    mDownY = y;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mCircleClickListener != null) {
                    if (Math.abs(x - mDownX) <= mTouchSlop && Math.abs(y - mDownY) <= mTouchSlop) {
                        float max = Float.MAX_VALUE;
                        int index = 0;
                        for (int i = 0; i < mCirclePoints.size(); i++) {
                            RectF pointF = mCirclePoints.get(i);
                            float offset = Math.abs(pointF.left - x);
                            if (offset < max) {
                                max = offset;
                                index = i;
                            }
                        }
                        mCircleClickListener.onClick(index);
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mNavigatorHelper.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        mNavigatorHelper.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mNavigatorHelper.onPageScrollStateChanged(state);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        prepareCirclePoints();
    }

    @Override
    public void notifyDataSetChanged() {
        prepareCirclePoints();
        requestLayout();
    }

    @Override
    public void onAttachToMagicIndicator() {
    }

    @Override
    public void onDetachFromMagicIndicator() {
    }

    public void setMinSize(int minWidth, int minHeight) {
        mMinWidth = minWidth;
        prepareCirclePoints();
        invalidate();
    }

    public void setRadius(int radius){
        mRadius = radius;
        invalidate();
    }

    public void setMaxSize(int maxWidth, int maxHeight) {
        mMaxWidth = maxWidth;
        mMaxHeight = maxHeight;
        prepareCirclePoints();
        invalidate();
    }

    public void setNormalCircleColor(int normalCircleColor) {
        mNormalCircleColor = normalCircleColor;
        invalidate();
    }

    public void setSelectedCircleColor(int selectedCircleColor) {
        mSelectedCircleColor = selectedCircleColor;
        invalidate();
    }

    public void setCircleSpacing(int circleSpacing) {
        mCircleSpacing = circleSpacing;
        prepareCirclePoints();
        invalidate();
    }

    public void setStartInterpolator(Interpolator startInterpolator) {
        mStartInterpolator = startInterpolator;
        if (mStartInterpolator == null) {
            mStartInterpolator = new LinearInterpolator();
        }
    }

    public void setCount(int count) {
        mCircleCount = count;  // 此处不调用invalidate，让外部调用notifyDataSetChanged
        mNavigatorHelper.setTotalCount(mCircleCount);
    }

    public void setTouchable(boolean touchable) {
        mTouchable = touchable;
    }

    public void setFollowTouch(boolean followTouch) {
        mFollowTouch = followTouch;
    }

    public void setSkimOver(boolean skimOver) {
        mNavigatorHelper.setSkimOver(skimOver);
    }

    public void setCircleClickListener(OnCircleClickListener circleClickListener) {
        if (!mTouchable) {
            mTouchable = true;
        }
        mCircleClickListener = circleClickListener;
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        if (mFollowTouch) {
            float radius = mMinWidth + (mMaxWidth - mMinWidth) * mStartInterpolator.getInterpolation(enterPercent);
            mCircleRadiusArray.put(index, radius);
            invalidate();
        }
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        if (mFollowTouch) {
            float radius = mMaxWidth + (mMinWidth - mMaxWidth) * mStartInterpolator.getInterpolation(leavePercent);
            mCircleRadiusArray.put(index, radius);
            invalidate();
        }
    }

    @Override
    public void onSelected(int index, int totalCount) {
        if (!mFollowTouch) {
            mCircleRadiusArray.put(index, (float) mMaxWidth);
            invalidate();
        }
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        if (!mFollowTouch) {
            mCircleRadiusArray.put(index, (float) mMinWidth);
            invalidate();
        }
    }

    public interface OnCircleClickListener {
        void onClick(int index);
    }
}
