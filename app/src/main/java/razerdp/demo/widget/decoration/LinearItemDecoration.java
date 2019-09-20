package razerdp.demo.widget.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import razerdp.demo.utils.UIHelper;


/**
 * Created by 大灯泡 on 2019/6/24
 * <p>
 * Description：
 */
public class LinearItemDecoration extends RecyclerView.ItemDecoration {
    private static final int DEFAULT_SIZE = UIHelper.dip2px(0.5f);
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private static final String TAG = "DividerItem";
    private Drawable mDivider;
    private int mOrientation;
    private final Rect mBounds = new Rect();
    private final Rect mPaddingBounds = new Rect();
    private int mDividerSize = DEFAULT_SIZE;

    public LinearItemDecoration() {
        this(DEFAULT_SIZE, Color.parseColor("#e0e0e0"));
    }

    public LinearItemDecoration(int color) {
        this(DEFAULT_SIZE, color);
    }

    public LinearItemDecoration(int dividerSize, int color) {
        this(VERTICAL, dividerSize, color);
    }

    public LinearItemDecoration(int orientation, int dividerSize, int color) {
        this(orientation, color, dividerSize, 0, 0, 0, 0);
    }

    public LinearItemDecoration(int orientation, int color, int dividerSize, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        this.mDivider = new ColorDrawable(color);
        this.setOrientation(orientation);
        this.mDividerSize = dividerSize;
        mPaddingBounds.set(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    public void setOrientation(int orientation) {
        if (orientation != 0 && orientation != 1) {
            throw new IllegalArgumentException("Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        } else {
            this.mOrientation = orientation;
        }
    }

    public LinearItemDecoration setDividerSize(int size) {
        this.mDividerSize = size;
        return this;
    }

    public void setDrawable(@NonNull Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Drawable cannot be null.");
        } else {
            this.mDivider = drawable;
        }
    }

    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() != null && this.mDivider != null) {
            if (this.mOrientation == 1) {
                this.drawVertical(c, parent);
            } else {
                this.drawHorizontal(c, parent);
            }

        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        int left;
        int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft() + mPaddingBounds.left;
            right = parent.getWidth() - parent.getPaddingRight() - mPaddingBounds.right;
            canvas.clipRect(left, parent.getPaddingTop() + mPaddingBounds.top, right, parent.getHeight() - parent.getPaddingBottom() - mPaddingBounds.bottom);
        } else {
            left = mPaddingBounds.left;
            right = parent.getWidth() - mPaddingBounds.right;
        }

        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; ++i) {
            View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, this.mBounds);
            int bottom = this.mBounds.bottom + Math.round(child.getTranslationY()) - mPaddingBounds.bottom;
            int top = bottom - mDividerSize + mPaddingBounds.top;
            this.mDivider.setBounds(left, top, right, bottom);
            this.mDivider.draw(canvas);
        }

        canvas.restore();
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        int top;
        int bottom;
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop() + mPaddingBounds.top;
            bottom = parent.getHeight() - parent.getPaddingBottom() - mPaddingBounds.bottom;
            canvas.clipRect(parent.getPaddingLeft() + mPaddingBounds.left,
                    top,
                    parent.getWidth() - parent.getPaddingRight() - mPaddingBounds.right,
                    bottom);
        } else {
            top = mPaddingBounds.top;
            bottom = parent.getHeight() - mPaddingBounds.bottom;
        }

        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; ++i) {
            View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, this.mBounds);
            int right = this.mBounds.right + Math.round(child.getTranslationX()) - mPaddingBounds.right;
            int left = right - mDividerSize + mPaddingBounds.left;
            this.mDivider.setBounds(left, top, right, bottom);
            this.mDivider.draw(canvas);
        }

        canvas.restore();
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (this.mDivider == null) {
            outRect.set(0, 0, 0, 0);
        } else {
            if (this.mOrientation == 1) {
                outRect.set(0, 0, 0, this.mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, this.mDivider.getIntrinsicWidth(), 0);
            }

        }
    }
}
