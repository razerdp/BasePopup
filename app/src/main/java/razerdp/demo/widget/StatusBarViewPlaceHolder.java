package razerdp.demo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import razerdp.basepopup.R;
import razerdp.demo.app.AppContext;
import razerdp.demo.utils.ViewUtil;


/**
 * Created by 大灯泡 on 2019/4/15.
 */
public class StatusBarViewPlaceHolder extends View {
    private static final String TAG = "StatusBarViewPlaceHolder";

    private static int sStatusBarHeight = 0;

    public StatusBarViewPlaceHolder(Context context) {
        this(context, null);
    }

    public StatusBarViewPlaceHolder(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusBarViewPlaceHolder(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (getId() == View.NO_ID) {
            setId(R.id.statusbar_placeholder);
        }
        Drawable background = null;
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.StatusBarViewPlaceHolder);
        background = a.getDrawable(R.styleable.StatusBarViewPlaceHolder_android_background);
        if (background != null) {
            ViewUtil.setBackground(this, background);
        } else {
            setBackgroundColor(Color.TRANSPARENT);
        }
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (sStatusBarHeight == 0) {
            sStatusBarHeight = getStatusBarHeight();
        }
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), sStatusBarHeight);
    }

    int getStatusBarHeight() {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = AppContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = AppContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
