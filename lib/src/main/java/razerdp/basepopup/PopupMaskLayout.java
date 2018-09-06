package razerdp.basepopup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import razerdp.blur.BlurImageView;
import razerdp.util.PopupUtil;

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

    public static PopupMaskLayout create(Context context, BasePopupHelper helper, ViewGroup.LayoutParams params) {
        PopupMaskLayout view = new PopupMaskLayout(context);
        view.init(context, helper, params);
        return view;
    }

    private void init(Context context, BasePopupHelper mHelper, ViewGroup.LayoutParams params) {
        if (mHelper == null) {
            setVisibility(GONE);
            return;
        }
        setLayoutAnimation(null);
        setVisibility(VISIBLE);
        if (mHelper.isAllowToBlur()) {
            mBlurImageView = new BlurImageView(context);
            mBlurImageView.applyBlurOption(mHelper.getBlurOption());
            addViewInLayout(mBlurImageView, -1, generateDefaultLayoutParams());
        }
        if (!PopupUtil.isBackgroundInvalidated(mHelper.getPopupBackground())) {
            mBackgroundView = PopupBackgroundView.creaete(context, mHelper);
            LayoutParams backgroundViewParams = generateDefaultLayoutParams();
            if (mHelper.isAlignBackground() && params instanceof WindowManager.LayoutParams) {
                backgroundViewParams.topMargin = ((WindowManager.LayoutParams) params).y;
            }
            addViewInLayout(mBackgroundView, -1, backgroundViewParams);
        }
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
