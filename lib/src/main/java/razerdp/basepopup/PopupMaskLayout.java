package razerdp.basepopup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import razerdp.blur.BlurImageView;
import razerdp.util.PopupUiUtils;
import razerdp.util.PopupUtils;

/**
 * Created by 大灯泡 on 2018/5/9.
 * <p>
 * 蒙层
 */
class PopupMaskLayout extends FrameLayout implements BasePopupEvent.EventObserver, ClearMemoryObject {

    BlurImageView mBlurImageView;
    private BackgroundViewHolder mBackgroundViewHolder;
    private BasePopupHelper mPopupHelper;
    private int[] location = null;
    private RectF maskRect;

    private PopupMaskLayout(Context context) {
        super(context);
    }

    private PopupMaskLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private PopupMaskLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    PopupMaskLayout(Context context, BasePopupHelper helper) {
        this(context);
        init(context, helper);
    }


    private void init(Context context, BasePopupHelper mHelper) {
        this.mPopupHelper = mHelper;
        setClickable(true);
        location = null;
        maskRect = new RectF();
        setLayoutAnimation(null);
        if (mHelper == null) {
            setBackgroundColor(Color.TRANSPARENT);
            return;
        }
        mHelper.observerEvent(this, this);
        if (mHelper.isAllowToBlur()) {
            mBlurImageView = new BlurImageView(context);
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
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (location == null && mPopupHelper != null && mPopupHelper.isAllowToBlur() && mBlurImageView != null) {
            location = new int[2];
            getLocationOnScreen(location);
            mBlurImageView.setCutoutX(location[0]);
            mBlurImageView.setCutoutY(location[1]);
            mBlurImageView.applyBlurOption(mPopupHelper.getBlurOption());
        }
        maskRect.set(left, top, right, bottom);
        super.onLayout(changed, left, top, right, bottom);
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
        maskRect.set(left, top, right, bottom);
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
        if (mPopupHelper != null) {
            mPopupHelper.removeEventObserver(this);
            mPopupHelper = null;
        }
    }

    @Override
    public void onEvent(Message msg) {
        switch (msg.what) {
            case BasePopupEvent.EVENT_SHOW:
                handleShow();
                break;
            case BasePopupEvent.EVENT_DISMISS:
                handleDismiss(msg.arg1 == 1 ? -2 : 0);
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mPopupHelper != null) {
            if (!mPopupHelper.isOverlayStatusbar()) {
                ev.offsetLocation(0, PopupUiUtils.getStatusBarHeight());
            }
            mPopupHelper.dispatchOutSideEvent(ev, maskRect.contains(ev.getRawX(), ev.getRawY()),isPressed());
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public void clear(boolean destroy) {
        if (mBlurImageView != null) {
            mBlurImageView.destroy();
        }
        if (mBackgroundViewHolder != null) {
            mBackgroundViewHolder.clear(destroy);
        }
        if (destroy) {
            mPopupHelper = null;
            mBackgroundViewHolder = null;
            mBlurImageView = null;
        }
    }

    /**
     * 在decorview attach时调用，此时animator已经初始化
     */
    public void handleShow() {
        if (mBackgroundViewHolder != null) {
            mBackgroundViewHolder.handleShow();
        }
    }

    final class BackgroundViewHolder implements ClearMemoryObject {

        View mBackgroundView;
        BasePopupHelper mHelper;

        BackgroundViewHolder(View backgroundView, BasePopupHelper helper) {
            mBackgroundView = backgroundView;
            mHelper = helper;
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
            if (mHelper != null &&
                    mHelper.isPopupFadeEnable() &&
                    mBackgroundView != null &&
                    ((mBackgroundView instanceof PopupBackgroundView) || mBackgroundView.getAnimation() == null)) {
                if (mHelper.mMaskViewDismissAnimation != null) {
                    if (mHelper.isSyncMaskAnimationDuration()) {
                        if (mHelper.dismissDuration > 0 && mHelper.isDefaultMaskViewDismissAnimation) {
                            //当动画时间大于0，且没有设置过蒙层动画，则修改时间为动画时间+50ms
                            mHelper.mMaskViewDismissAnimation.setDuration(mHelper.dismissDuration + 50);
                        }
                    }
                    mBackgroundView.startAnimation(mHelper.mMaskViewDismissAnimation);
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

        void handleShow() {
            if (mHelper != null &&
                    mHelper.isPopupFadeEnable() &&
                    mBackgroundView != null &&
                    ((mBackgroundView instanceof PopupBackgroundView) || mBackgroundView.getAnimation() == null)) {
                //如果是自定义的backgroundview，同时有自己的动画，那就不使用我们的，而是使用开发者自定义的
                if (mHelper.mMaskViewShowAnimation != null) {
                    if (mHelper.isSyncMaskAnimationDuration()) {
                        if (mHelper.showDuration > 0 && mHelper.isDefaultMaskViewShowAnimation) {
                            //当动画时间大于0，且没有设置过蒙层动画，则修改时间为动画时间+50ms
                            mHelper.mMaskViewShowAnimation.setDuration(mHelper.showDuration + 50);
                        }
                    }
                    mBackgroundView.startAnimation(mHelper.mMaskViewShowAnimation);
                }
            }
        }

        @Override
        public void clear(boolean destroy) {
            if (destroy) {
                mBackgroundView = null;
                mHelper = null;
            }
        }
    }
}
