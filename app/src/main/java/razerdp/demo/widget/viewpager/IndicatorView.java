package razerdp.demo.widget.viewpager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 大灯泡 on 2019/4/24
 * <p>
 * Description：
 */
public class IndicatorView extends View {
    private static final String TAG = "IndicatorView";

     static final int NORMAL_COLOR = 0xBFCCCCCC;
     static final int SELECTED_COLOR = 0xFFFFDAA6;

    private int mNormalColor = NORMAL_COLOR;
    private int mSelectedColor = SELECTED_COLOR;

    private Paint mNormalPaint = new Paint(Paint.ANTI_ALIAS_FLAG) {
        {
            setStyle(Style.FILL);
            setColor(mNormalColor);
        }
    };

    private Paint mSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG) {
        {
            setStyle(Style.FILL);
            setColor(mSelectedColor);
        }
    };

    private RectF mRectF = new RectF();

    public IndicatorView(Context context) {
        super(context);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (getBackground() != null) return;

        int width = getWidth();
        int height = getHeight();

        mRectF.set(0, 0, width, height);

        canvas.drawOval(mRectF, isSelected() ? mSelectedPaint : mNormalPaint);
    }

    public void setSelected(boolean selected) {
        super.setSelected(selected);
        invalidate();
    }


    public int getNormalColor() {
        return mNormalColor;
    }

    public void setNormalColor(int dotNormalColor) {
        if (mNormalColor == dotNormalColor) return;
        mNormalColor = dotNormalColor == 0 ? NORMAL_COLOR : dotNormalColor;
        if (mNormalPaint != null) {
            mNormalPaint.setColor(mNormalColor);
        }
        invalidate();
    }

    public int getSelectedColor() {
        return mSelectedColor;
    }

    public void setSelectedColor(int dotSelectedColor) {
        if (mSelectedColor == dotSelectedColor) return;
        mSelectedColor = dotSelectedColor == 0 ? SELECTED_COLOR : dotSelectedColor;
        if (mSelectedPaint != null) {
            mSelectedPaint.setColor(mSelectedColor);
        }
        invalidate();
    }
}
