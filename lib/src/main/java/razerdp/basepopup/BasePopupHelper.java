package razerdp.basepopup;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

import razerdp.blur.PopupBlurOption;
import razerdp.library.R;
import razerdp.util.KeyboardUtils;
import razerdp.util.PopupUtils;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2017/12/12.
 * <p>
 * PopupHelper，这货与Popup强引用哦~
 */
@SuppressWarnings("all")
final class BasePopupHelper implements KeyboardUtils.OnKeyboardChangeListener, BasePopupFlag {

    BasePopupWindow popupWindow;

    WeakHashMap<Object, BasePopupEvent.EventObserver> eventObserverMap;

    enum ShowMode {
        RELATIVE_TO_ANCHOR,
        SCREEN,
        POSITION
    }

    private static final int CONTENT_VIEW_ID = R.id.base_popup_content_root;

    ShowMode mShowMode = ShowMode.SCREEN;

    int contentRootId = CONTENT_VIEW_ID;

    int flag = IDLE;

    static int showCount;

    //animate
    Animation mShowAnimation;
    Animator mShowAnimator;
    Animation mDismissAnimation;
    Animator mDismissAnimator;

    long showDuration;
    long dismissDuration;

    //callback
    BasePopupWindow.OnDismissListener mOnDismissListener;
    BasePopupWindow.OnBeforeShowCallback mOnBeforeShowCallback;

    //option
    BasePopupWindow.GravityMode gravityMode = BasePopupWindow.GravityMode.RELATIVE_TO_ANCHOR;
    int popupGravity = Gravity.NO_GRAVITY;
    int offsetX;
    int offsetY;
    int preMeasureWidth;
    int preMeasureHeight;

    int popupViewWidth = 0;
    int popupViewHeight = 0;
    //锚点view的location
    Rect mAnchorViewBound;

    //模糊option(为空的话则不模糊）
    PopupBlurOption mBlurOption;
    //背景颜色
    Drawable mBackgroundDrawable = new ColorDrawable(BasePopupWindow.DEFAULT_BACKGROUND_COLOR);
    //背景对齐方向
    int alignBackgroundGravity = Gravity.TOP;
    //背景View
    View mBackgroundView;

    EditText mAutoShowInputEdittext;

    KeyboardUtils.OnKeyboardChangeListener mKeyboardStateChangeListener;

    int mSoftInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
    ViewGroup.MarginLayoutParams layoutParams;
    Point mTempOffset = new Point();

    int maxWidth, maxHeight, minWidth, minHeight;

    int keybaordAlignViewId;

    InnerShowInfo mShowInfo;

    static class InnerShowInfo {
        WeakReference<View> mAnchorView;
        boolean positionMode;

        InnerShowInfo(View mAnchorView, boolean positionMode) {
            this.mAnchorView = new WeakReference<>(mAnchorView);
            this.positionMode = positionMode;
        }

    }

    BasePopupHelper(BasePopupWindow popupWindow) {
        mAnchorViewBound = new Rect();
        this.popupWindow = popupWindow;
        this.eventObserverMap = new WeakHashMap<>();
    }

    void observerEvent(Object who, BasePopupEvent.EventObserver observer) {
        eventObserverMap.put(who, observer);
    }

    void removeEventObserver(Object who) {
        eventObserverMap.remove(who);
    }

    void sendEvent(Message msg) {
        if (msg == null) return;
        if (msg.what < 0) return;
        for (Map.Entry<Object, BasePopupEvent.EventObserver> entry : eventObserverMap.entrySet()) {
            if (entry.getValue() != null) {
                entry.getValue().onEvent(msg);
            }
        }
    }

