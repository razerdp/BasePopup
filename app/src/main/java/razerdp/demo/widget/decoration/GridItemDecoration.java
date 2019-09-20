package razerdp.demo.widget.decoration;

/**
 * Created by 大灯泡 on 2019/8/1
 * <p>
 * Description：grid分割线
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import razerdp.demo.base.baseadapter.HeaderViewWrapperAdapter;


public class GridItemDecoration extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private DecorationDrawOption mDrawOption;


    public GridItemDecoration(@NonNull DecorationDrawOption mDrawOption) {
        this(Color.TRANSPARENT, mDrawOption);
    }

    public GridItemDecoration(@ColorInt int colorRGB, @NonNull DecorationDrawOption mDrawOption) {
        if (mDrawOption == null) {
            throw new NullPointerException("DecorationDrawOption不能为空");
        }
        this.mDrawOption = mDrawOption;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(colorRGB);
        mPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        mDrawOption.recyclerView = parent;
        //left, top, right, bottom
        int childCount1 = parent.getChildCount();
        for (int i = 0; i < childCount1; i++) {
            View child = parent.getChildAt(i);

            int itemPosition = ((RecyclerView.LayoutParams) child.getLayoutParams()).getViewAdapterPosition();

            drawChildLeftVertical(child, itemPosition, c, parent);
            drawChildTopHorizontal(child, itemPosition, c, parent);
            drawChildRightVertical(child, itemPosition, c, parent);
            drawChildBottomHorizontal(child, itemPosition, c, parent);
        }
    }

    private void drawChildBottomHorizontal(View child, int itemPosition, Canvas c, RecyclerView parent) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                .getLayoutParams();
        int size = mDrawOption.bottomDividerSizeInternal(itemPosition);
        int left = child.getLeft() - params.leftMargin - size;
        int right = child.getRight() + params.rightMargin + size;
        int top = child.getBottom() + params.bottomMargin;
        int bottom = top + size;

        c.drawRect(left, top, right, bottom, mPaint);
    }

    private void drawChildTopHorizontal(View child, int itemPosition, Canvas c, RecyclerView parent) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                .getLayoutParams();
        int size = mDrawOption.topDividerSizeInternal(itemPosition);
        int left = child.getLeft() - params.leftMargin - size;
        int right = child.getRight() + params.rightMargin + size;
        int bottom = child.getTop() - params.topMargin;
        int top = bottom - size;

        c.drawRect(left, top, right, bottom, mPaint);
    }

    private void drawChildLeftVertical(View child, int itemPosition, Canvas c, RecyclerView parent) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                .getLayoutParams();
        int size = mDrawOption.leftDividerSizeInternal(itemPosition);
        int top = child.getTop() - params.topMargin - size;
        int bottom = child.getBottom() + params.bottomMargin + size;
        int right = child.getLeft() - params.leftMargin;
        int left = right - size;

        c.drawRect(left, top, right, bottom, mPaint);
    }

    private void drawChildRightVertical(View child, int itemPosition, Canvas c, RecyclerView parent) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                .getLayoutParams();
        int size = mDrawOption.rightDividerSizeInternal(itemPosition);
        int top = child.getTop() - params.topMargin - size;
        int bottom = child.getBottom() + params.bottomMargin + size;
        int left = child.getRight() + params.rightMargin;
        int right = left + size;

        c.drawRect(left, top, right, bottom, mPaint);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        mDrawOption.recyclerView = parent;
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();

        outRect.set(mDrawOption.leftDividerSize(itemPosition),
                mDrawOption.topDividerSize(itemPosition),
                mDrawOption.rightDividerSize(itemPosition),
                mDrawOption.bottomDividerSize(itemPosition));
    }

    public static abstract class DecorationDrawOption {
        RecyclerView recyclerView;

        final int leftDividerSizeInternal(int itemPosition) {
            if (!checkLayoutManager()) return 0;
            return leftDividerSize(itemPosition);
        }

        final int topDividerSizeInternal(int itemPosition) {
            if (!checkLayoutManager()) return 0;
            return topDividerSize(itemPosition);
        }

        final int rightDividerSizeInternal(int itemPosition) {
            if (!checkLayoutManager()) return 0;
            return rightDividerSize(itemPosition);
        }

        final int bottomDividerSizeInternal(int itemPosition) {
            if (!checkLayoutManager()) return 0;
            return bottomDividerSize(itemPosition);
        }

        public abstract int leftDividerSize(int itemPosition);

        public abstract int topDividerSize(int itemPosition);

        public abstract int rightDividerSize(int itemPosition);

        public abstract int bottomDividerSize(int itemPosition);

        public int getItemCount() {
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter != null) {
                if (adapter instanceof HeaderViewWrapperAdapter) {
                    return ((HeaderViewWrapperAdapter) adapter).getWrappedAdapter().getItemCount();
                }
                return adapter.getItemCount();
            }
            return 0;
        }

        boolean checkLayoutManager() {
            return recyclerView != null && (recyclerView.getLayoutManager() instanceof GridLayoutManager) && (recyclerView.getAdapter() != null);
        }

        protected int getSpanCount() {
            if (!checkLayoutManager()) return 0;
            GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            return gridLayoutManager.getSpanCount();
        }

        protected boolean isFirstColumn(int position) {
            if (!checkLayoutManager()) return true;
            GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            if (gridLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                return position % getSpanCount() == 0;
            } else {
                int lastColumnCount = getItemCount() % getSpanCount();
                lastColumnCount = lastColumnCount == 0 ? getSpanCount() : lastColumnCount;
                return position < getItemCount() - lastColumnCount;
            }
        }

        protected boolean isLastColumn(int position) {
            if (!checkLayoutManager()) return true;
            GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            if (gridLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                return (position + 1) % getSpanCount() == 0;
            } else {
                int lastColumnCount = getItemCount() % getSpanCount();
                lastColumnCount = lastColumnCount == 0 ? getSpanCount() : lastColumnCount;
                return position >= getItemCount() - lastColumnCount;
            }
        }

        protected boolean isFirstRow(int position) {
            if (!checkLayoutManager()) return true;
            GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            if (gridLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                return position < getSpanCount();
            } else {
                return position % getSpanCount() == 0;
            }
        }

        protected boolean isLastRow(int position) {
            if (!checkLayoutManager()) return true;
            GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            if (gridLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                int lastColumnCount = getItemCount() % getSpanCount();
                lastColumnCount = lastColumnCount == 0 ? getSpanCount() : lastColumnCount;
                return position >= getItemCount() - lastColumnCount;
            } else {
                return (position + 1) % getSpanCount() == 0;
            }
        }

    }



}
