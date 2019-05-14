package razerdp.basepopup;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import razerdp.blur.BlurImageView;
import razerdp.library.R;
import razerdp.util.PopupUtils;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2018/5/9.
 * <p>
 * 蒙层
 */
class PopupMaskLayout extends FrameLayout {

    private BlurImageView mBlurImageView;
    private BackgroundViewHolder mBackgroundViewHolder;

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
        PopupLog.i("dispatch  >> " + dispatch);
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
        if (mHelper.getBackgroundView() != null) {
            mBackgroundViewHolder = new BackgroundViewHolder(mHelper.getBackgroundView(), mHelper);
        } else {
            if (!PopupUtils.isBackgroundInvalidated(mHelper.getPopupBackground())) {
                mBackgroundViewHolder = new BackgroundViewHolder(PopupBackgroundView.creaete(context, mHelper), mHelper);
            }
        }
        if (mBackgroundViewHolder != null) {
            mBackgroundViewHolder.addInLayout();
        }
        mHelper.registerActionListener(new PopupWindowActionListener() {
            @Override
            public void onShow(boolean hasAnimate) {

            }

            @Override
            public void onDismiss(boolean hasAnimate) {
                handleDismiss(hasAnimate ? -2 : 0);
            }

            @Override
            public boolean onUpdate() {
                return false;
            }
        });
    }


    public void handleAlignBackground(int gravity, int contentLeft, int contentTop, int contentRight, int contentBottom) {
        int left = getLeft();
        int top = getTop();
        int right = getRight();
        int bottom = getBottom();

        switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.LEFT:
            case Gravity.START:
                left = contentLeft;
                break;
            case Gravity.RIGHT:
            case Gravity.END:
                right = contentRight;
                break;
            default:
                break;
        }

        switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.TOP:
                top = contentTop;
                break;
            case Gravity.BOTTOM:
                bottom = contentBottom;
                break;
            default:
                break;
        }
        if (mBackgroundViewHolder != null) {
            mBackgroundViewHolder.handleAlignBackground(left, top, right, bottom);
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public void update() {
        if (mBlurImageView != null) {
            mBlurImageView.update();
        }
        if (mBackgroundViewHolder != null) {
            mBackgroundViewHolder.update();
        }
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
        if (mBackgroundViewHolder != null) {
            mBackgroundViewHolder.dismiss();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeAllViews();
        if (mBackgroundViewHolder != null) {
            mBackgroundViewHolder.destroy();
            mBackgroundViewHolder = null;
        }

        if (mBlurImageView != null) {
            mBlurImageView.destroy();
            mBlurImageView = null;
        }
    }


    final class BackgroundViewHolder {

        View mBackgroundView;
        BasePopupHelper mHelper;

        BackgroundViewHolder(View backgroundView, BasePopupHelper helper) {
            mBackgroundView = backgroundView;
            mHelper = helper;
            if (!(mBackgroundView instanceof PopupBackgroundView)) {
                if (mHelper.isPopupFadeEnable()) {
                    Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.basepopup_fade_in);
                    if (fadeIn != null) {
                        long fadeInTime = mHelper.getShowAnimationDuration() - 200;
                        fadeIn.setDuration(Math.max(fadeIn.getDuration(), fadeInTime));
                        fadeIn.setFillAfter(true);
                        mBackgroundView.startAnimation(fadeIn);
                    }
                }
            }
        }

        void addInLayout() {
            if (mBackgroundView != null) {
                addViewInLayout(mBackgroundView, -1, generateDefaultLayoutParams());
            }
        }

        void handleAlignBackground(int left, int top, int right, int bottom) {
            if (mBackgroundView == null) return;
            mBackgroundView.layout(left, top, right, bottom);
        }

        void update() {
            if (mBackgroundView instanceof PopupBackgroundView) {
                ((PopupBackgroundView) mBackgroundView).update();
            }
        }

        void dismiss() {
            if (mBackgroundView instanceof PopupBackgroundView) {
                ((PopupBackgroundView) mBackgroundView).handleAnimateDismiss();
            } else {
                if (mBackgroundView != null && mHelper != null && mHelper.isPopupFadeEnable()) {
                    Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.basepopup_fade_out);
                    if (fadeOut != null) {
                        long fadeDismissTime = mHelper.getDismissAnimationDuration() - 200;
                        fadeOut.setDuration(Math.max(fadeOut.getDuration(), fadeDismissTime));
                        fadeOut.setFillAfter(true);
                        mBackgroundView.startAnimation(fadeOut);
                    }
                }
            }
        }

        void destroy() {
            if (mBackgroundView instanceof PopupBackgroundView) {
                ((PopupBackgroundView) mBackgroundView).destroy();
                mBackgroundView = null;
            } else {
                mBackgroundView = null;
            }
        }
    }
}
