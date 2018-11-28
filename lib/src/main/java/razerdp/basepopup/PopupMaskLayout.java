package razerdp.basepopup;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import razerdp.blur.BlurImageView;
import razerdp.util.PopupUtil;
import razerdp.util.log.PopupLogUtil;

/**
 * Created by 大灯泡 on 2018/5/9.
 * <p>
 * popup第二层window的蒙层
 */
class PopupMaskLayout extends FrameLayout {

    private PopupBackgroundView mBackgroundView;
    private BlurImageView mBlurImageView;

    private PopupMaskLayout(Context context) {
        this(context, null);
    }

    private PopupMaskLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private PopupMaskLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static PopupMaskLayout create(Context context, BasePopupHelper helper) {
        PopupMaskLayout view = new PopupMaskLayout(context);
        view.init(context, helper);
        return view;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean dispatch = super.dispatchKeyEvent(event);
        PopupLogUtil.trace("dispatch  >> " + dispatch);
        return dispatch;
    }

    private void init(Context context, BasePopupHelper mHelper) {
        setLayoutAnimation(null);
        if (mHelper == null) {
            setBackgroundColor(Color.TRANSPARENT);
            return;
        }
        if (mHelper.isAllowToBlur()) {
            mBlurImageView = new BlurImageView(context);
            mBlurImageView.applyBlurOption(mHelper.getBlurOption());
            addViewInLayout(mBlurImageView, -1, generateDefaultLayoutParams());
        }
        if (!PopupUtil.isBackgroundInvalidated(mHelper.getPopupBackground())) {
            mBackgroundView = PopupBackgroundView.creaete(context, mHelper);
            addViewInLayout(mBackgroundView, -1, generateDefaultLayoutParams());
        }
        mHelper.registerActionListener(new PopupWindowActionListener() {
            @Override
            public void onShow(boolean hasAnimate) {

            }

            @Override
            public void onDismiss(boolean hasAnimate) {
                handleDismiss(hasAnimate ? -2 : 0);
            }
        });
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public void handleStart(long duration) {
        if (mBlurImageView != null) {
            mBlurImageView.start(duration);
        }
    }

    public void handleDismiss(long duration) {
        if (mBlurImageView != null) {
            mBlurImageView.dismiss(duration);
        }
        if (mBackgroundView != null) {
            mBackgroundView.handleAnimateDismiss();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeAllViews();
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
