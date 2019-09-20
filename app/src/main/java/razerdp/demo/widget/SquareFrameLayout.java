package razerdp.demo.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import razerdp.basepopup.R;


/**
 * 正方形控件
 */
public class SquareFrameLayout extends FrameLayout {


    private static final int MEASURE_MODE_MIN = 0;
    private static final int MEASURE_MODE_MAX = 1;
    private static final int MEASURE_MODE_WIDTH = 2;
    private static final int MEASURE_MODE_HEIGHT = 3;


    private int measureMode = MEASURE_MODE_MIN;

    public SquareFrameLayout(Context context) {
        this(context, null);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(11)
    public SquareFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SquareFrameLayout);
        measureMode = a.getInt(R.styleable.SquareFrameLayout_measure_mode, measureMode);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int maxSpec = widthSize > heightSize ? widthMeasureSpec : heightMeasureSpec;
        int minSpec = widthSize > heightSize ? heightMeasureSpec : widthMeasureSpec;

        switch (measureMode) {
            case MEASURE_MODE_WIDTH:
                super.onMeasure(widthMeasureSpec, widthMeasureSpec);
                break;
            case MEASURE_MODE_HEIGHT:
                super.onMeasure(heightMeasureSpec, heightMeasureSpec);
                break;
            case MEASURE_MODE_MAX:
                super.onMeasure(maxSpec, maxSpec);
                break;
            case MEASURE_MODE_MIN:
                super.onMeasure(minSpec, minSpec);
                break;
        }
    }
}
