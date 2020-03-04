package razerdp.demo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import razerdp.basepopup.R;


/**
 * Created by 大灯泡 on 2019/6/14
 * <p>
 * Description：限制大小的framelayout
 */
public class LimitSizeFrameLayout extends FrameLayout {
    private static final String TAG = "LimitSizeFrameLayout";

    private float maxWidthRatioInParent;
    private float maxHeightRatioInParent;
    private float minWidthRatioInParent;
    private float minHeightRatioInParent;

    private int maxWidth;
    private int maxHeight;
    private int minWidth;
    private int minHeight;

    public LimitSizeFrameLayout(Context context) {
        this(context, null);
    }

    public LimitSizeFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LimitSizeFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LimitSizeFrameLayout);
        maxWidthRatioInParent = a.getFloat(R.styleable.LimitSizeFrameLayout_maxWidth_Ratio, maxWidthRatioInParent);
        maxHeightRatioInParent = a.getFloat(R.styleable.LimitSizeFrameLayout_maxHeight_Ratio, maxHeightRatioInParent);
        minWidthRatioInParent = a.getFloat(R.styleable.LimitSizeFrameLayout_minWidth_Ratio, minWidthRatioInParent);
        minHeightRatioInParent = a.getFloat(R.styleable.LimitSizeFrameLayout_minHeight_Ratio, minHeightRatioInParent);

        maxWidth = a.getDimensionPixelSize(R.styleable.LimitSizeFrameLayout_maxWidth, maxWidth);
        maxHeight = a.getDimensionPixelSize(R.styleable.LimitSizeFrameLayout_maxHeight, maxHeight);
        minWidth = a.getDimensionPixelSize(R.styleable.LimitSizeFrameLayout_minWidth, minWidth);
        minHeight = a.getDimensionPixelSize(R.styleable.LimitSizeFrameLayout_minHeight, minHeight);

        a.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View parent = (View) getParent();
        if (parent != null) {
            super.onMeasure(fitWidth(MeasureSpec.getSize(widthMeasureSpec), widthMeasureSpec), fitHeight(MeasureSpec.getSize(heightMeasureSpec), heightMeasureSpec));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private int fitWidth(int parentWidth, int widthMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int rMinWidth = Math.min(minWidth, parentWidth);
        boolean change = false;
        if (rMinWidth > 0) {
            if (width < rMinWidth) {
                width = rMinWidth;
                change = true;
            }
        } else if (minWidthRatioInParent > 0) {
            int min = Math.round(parentWidth * Math.min(minWidthRatioInParent, 1f));
            if (width < min) {
                width = min;
                change = true;
            }
        }

        int rMaxWidth = Math.min(maxWidth, parentWidth);
        if (maxWidth > 0) {
            if (width > rMaxWidth) {
                width = rMaxWidth;
                change = true;
            }
        } else if (maxWidthRatioInParent > 0) {
            int max = Math.round(parentWidth * Math.min(maxWidthRatioInParent, 1f));
            if (width > max) {
                width = max;
                change = true;
            }
        }

        return MeasureSpec.makeMeasureSpec(width, change && widthMode == MeasureSpec.UNSPECIFIED ? MeasureSpec.EXACTLY : widthMode);
    }

    private int fitHeight(int parentHeight, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int rMinHeight = Math.min(minHeight, parentHeight);
        boolean change = false;
        if (minHeight > 0) {
            if (height < rMinHeight) {
                height = rMinHeight;
                change = true;
            }
        } else if (minHeightRatioInParent > 0) {
            int min = Math.round(parentHeight * Math.min(minHeightRatioInParent, 1f));
            if (height < min) {
                height = min;
                change = true;
            }
        }

        int rMaxHeight = Math.min(maxHeight, parentHeight);
        if (maxHeight > 0) {
            if (height > rMaxHeight) {
                height = rMaxHeight;
                change = true;
            }
        } else if (maxHeightRatioInParent > 0) {
            int max = Math.round(parentHeight * Math.min(maxHeightRatioInParent, 1f));
            if (height > max) {
                height = max;
                change = true;
            }
        }

        return MeasureSpec.makeMeasureSpec(height, change && heightMode == MeasureSpec.UNSPECIFIED ? MeasureSpec.EXACTLY : heightMode);
    }

    public float getMaxWidthRatioInParent() {
        return maxWidthRatioInParent;
    }

    public LimitSizeFrameLayout setMaxWidthRatioInParent(float maxWidthRatioInParent) {
        this.maxWidthRatioInParent = maxWidthRatioInParent;
        requestLayout();
        return this;
    }

    public float getMaxHeightRatioInParent() {
        return maxHeightRatioInParent;
    }

    public LimitSizeFrameLayout setMaxHeightRatioInParent(float maxHeightRatioInParent) {
        this.maxHeightRatioInParent = maxHeightRatioInParent;
        requestLayout();
        return this;
    }

    public float getMinWidthRatioInParent() {
        return minWidthRatioInParent;
    }

    public LimitSizeFrameLayout setMinWidthRatioInParent(float minWidthRatioInParent) {
        this.minWidthRatioInParent = minWidthRatioInParent;
        requestLayout();
        return this;
    }

    public float getMinHeightRatioInParent() {
        return minHeightRatioInParent;
    }

    public LimitSizeFrameLayout setMinHeightRatioInParent(float minHeightRatioInParent) {
        this.minHeightRatioInParent = minHeightRatioInParent;
        requestLayout();
        return this;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public LimitSizeFrameLayout setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        requestLayout();
        return this;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public LimitSizeFrameLayout setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        requestLayout();
        return this;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public LimitSizeFrameLayout setMinWidth(int minWidth) {
        this.minWidth = minWidth;
        requestLayout();
        return this;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public LimitSizeFrameLayout setMinHeight(int minHeight) {
        this.minHeight = minHeight;
        requestLayout();
        return this;
    }
}
