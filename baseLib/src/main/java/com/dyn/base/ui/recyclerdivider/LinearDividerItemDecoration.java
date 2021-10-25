package com.dyn.base.ui.recyclerdivider;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This class can only be used in the RecyclerView which use a LinearLayoutManager or
 * its subclass.
 * blog https://github.com/dinuscxj/RecyclerItemDecoration
 */
public class LinearDividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public static final int LINEAR_DIVIDER_HORIZONTAL = LinearLayoutManager.HORIZONTAL;
    public static final int LINEAR_DIVIDER_VERTICAL = LinearLayoutManager.VERTICAL;

    private final SparseIntArray mDividerOffsets = new SparseIntArray();
    private final SparseArray<DrawableCreator> mTypeDrawableFactories = new SparseArray<>();

    @IntDef({
            LINEAR_DIVIDER_HORIZONTAL,
            LINEAR_DIVIDER_VERTICAL
    })
    @Retention(RetentionPolicy.SOURCE)
    private @interface Orientation {
    }

    @Orientation
    private int mOrientation = LINEAR_DIVIDER_VERTICAL;
    private Drawable mDivider;
    /**最后一条是否显示分割线  默认显示
     * true 不显示 false 显示
     * */
    private boolean isLastNoHasLine;
    private int mLeftOffsets,mRightOffsets;

    public void setLeftOffsets(int mLeftOffsets) {
        this.mLeftOffsets = mLeftOffsets;
    }

    public void setRightOffsets(int mRightOffsets) {
        this.mRightOffsets = mRightOffsets;
    }

    public LinearDividerItemDecoration(Context context) {
        this(context,LINEAR_DIVIDER_VERTICAL);
    }
    public LinearDividerItemDecoration(Context context,@Orientation int orientation) {
        this(context,orientation,false);
    }

    public LinearDividerItemDecoration(Context context, @Orientation int orientation, boolean isLastNoHasLine) {
        resolveDivider(context);
        setOrientation(orientation);
        this.isLastNoHasLine = isLastNoHasLine;
    }

    public void setLastNoHasLine(boolean isLastNoHasLine){
        this.isLastNoHasLine = isLastNoHasLine;
    }

    private void resolveDivider(Context context) {
        if (context != null) {
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            mDivider = a.getDrawable(0);
            a.recycle();
        }
    }

    public void setOrientation(@Orientation int orientation) {
        mOrientation = orientation;
    }

    public void setDivider(Drawable divider) {
        this.mDivider = divider;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == LINEAR_DIVIDER_VERTICAL) {
            drawVerticalDividers(c, parent);
        } else {
            drawHorizontalDividers(c, parent);
        }
    }

    public void drawVerticalDividers(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft()+mLeftOffsets;
        final int right = parent.getWidth() - parent.getPaddingRight() - mRightOffsets;

        int childCount = parent.getChildCount();
        if (isLastNoHasLine){
            childCount = childCount-1;
        }
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final Drawable divider = getDivider(parent, params.getViewAdapterPosition());
            final int top = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
            final int bottom = top + divider.getIntrinsicHeight();

            mDividerOffsets.put(params.getViewAdapterPosition(), divider.getIntrinsicHeight());

            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    public void drawHorizontalDividers(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();


        int childCount = parent.getChildCount();
        if (isLastNoHasLine){
            childCount = childCount-1;
        }
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final Drawable divider = getDivider(parent, params.getViewAdapterPosition());
            final int left = child.getRight() + params.rightMargin + Math.round(ViewCompat.getTranslationX(child));
            final int right = left + divider.getIntrinsicHeight();

            mDividerOffsets.put(params.getViewAdapterPosition(), divider.getIntrinsicHeight());

            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int adapterPosition = parent.getChildAdapterPosition(view);
        if (adapterPosition == parent.getAdapter().getItemCount() - 1 && isLastNoHasLine) {
            return;
        }

        if (mDividerOffsets.indexOfKey(adapterPosition) < 0) {
            mDividerOffsets.put(adapterPosition, getDivider(parent, adapterPosition).getIntrinsicHeight());
        }

        if (mOrientation == LINEAR_DIVIDER_VERTICAL) {
            outRect.set(0, 0, 0, mDividerOffsets.get(parent.getChildAdapterPosition(view)));
        } else {
            outRect.set(0, 0, mDividerOffsets.get(parent.getChildAdapterPosition(view)), 0);
        }
    }

    private Drawable getDivider(RecyclerView parent, int adapterPosition) {
        final RecyclerView.Adapter adapter = parent.getAdapter();
        final int itemType = adapter.getItemViewType(adapterPosition);
        final DrawableCreator drawableCreator = mTypeDrawableFactories.get(itemType);

        if (drawableCreator != null) {
            return drawableCreator.create(parent, adapterPosition);
        }

        return mDivider;
    }

    public void registerTypeDrawable(int itemType, DrawableCreator drawableCreator) {
        mTypeDrawableFactories.put(itemType, drawableCreator);
    }

    public interface DrawableCreator {
        Drawable create(RecyclerView parent, int adapterPosition);
    }
}