    View inflate(Context context, int layoutId) {
        try {
            FrameLayout tempLayout = new FrameLayout(context);
            View result = LayoutInflater.from(context).inflate(layoutId, tempLayout, false);
            ViewGroup.LayoutParams childParams = result.getLayoutParams();
            if (childParams != null) {
                checkAndSetGravity(childParams);
                if (childParams instanceof ViewGroup.MarginLayoutParams) {
                    layoutParams = new ViewGroup.MarginLayoutParams((ViewGroup.MarginLayoutParams) childParams);
                } else {
                    layoutParams = new ViewGroup.MarginLayoutParams(childParams);
                }

                if (popupViewWidth != 0 && layoutParams.width != popupViewWidth) {
                    layoutParams.width = popupViewWidth;
                }
                if (popupViewHeight != 0 && layoutParams.height != popupViewHeight) {
                    layoutParams.height = popupViewHeight;
                }
                tempLayout = null;
                return result;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    void preMeasurePopupView(View mContentView, int w, int h) {
        if (mContentView != null) {
            int measureWidth = View.MeasureSpec.makeMeasureSpec(Math.max(w, 0), w == ViewGroup.LayoutParams.WRAP_CONTENT ? View.MeasureSpec.UNSPECIFIED : View.MeasureSpec.EXACTLY);
            int measureHeight = View.MeasureSpec.makeMeasureSpec(Math.max(w, h), h == ViewGroup.LayoutParams.WRAP_CONTENT ? View.MeasureSpec.UNSPECIFIED : View.MeasureSpec.EXACTLY);
            mContentView.measure(measureWidth, measureHeight);
            setPreMeasureWidth(mContentView.getMeasuredWidth());
            setPreMeasureHeight(mContentView.getMeasuredHeight());
            mContentView.setFocusableInTouchMode(true);
        }
    }

    void checkAndSetGravity(ViewGroup.LayoutParams p) {
        if (p == null) return;
        if (p instanceof LinearLayout.LayoutParams) {
            setPopupGravity(gravityMode, ((LinearLayout.LayoutParams) p).gravity);
        } else if (p instanceof FrameLayout.LayoutParams) {
            setPopupGravity(gravityMode, ((FrameLayout.LayoutParams) p).gravity);
        }
    }

    //region Animation

    void startShowAnimate(int width, int height) {
        if (getShowAnimation(width, height) == null) {
            getShowAnimator(width, height);
        }
        if (mShowAnimation != null) {
            mShowAnimation.cancel();
            popupWindow.mDisplayAnimateView.startAnimation(mShowAnimation);
        } else if (mShowAnimator != null) {
            mShowAnimator.cancel();
            mShowAnimator.start();
        }
    }

    void startDismissAnimate(int width, int height) {
        if (getDismissAnimation(width, height) == null) {
            getDismissAnimator(width, height);
        }
        if (mDismissAnimation != null) {
            mDismissAnimation.cancel();
            popupWindow.mDisplayAnimateView.startAnimation(mDismissAnimation);
            if (mOnDismissListener != null) {
                mOnDismissListener.onDismissAnimationStart();
            }
            setFlag(CUSTOM_ON_ANIMATE_DISMISS, true);
        } else if (mDismissAnimator != null) {
            mDismissAnimator.cancel();
            mDismissAnimator.start();
            if (mOnDismissListener != null) {
                mOnDismissListener.onDismissAnimationStart();
            }
            setFlag(CUSTOM_ON_ANIMATE_DISMISS, true);
        }
    }

    Animation getShowAnimation(int width, int height) {
        if (mShowAnimation == null) {
            mShowAnimation = popupWindow.onCreateShowAnimation(width, height);
            if (mShowAnimation != null) {
                showDuration = PopupUtils.getAnimationDuration(mShowAnimation, 0);
                setToBlur(mBlurOption);
            }
        }
        return mShowAnimation;
    }

    Animator getShowAnimator(int width, int height) {
        if (mShowAnimator == null) {
            mShowAnimator = popupWindow.onCreateShowAnimator(width, height);
            if (mShowAnimator != null) {
                showDuration = PopupUtils.getAnimatorDuration(mShowAnimator, 0);
                setToBlur(mBlurOption);
            }
        }
        return mShowAnimator;
    }

    Animation getDismissAnimation(int width, int height) {
        if (mDismissAnimation == null) {
            mDismissAnimation = popupWindow.onCreateDismissAnimation(width, height);
            if (mDismissAnimation != null) {
                dismissDuration = PopupUtils.getAnimationDuration(mDismissAnimation, 0);
                setToBlur(mBlurOption);
            }
        }
        return mDismissAnimation;
    }

    Animator getDismissAnimator(int width, int height) {
        if (mDismissAnimator == null) {
            mDismissAnimator = popupWindow.onCreateDismissAnimator(width, height);
            if (mDismissAnimator != null) {
                dismissDuration = PopupUtils.getAnimatorDuration(mDismissAnimator, 0);
                setToBlur(mBlurOption);
            }
        }
        return mDismissAnimator;
    }


    void setToBlur(PopupBlurOption option) {
        this.mBlurOption = option;
        if (option != null) {
            if (option.getBlurInDuration() <= 0) {
                if (showDuration > 0) {
                    option.setBlurInDuration(showDuration);
                }
            }
            if (option.getBlurOutDuration() <= 0) {
                if (dismissDuration > 0) {
                    option.setBlurOutDuration(dismissDuration);
                }
            }
        }
    }


    void setShowAnimation(Animation showAnimation) {
        if (mShowAnimation == showAnimation) return;
        if (mShowAnimation != null) {
            mShowAnimation.cancel();
        }
        mShowAnimation = showAnimation;
        showDuration = PopupUtils.getAnimationDuration(mShowAnimation, 0);
        setToBlur(mBlurOption);
    }

    /**
     * animation优先级更高
     */
    void setShowAnimator(Animator showAnimator) {
        if (mShowAnimation != null || mShowAnimator == showAnimator) return;
        if (mShowAnimator != null) {
            mShowAnimator.cancel();
        }
        mShowAnimator = showAnimator;
        showDuration = PopupUtils.getAnimatorDuration(mShowAnimator, 0);
        setToBlur(mBlurOption);
    }

    void setDismissAnimation(Animation dismissAnimation) {
        if (mDismissAnimation == dismissAnimation) return;
        if (mDismissAnimation != null) {
            mDismissAnimation.cancel();
        }
        mDismissAnimation = dismissAnimation;
        dismissDuration = PopupUtils.getAnimationDuration(mDismissAnimation, 0);
        setToBlur(mBlurOption);
    }

    void setDismissAnimator(Animator dismissAnimator) {
        if (mDismissAnimation != null || mDismissAnimator == dismissAnimator) return;
        if (mDismissAnimator != null) {
            mDismissAnimator.cancel();
        }
        mDismissAnimator = dismissAnimator;
        dismissDuration = PopupUtils.getAnimatorDuration(mDismissAnimator, 0);
        setToBlur(mBlurOption);
    }

    //endregion

    BasePopupHelper setPopupViewWidth(int popupViewWidth) {
        if (popupViewWidth != 0) {
            getLayoutParams().width = popupViewWidth;
        }
        return this;
    }

    BasePopupHelper setPopupViewHeight(int popupViewHeight) {
        if (popupViewHeight != 0) {
            getLayoutParams().height = popupViewHeight;
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

    boolean isFitsizable() {
        return (flag & FITSIZE) != 0;
    }

    BasePopupHelper setShowAsDropDown(boolean showAsDropDown) {
        setFlag(AS_DROP_DOWN, showAsDropDown);
        return this;
    }

    BasePopupHelper setShowLocation(int x, int y) {
        mAnchorViewBound.set(x, y, x + 1, y + 1);
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

    BasePopupHelper setClipChildren(boolean clipChildren) {
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

    BasePopupHelper getAnchorLocation(View v) {
        if (v == null) return this;
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        mAnchorViewBound.set(location[0], location[1], location[0] + v.getWidth(), location[1] + v.getHeight());
        return this;
    }

    public Rect getAnchorViewBound() {
        return mAnchorViewBound;
    }

    Point getTempOffset() {
        return mTempOffset;
    }

    Point getTempOffset(int x, int y) {
        mTempOffset.set(x, y);
        return mTempOffset;
    }

    boolean isBackPressEnable() {
        return (flag & BACKPRESS_ENABLE) != 0;
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

    BasePopupHelper setForceAdjustKeyboard(boolean adjust) {
        setFlag(KEYBOARD_FORCE_ADJUST, adjust);
        return this;
    }

    boolean isAllowToBlur() {
        return mBlurOption != null && mBlurOption.isAllowToBlur();
    }


    boolean isClipChildren() {
        return (flag & CLIP_CHILDREN) != 0;
    }

    /**
     * non null
     */
    ViewGroup.MarginLayoutParams getLayoutParams() {
        if (layoutParams == null) {
            int w = popupViewWidth == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : popupViewWidth;
            int h = popupViewHeight == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : popupViewHeight;
            layoutParams = new ViewGroup.MarginLayoutParams(w, h);
        }
        return layoutParams;
    }

    int getShowCount() {
        return showCount;
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

    BasePopupHelper resize(boolean keep) {
        setFlag(FITSIZE, keep);
        return this;
    }

    boolean isResizeable() {
        return (flag & FITSIZE) != 0;
    }

    //-----------------------------------------controller-----------------------------------------
    void prepare(View v, boolean positionMode) {
        if (mShowInfo == null) {
            mShowInfo = new InnerShowInfo(v, positionMode);
        } else {
            mShowInfo.mAnchorView = new WeakReference<>(v);
            mShowInfo.positionMode = positionMode;
        }
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

    void setFlag(int flag, boolean added) {
        if (!added) {
            this.flag &= ~flag;
        } else {
            this.flag |= flag;
            if (flag == AUTO_LOCATED) {
                this.flag |= AS_DROP_DOWN;
            }
        }
    }

    boolean onDispatchKeyEvent(KeyEvent event) {
        return popupWindow.onDispatchKeyEvent(event);
    }

    boolean onInterceptTouchEvent(MotionEvent event) {
        return popupWindow.onInterceptTouchEvent(event);
    }

    boolean onTouchEvent(MotionEvent event) {
        return popupWindow.onTouchEvent(event);
    }

    boolean onBackPressed() {
        return popupWindow.onBackPressed();
    }

    boolean onOutSideTouch() {
        return popupWindow.onOutSideTouch();
    }

    void onShow() {
        if ((flag & CUSTOM_ON_UPDATE) != 0) return;
        if (mShowAnimation == null || mShowAnimator == null) {
            popupWindow.mDisplayAnimateView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    startShowAnimate(popupWindow.mDisplayAnimateView.getWidth(), popupWindow.mDisplayAnimateView.getHeight());
                    popupWindow.mDisplayAnimateView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        } else {
            startShowAnimate(popupWindow.mDisplayAnimateView.getWidth(), popupWindow.mDisplayAnimateView.getHeight());
        }
        if (isAutoShowInputMethod()) {
            if (mAutoShowInputEdittext != null) {
                mAutoShowInputEdittext.requestFocus();
                KeyboardUtils.open(mAutoShowInputEdittext, 350);
            } else {
                KeyboardUtils.open(popupWindow.getContentView(), 350);
            }
        }
        handleShow();
    }


    void dismiss(boolean animateDismiss) {
        if (mOnDismissListener != null && !mOnDismissListener.onBeforeDismiss()) {
            return;
        }
        if (popupWindow.mDisplayAnimateView == null || animateDismiss && (flag & CUSTOM_ON_ANIMATE_DISMISS) != 0) {
            return;
        }
        if (isAutoShowInputMethod()) {
            KeyboardUtils.close(popupWindow.getContext());
        }
        Message msg = BasePopupEvent.getMessage(BasePopupEvent.EVENT_DISMISS);
        if (animateDismiss) {
            startDismissAnimate(popupWindow.mDisplayAnimateView.getWidth(), popupWindow.mDisplayAnimateView.getHeight());
            msg.arg1 = 1;
            popupWindow.mDisplayAnimateView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    flag &= ~CUSTOM_ON_ANIMATE_DISMISS;
                    popupWindow.superDismiss();
                }
            }, Math.max(dismissDuration, 0));
        } else {
            msg.arg1 = 0;
            popupWindow.superDismiss();
        }
        sendEvent(msg);
    }

    void forceDismiss() {
        if (mDismissAnimation != null) mDismissAnimation.cancel();
        if (mDismissAnimator != null) mDismissAnimator.cancel();
        KeyboardUtils.close(popupWindow.getContext());
        flag &= ~CUSTOM_ON_ANIMATE_DISMISS;
        popupWindow.superDismiss();
    }

    void onAutoLocationChange(int oldGravity, int newGravity) {
        PopupLog.i("onAutoLocationChange", oldGravity, newGravity);
    }

    void onAnchorTop() {
    }

    void onAnchorBottom() {
    }

    @Override
    public void onKeyboardChange(Rect keyboardBounds, boolean isVisible) {
        if (mKeyboardStateChangeListener != null) {
            mKeyboardStateChangeListener.onKeyboardChange(keyboardBounds, isVisible);
        }
    }

    void update(View v, boolean positionMode) {
        if (!popupWindow.isShowing() || popupWindow.mContentView == null) return;
        prepare(v, positionMode);
        popupWindow.mPopupWindow.update();
    }

    void onUpdate() {
        if (mShowInfo != null) {
            prepare(mShowInfo.mAnchorView == null ? null : mShowInfo.mAnchorView.get(), mShowInfo.positionMode);
        }
    }

    void dispatchOutSideEvent(MotionEvent event) {
        if (popupWindow != null) {
            popupWindow.dispatchOutSideEvent(event);
        }
    }
}
