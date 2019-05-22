package razerdp.basepopup;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.lang.ref.WeakReference;

import razerdp.blur.PopupBlurOption;
import razerdp.interceptor.PopupWindowEventInterceptor;
import razerdp.library.R;

/**
 * Created by 大灯泡 on 2017/12/12.
 * <p>
 * popupoption
 */
final class BasePopupHelper implements PopupTouchController, PopupWindowActionListener, PopupWindowLocationListener,
        PopupKeyboardStateChangeListener, BasePopupFlag {

    enum ShowMode {
        RELATIVE_TO_ANCHOR,
        SCREEN,
        POSITION
    }

    private static final int CONTENT_VIEW_ID = R.id.base_popup_content_root;
    static final int DEFAULT_WIDTH = ViewGroup.LayoutParams.WRAP_CONTENT;
    static final int DEFAULT_HEIGHT = ViewGroup.LayoutParams.WRAP_CONTENT;

    private ShowMode mShowMode = ShowMode.SCREEN;

    private int contentRootId = CONTENT_VIEW_ID;

    private int flag = IDLE;

    private static int showCount;

    //animate
    private Animation mShowAnimation;
    private Animator mShowAnimator;
    private Animation mDismissAnimation;
    private Animator mDismissAnimator;

    //callback
    private BasePopupWindow.OnDismissListener mOnDismissListener;
    private BasePopupWindow.OnBeforeShowCallback mOnBeforeShowCallback;

    //option
    private BasePopupWindow.GravityMode gravityMode = BasePopupWindow.GravityMode.RELATIVE_TO_ANCHOR;
    private int popupGravity = Gravity.NO_GRAVITY;
    private int offsetX;
    private int offsetY;
    private int preMeasureWidth;
    private int preMeasureHeight;

    private int popupViewWidth;
    private int popupViewHeight;
    //锚点view的location
    private int[] mAnchorViewLocation;
    private int mAnchorViewHeight;
    private int mAnchorViewWidth;

    //模糊option(为空的话则不模糊）
    private PopupBlurOption mBlurOption;
    //背景颜色
    private Drawable mBackgroundDrawable = new ColorDrawable(BasePopupWindow.DEFAULT_BACKGROUND_COLOR);
    //背景对齐方向
    private int alignBackgroundGravity = Gravity.TOP;
    //背景View
    private View mBackgroundView;

    private PopupTouchController mTouchControllerDelegate;
    private PopupWindowActionListener mActionListener;
    private PopupWindowLocationListener mLocationListener;
    private PopupKeyboardStateChangeListener mKeyboardStateChangeListener;
    private PopupWindowEventInterceptor mEventInterceptor;

    private int mSoftInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
    private ViewGroup.MarginLayoutParams mParseFromXmlParams;
    private Point mTempOffset = new Point();

    private int maxWidth, maxHeight, minWidth, minHeight;

    private InnerShowInfo mShowInfo;

    private static class InnerShowInfo {
        WeakReference<View> mAnchorView;
        boolean positionMode;

        InnerShowInfo(View mAnchorView, boolean positionMode) {
            this.mAnchorView = new WeakReference<>(mAnchorView);
            this.positionMode = positionMode;
        }

    }

    BasePopupHelper(PopupTouchController controller) {
        mAnchorViewLocation = new int[2];
        this.mTouchControllerDelegate = controller;
    }

    BasePopupHelper registerActionListener(PopupWindowActionListener actionListener) {
        this.mActionListener = actionListener;
        return this;
    }

    BasePopupHelper registerLocationLisener(PopupWindowLocationListener locationListener) {
        this.mLocationListener = locationListener;
        return this;
    }

    BasePopupHelper registerKeyboardStateChangeListener(PopupKeyboardStateChangeListener mKeyboardStateChangeListener) {
        this.mKeyboardStateChangeListener = mKeyboardStateChangeListener;
        return this;
    }

    public View inflate(Context context, int layoutId) {
        try {
            FrameLayout tempLayout = new FrameLayout(context);
            View result = LayoutInflater.from(context).inflate(layoutId, tempLayout, false);
            ViewGroup.LayoutParams childParams = result.getLayoutParams();
            if (childParams != null) {
                checkAndSetGravity(childParams);
                if (childParams instanceof ViewGroup.MarginLayoutParams) {
                    mParseFromXmlParams = new ViewGroup.MarginLayoutParams((ViewGroup.MarginLayoutParams) childParams);
                    if ((flag & CUSTOM_WIDTH) != 0) {
                        mParseFromXmlParams.width = popupViewWidth;
                    }
                    if ((flag & CUSTOM_HEIGHT) != 0) {
                        mParseFromXmlParams.height = popupViewHeight;
                    }
                    tempLayout = null;
                    return result;
                }
                mParseFromXmlParams = new ViewGroup.MarginLayoutParams(childParams);
                if ((flag & CUSTOM_WIDTH) != 0) {
                    mParseFromXmlParams.width = popupViewWidth;
                }
                if ((flag & CUSTOM_HEIGHT) != 0) {
                    mParseFromXmlParams.height = popupViewHeight;
                }
                tempLayout = null;
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void checkAndSetGravity(ViewGroup.LayoutParams p) {
        if (p == null) return;
        if (p instanceof LinearLayout.LayoutParams) {
            setPopupGravity(gravityMode, ((LinearLayout.LayoutParams) p).gravity);
        } else if (p instanceof FrameLayout.LayoutParams) {
            setPopupGravity(gravityMode, ((FrameLayout.LayoutParams) p).gravity);
        }
    }

    Animation getShowAnimation() {
        return mShowAnimation;
    }

    BasePopupHelper setShowAnimation(Animation showAnimation) {
        if (mShowAnimation == showAnimation) return this;
        if (mShowAnimation != null) {
            mShowAnimation.cancel();
        }
        mShowAnimation = showAnimation;
        applyBlur(mBlurOption);
        return this;
    }

    Animator getShowAnimator() {
        return mShowAnimator;
    }

    BasePopupHelper setShowAnimator(Animator showAnimator) {
        if (mShowAnimator == showAnimator) return this;
        if (mShowAnimator != null) {
            mShowAnimator.cancel();
        }
        mShowAnimator = showAnimator;
        applyBlur(mBlurOption);
        return this;
    }

    Animation getDismissAnimation() {
        return mDismissAnimation;
    }

    BasePopupHelper setDismissAnimation(Animation dismissAnimation) {
        if (mDismissAnimation == dismissAnimation) return this;
        if (mDismissAnimation != null) {
            mDismissAnimation.cancel();
        }
        mDismissAnimation = dismissAnimation;
        applyBlur(mBlurOption);
        return this;
    }

    Animator getDismissAnimator() {
        return mDismissAnimator;
    }

    BasePopupHelper setDismissAnimator(Animator dismissAnimator) {
        if (mDismissAnimator == dismissAnimator) return this;
        if (mDismissAnimator != null) {
            mDismissAnimator.cancel();
        }
        mDismissAnimator = dismissAnimator;
        applyBlur(mBlurOption);
        return this;
    }

    boolean isCustomMeasure() {
        return (flag & (CUSTOM_WIDTH | CUSTOM_HEIGHT)) != 0;
    }

    int getPopupViewWidth() {
        if ((flag & CUSTOM_WIDTH) != 0) {
            return popupViewWidth;
        } else {
            if (mParseFromXmlParams != null) {
                return mParseFromXmlParams.width;
            }
        }
        return popupViewWidth;
    }

    BasePopupHelper setPopupViewWidth(int popupViewWidth) {
        this.popupViewWidth = popupViewWidth;
        if (popupViewWidth != DEFAULT_WIDTH) {
            setFlag(CUSTOM_WIDTH, true);
            if (mParseFromXmlParams != null) {
                mParseFromXmlParams.width = popupViewWidth;
            }
        } else {
            setFlag(CUSTOM_WIDTH, false);
        }
        return this;
    }

    int getPopupViewHeight() {
        if ((flag & CUSTOM_HEIGHT) != 0) {
            return popupViewHeight;
        } else {
            if (mParseFromXmlParams != null) {
                return mParseFromXmlParams.height;
            }
        }
        return popupViewHeight;
    }

    BasePopupHelper setPopupViewHeight(int popupViewHeight) {
        this.popupViewHeight = popupViewHeight;
        if (popupViewHeight != DEFAULT_HEIGHT) {
            setFlag(CUSTOM_HEIGHT, true);
            if (mParseFromXmlParams != null) {
                mParseFromXmlParams.height = popupViewHeight;
            }
        } else {
            setFlag(CUSTOM_HEIGHT, false);
        }
        return this;
    }

    int getPreMeasureWidth() {
        return preMeasureWidth;
    }

    BasePopupHelper setPreMeasureWidth(int preMeasureWidth) {
        this.preMeasureWidth = preMeasureWidth;
        return this;
    }

    int getPreMeasureHeight() {
        return preMeasureHeight;
    }

    BasePopupHelper setPreMeasureHeight(int preMeasureHeight) {
        this.preMeasureHeight = preMeasureHeight;
        return this;
    }

    boolean isPopupFadeEnable() {
        return (flag & FADE_ENABLE) != 0;
    }

    BasePopupHelper setPopupFadeEnable(PopupWindow popupWindow, boolean fadeEnable) {
        if (popupWindow == null) return this;
        setFlag(FADE_ENABLE, fadeEnable);
        return this;
    }

    boolean isShowAsDropDown() {
        return (flag & AS_DROP_DOWN) != 0;
    }

    BasePopupHelper setShowAsDropDown(boolean showAsDropDown) {
        setFlag(AS_DROP_DOWN, showAsDropDown);
        return this;
    }

    BasePopupHelper setShowLocation(int x, int y) {
        mAnchorViewLocation[0] = x;
        mAnchorViewLocation[1] = y;
        mAnchorViewWidth = 1;
        mAnchorViewHeight = 1;
        return this;
    }

    BasePopupWindow.GravityMode getGravityMode() {
        return gravityMode;
    }

    int getPopupGravity() {
        return popupGravity;
    }

    BasePopupHelper setPopupGravity(BasePopupWindow.GravityMode mode, int popupGravity) {
        if (popupGravity == this.popupGravity && gravityMode == mode) return this;
        this.gravityMode = mode;
        this.popupGravity = popupGravity;
        return this;
    }

    public BasePopupHelper setClipChildren(boolean clipChildren) {
        setFlag(CLIP_CHILDREN, clipChildren);
        return this;
    }

    int getOffsetX() {
        return offsetX;
    }

    BasePopupHelper setOffsetX(int offsetX) {
        this.offsetX = offsetX;
        return this;
    }

    int getOffsetY() {
        return offsetY;
    }

    BasePopupHelper setOffsetY(int offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    boolean isAutoShowInputMethod() {
        return (flag & AUTO_INPUT_METHOD) != 0;
    }

    BasePopupHelper autoShowInputMethod(PopupWindow popupWindow, boolean autoShowInputMethod) {
        if (popupWindow == null) return this;
        setFlag(AUTO_INPUT_METHOD, autoShowInputMethod);
        popupWindow.setSoftInputMode(autoShowInputMethod ? WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE : WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED);
        return this;
    }

    BasePopupHelper setSoftInputMode(int inputMethodType) {
        mSoftInputMode = inputMethodType;
        return this;
    }

    boolean isAutoLocatePopup() {
        return (flag & AUTO_LOCATED) != 0;
    }

    BasePopupHelper autoLocatePopup(boolean autoLocatePopup) {
        setFlag(AUTO_LOCATED, autoLocatePopup);
        return this;
    }

    BasePopupWindow.OnDismissListener getOnDismissListener() {
        return mOnDismissListener;
    }

    BasePopupHelper setOnDismissListener(BasePopupWindow.OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
        return this;
    }

    BasePopupWindow.OnBeforeShowCallback getOnBeforeShowCallback() {
        return mOnBeforeShowCallback;
    }

    BasePopupHelper setOnBeforeShowCallback(BasePopupWindow.OnBeforeShowCallback onBeforeShowCallback) {
        mOnBeforeShowCallback = onBeforeShowCallback;
        return this;
    }

    boolean isOutSideDismiss() {
        return (flag & OUT_SIDE_DISMISS) != 0;
    }

    BasePopupHelper dismissOutSideTouch(PopupWindow popupWindow, boolean dismissWhenTouchOutside) {
        if (popupWindow == null) return this;
        setFlag(OUT_SIDE_DISMISS, dismissWhenTouchOutside);
        return this;
    }

    boolean isOutSideTouchable() {
        return (flag & OUT_SIDE_TOUCHABLE) != 0;
    }

    BasePopupHelper outSideTouchable(PopupWindow popupWindow, boolean touchAble) {
        if (popupWindow == null) return this;
        setFlag(OUT_SIDE_TOUCHABLE, touchAble);
        return this;
    }

    BasePopupHelper setClipToScreen(boolean clipToScreen) {
        setFlag(CLIP_TO_SCREEN, clipToScreen);
        return this;
    }

    BasePopupHelper setEventInterceptor(PopupWindowEventInterceptor mInterceptor) {
        this.mEventInterceptor = mInterceptor;
        return this;
    }

    BasePopupHelper getAnchorLocation(View v) {
        if (v == null) return this;
        v.getLocationOnScreen(mAnchorViewLocation);
        mAnchorViewWidth = v.getWidth();
        mAnchorViewHeight = v.getHeight();
        return this;
    }

    public Point getTempOffset() {
        return mTempOffset;
    }

    public Point getTempOffset(int x, int y) {
        mTempOffset.set(x, y);
        return mTempOffset;
    }

    int getAnchorWidth() {
        return mAnchorViewWidth;
    }

    int getAnchorHeight() {
        return mAnchorViewHeight;
    }

    int getAnchorX() {
        return mAnchorViewLocation[0];
    }

    int getAnchorY() {
        return mAnchorViewLocation[1];
    }

    boolean isBackPressEnable() {
        return (flag & BACKPRESS_ENABLE) != 0;
    }

    boolean isClipToScreen() {
        return (flag & CLIP_TO_SCREEN) != 0;
    }

    BasePopupHelper backPressEnable(PopupWindow popupWindow, boolean backPressEnable) {
        if (popupWindow == null) return this;
        setFlag(BACKPRESS_ENABLE, backPressEnable);
        return this;
    }

    boolean isFullScreen() {
        return (flag & FULL_SCREEN) != 0;
    }

    BasePopupHelper fullScreen(boolean fullScreen) {
        setFlag(FULL_SCREEN, fullScreen);
        return this;
    }

    PopupBlurOption getBlurOption() {
        return mBlurOption;
    }

    BasePopupHelper applyBlur(PopupBlurOption option) {
        this.mBlurOption = option;
        if (option != null) {
            if (option.getBlurInDuration() <= 0) {
                long duration = getShowAnimationDuration();
                if (duration > 0) {
                    option.setBlurInDuration(duration);
                }
            }
            if (option.getBlurOutDuration() <= 0) {
                long duration = getDismissAnimationDuration();
                if (duration > 0) {
                    option.setBlurOutDuration(duration);
                }
            }
        }
        return this;
    }

    long getShowAnimationDuration() {
        long duration = 0;
        if (mShowAnimation != null) {
            duration = mShowAnimation.getDuration();
        } else if (mShowAnimator != null) {
            duration = getDurationFromAnimator(mShowAnimator);
        }
        return duration < 0 ? 500 : duration;
    }

    long getDismissAnimationDuration() {
        long duration = 0;
        if (mDismissAnimation != null) {
            duration = mDismissAnimation.getDuration();
        } else if (mDismissAnimator != null) {
            duration = getDurationFromAnimator(mDismissAnimator);
        }
        return duration < 0 ? 500 : duration;
    }

    private long getDurationFromAnimator(Animator animator) {
        if (animator == null) return -1;
        long duration = 0;
        if (animator instanceof AnimatorSet) {
            AnimatorSet set = ((AnimatorSet) animator);
            duration = set.getDuration();
            if (duration < 0) {
                for (Animator childAnimation : set.getChildAnimations()) {
                    duration = Math.max(duration, childAnimation.getDuration());
                }
            }
        } else {
            duration = animator.getDuration();
        }
        return duration;
    }

    Drawable getPopupBackground() {
        return mBackgroundDrawable;
    }

    BasePopupHelper setPopupBackground(Drawable background) {
        mBackgroundDrawable = background;
        return this;
    }

    boolean isAlignBackground() {
        return (flag & ALIGN_BACKGROUND) != 0;
    }

    BasePopupHelper setAlignBackgound(boolean mAlignBackground) {
        setFlag(ALIGN_BACKGROUND, mAlignBackground);
        if (!mAlignBackground) {
            setAlignBackgroundGravity(Gravity.NO_GRAVITY);
        }
        return this;
    }

    int getAlignBackgroundGravity() {
        if (isAlignBackground() && alignBackgroundGravity == Gravity.NO_GRAVITY) {
            alignBackgroundGravity = Gravity.TOP;
        }
        return alignBackgroundGravity;
    }

    BasePopupHelper setAlignBackgroundGravity(int gravity) {
        this.alignBackgroundGravity = gravity;
        return this;
    }

    boolean isAllowToBlur() {
        return mBlurOption != null && mBlurOption.isAllowToBlur();
    }


    boolean isClipChildren() {
        return (flag & CLIP_CHILDREN) != 0;
    }

    ViewGroup.MarginLayoutParams getParaseFromXmlParams() {
        return mParseFromXmlParams;
    }

    int getShowCount() {
        return showCount;
    }

    PopupWindowEventInterceptor getEventInterceptor() {
        return mEventInterceptor;
    }

    BasePopupHelper setContentRootId(View contentRoot) {
        if (contentRoot == null) return this;
        if (contentRoot.getId() == View.NO_ID) {
            contentRoot.setId(CONTENT_VIEW_ID);
        }
        this.contentRootId = contentRoot.getId();
        return this;
    }

    int getContentRootId() {
        return contentRootId;
    }

    int getSoftInputMode() {
        return mSoftInputMode;
    }

    View getBackgroundView() {
        return mBackgroundView;
    }

    BasePopupHelper setBackgroundView(View backgroundView) {
        mBackgroundView = backgroundView;
        return this;
    }

    int getMaxWidth() {
        return maxWidth;
    }

    BasePopupHelper setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    int getMaxHeight() {
        return maxHeight;
    }

    BasePopupHelper setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    ShowMode getShowMode() {
        return mShowMode;
    }

    BasePopupHelper setShowMode(ShowMode showMode) {
        mShowMode = showMode;
        return this;
    }

    int getMinWidth() {
        return minWidth;
    }

    BasePopupHelper setMinWidth(int minWidth) {
        this.minWidth = minWidth;
        return this;
    }

    int getMinHeight() {
        return minHeight;
    }

    BasePopupHelper setMinHeight(int minHeight) {
        this.minHeight = minHeight;
        return this;
    }

    BasePopupHelper keepSize(boolean keep) {
        setFlag(KEEP_SIZE, keep);
        return this;
    }

    boolean isKeepSize() {
        return (flag & KEEP_SIZE) != 0;
    }

    //-----------------------------------------controller-----------------------------------------
    void prepare(View v, boolean positionMode) {
        mShowInfo = new InnerShowInfo(v, positionMode);
        if (positionMode) {
            setShowMode(BasePopupHelper.ShowMode.POSITION);
        } else {
            setShowMode(v == null ? BasePopupHelper.ShowMode.SCREEN : BasePopupHelper.ShowMode.RELATIVE_TO_ANCHOR);
        }
        getAnchorLocation(v);
    }


    void handleShow() {
        //针对官方的坑（两个popup切换页面后重叠）
        if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP ||
                android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
            showCount++;
        }
    }

    void handleDismiss() {
        if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP ||
                android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
            showCount--;
            showCount = Math.max(0, showCount);
        }
    }

    private void setFlag(int flag, boolean added) {
        if (!added) {
            this.flag &= ~flag;
        } else {
            this.flag |= flag;
            if (flag == AUTO_LOCATED) {
                this.flag |= AS_DROP_DOWN;
            }
        }
    }

    @Override
    public boolean onBeforeDismiss() {
        return mTouchControllerDelegate.onBeforeDismiss();
    }

    @Override
    public boolean callDismissAtOnce() {
        return mTouchControllerDelegate.callDismissAtOnce();
    }

    @Override
    public boolean onDispatchKeyEvent(KeyEvent event) {
        return mTouchControllerDelegate.onDispatchKeyEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mTouchControllerDelegate.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mTouchControllerDelegate.onTouchEvent(event);
    }

    @Override
    public boolean onBackPressed() {
        return mTouchControllerDelegate.onBackPressed();
    }

    @Override
    public boolean onOutSideTouch() {
        return mTouchControllerDelegate.onOutSideTouch();
    }

    @Override
    public void onShow(boolean hasAnimate) {
        if (mActionListener != null) {
            mActionListener.onShow(hasAnimate);
        }
    }

    @Override
    public void onDismiss(boolean hasAnimate) {
        if (mActionListener != null) {
            mActionListener.onDismiss(hasAnimate);
        }
    }

    @Override
    public void onAnchorTop() {
        if (mLocationListener != null) {
            mLocationListener.onAnchorTop();
        }
    }

    @Override
    public void onAnchorBottom() {
        if (mLocationListener != null) {
            mLocationListener.onAnchorBottom();
        }
    }

    @Override
    public void onKeyboardChange(int keyboardTop, int keyboardHeight, boolean isVisible,boolean fullScreen) {
        if (mKeyboardStateChangeListener != null) {
            mKeyboardStateChangeListener.onKeyboardChange(keyboardTop,keyboardHeight, isVisible,fullScreen);
        }
    }

    @Override
    public boolean onUpdate() {
        if (mShowInfo != null) {
            prepare(mShowInfo.mAnchorView == null ? null : mShowInfo.mAnchorView.get(), mShowInfo.positionMode);
        }
        return false;
    }
}
