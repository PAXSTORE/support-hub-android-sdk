package com.pax.android.supporthub.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 原生流式布局：固定子项宽度，自动换行
 */
public class FlowLayout extends ViewGroup {
    private int mColumns = 2;               // 每行个数
    private int mSpacing = dp2px(8);        // 行列间距
    private float mChildWidth = 0;          // 计算后的子项宽度

    public FlowLayout(Context context, int columns) {
        this(context, null);
        mColumns = columns;
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setColumns(int columns) {
        mColumns = columns;
        requestLayout();
    }

    public void setSpacing(int spacingDp) {
        mSpacing = dp2px(spacingDp);
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int usable = widthSize - getPaddingLeft() - getPaddingRight();
        mChildWidth = (usable - (mColumns - 1) * mSpacing) * 1.0f / mColumns;

        int x = getPaddingLeft(), y = getPaddingTop(), rowH = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) continue;

            int childWidthSpec = MeasureSpec.makeMeasureSpec((int) mChildWidth, MeasureSpec.EXACTLY);
            int childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            measureChild(child, childWidthSpec, childHeightSpec);

            int col = i % mColumns;
            if (col == 0 && i != 0) {               // 换行
                x = getPaddingLeft();
                y += rowH + mSpacing;
                rowH = 0;
            }
            rowH = Math.max(rowH, child.getMeasuredHeight());
            x += (int) (mChildWidth + mSpacing);
        }
        setMeasuredDimension(widthSize,
                y + rowH + getPaddingBottom());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int x = getPaddingLeft(), y = getPaddingTop(), rowH = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) continue;

            int col = i % mColumns;
            if (col == 0 && i != 0) {               // 换行
                x = getPaddingLeft();
                y += rowH + mSpacing;
                rowH = 0;
            }
            int left = x;
            int top = y;
            int right = left + (int) mChildWidth;
            int bottom = top + child.getMeasuredHeight();
            child.layout(left, top, right, bottom);

            x += (int) (mChildWidth + mSpacing);
            rowH = Math.max(rowH, child.getMeasuredHeight());
        }
    }

    private int dp2px(float dp) {
        return (int) (getResources().getDisplayMetrics().density * dp + 0.5f);
    }
}