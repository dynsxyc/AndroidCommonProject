package com.dyn.base.ui.recyclerdivider;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.ColorRes;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.Utils;
import com.dyn.base.R;


/**
 * Created by dyn on 2018/6/13.
 */

public class RecyclerDividerUtils {
    public static Drawable getLineHeightDrawable(float height, int color){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setSize(-1, (int) height);
        drawable.setColor(color);
        return drawable;
    }
    /**
     * 水平布局
     * */
    public static LinearDividerItemDecoration getRecyclerViewHorizontalLineDivider(int height,@ColorRes int color){
        return getRecyclerViewHorizontalLineDivider(height,color,false);
    }
    /**
     * 水平布局
     * */
    public static LinearDividerItemDecoration getRecyclerViewHorizontalLineDivider(int height, @ColorRes int color, boolean isLastNoHasLine){
        return getRecyclerViewLineDivider(height,color,LinearDividerItemDecoration.LINEAR_DIVIDER_HORIZONTAL,isLastNoHasLine,0,0);
    }

    /**
     * 垂直布局
     * */
    public static LinearDividerItemDecoration getRecyclerViewLineDivider(int height,@ColorRes int color){
        return getRecyclerViewLineDivider(height,color,LinearDividerItemDecoration.LINEAR_DIVIDER_VERTICAL,false,0,0);
    }

    /**
     * 垂直布局
     * */
    public static LinearDividerItemDecoration getRecyclerViewDefaultLineDivider(float height){
        return getRecyclerViewLineDivider(height, R.color.common_bg,LinearDividerItemDecoration.LINEAR_DIVIDER_VERTICAL,false,0,0);
    }
    /**
     * 垂直布局
     * */
    public static LinearDividerItemDecoration getRecyclerViewDefaultLineDivider(float height,boolean isLastNoHasLine){
        return getRecyclerViewLineDivider(height, R.color.common_bg,LinearDividerItemDecoration.LINEAR_DIVIDER_VERTICAL,false,0,0);
    }
    public static LinearDividerItemDecoration getRecyclerViewDefaultLineDivider(boolean isLastNoHasLine){
        return getRecyclerViewLineDivider(0.5f, R.color.common_bg,LinearDividerItemDecoration.LINEAR_DIVIDER_VERTICAL,isLastNoHasLine,12,12);
    }

    public static LinearDividerItemDecoration getRecyclerViewLineDividerOffsets(float height, @ColorRes int color, boolean isLastNoHasLine, int leftOffsets, int rightOffsets){
        return getRecyclerViewLineDivider(height,color,LinearDividerItemDecoration.LINEAR_DIVIDER_VERTICAL,isLastNoHasLine,leftOffsets,rightOffsets);
    }
    /**
     * @param height 线的高度 水平时指宽度
     * @param color 线的颜色
     * @param mOrientation 水平还是垂直的
     * @param isLastNoHasLine 最后一条是否显示分割线  默认显示  * true 不显示 false 显示
     * */
    public static LinearDividerItemDecoration getRecyclerViewLineDivider(float height, @ColorRes int color, int mOrientation, boolean isLastNoHasLine, int leftOffsets, int rightOffsets){
        height = ConvertUtils.dp2px(height);
        leftOffsets = ConvertUtils.dp2px(leftOffsets);
        rightOffsets = ConvertUtils.dp2px(rightOffsets);
        int resultColor = Utils.getApp().getResources().getColor(color);
        LinearDividerItemDecoration linearDividerItemDecoration = new LinearDividerItemDecoration(Utils.getApp(),mOrientation,isLastNoHasLine);
        linearDividerItemDecoration.setLeftOffsets(leftOffsets);
        linearDividerItemDecoration.setRightOffsets(rightOffsets);
        linearDividerItemDecoration.setDivider(getLineHeightDrawable(height,resultColor));
        return linearDividerItemDecoration;
    }

    /**
     * 九宫格边框样式
     * */
//    public static GridSpacingItemDecoration getRecyclerViewGridOffsetsDivider(int spanCount,float offsets){
//        return new GridSpacingItemDecoration(spanCount,ConvertUtils.dp2px(offsets),false);
//    }
}
