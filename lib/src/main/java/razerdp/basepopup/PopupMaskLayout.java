package razerdp.basepopup;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import razerdp.blur.BlurImageView;

/**
 * Created by 大灯泡 on 2018/5/9.
 * <p>
 * popup第二层window的蒙层
 */
class PopupMaskLayout extends FrameLayout {
    BasePopupHelper mHelper;

    private PopupBackgroundView mBackgroundView;
    private BlurImageView mBlurImageView;

    public PopupMaskLayout(Context context) {
        this(context, null);
    }

    public PopupMaskLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopupMaskLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PopupMaskLayout creaete(Context context, BasePopupHelper helper) {
        PopupMaskLayout view = new PopupMaskLayout(context);
        view.mHelper = helper;
        return view;
    }

    private void init(Context context) {
        if (mHelper == null) {
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);
        if (mHelper.getPopupBackgroundColor() != Color.TRANSPARENT) {
            mBackgroundView = PopupBackgroundView.creaete(context, mHelper);
            addView(mBackgroundView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        if (mHelper.isAllowToBlur()) {
            mBlurImageView = new BlurImageView(context);
            mBlurImageView.attachBlurOption(mHelper.getBlurOption());
            addView(new BlurImageView(context), new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }


    public void handleStart(long duration) {
        if (mBlurImageView != null) {
            mBlurImageView.start(duration);
        }
    }

    public void handleDismiss(long duration){
        if (mBlurImageView != null) {
            mBlurImageView.dismiss(duration);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mBackgroundView != null) {
            mBackgroundView.destroy();
            mBackgroundView = null;
        }

        if (mBlurImageView != null) {
            mBlurImageView.destroy();
            mBlurImageView = null;
        }
    }
}
