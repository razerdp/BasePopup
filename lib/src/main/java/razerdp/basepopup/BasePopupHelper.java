package razerdp.basepopup;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Message;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.Map;
import java.util.WeakHashMap;

import razerdp.blur.PopupBlurOption;
import razerdp.library.R;
import razerdp.util.KeyboardUtils;
import razerdp.util.PopupUiUtils;
import razerdp.util.PopupUtils;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2017/12/12.
 * <p>
 * PopupHelper，这货与Popup强引用哦~
 */
@SuppressWarnings("all")
final class BasePopupHelper implements KeyboardUtils.OnKeyboardChangeListener, BasePopupFlag, ClearMemoryObject {

    BasePopupWindow mPopupWindow;

    WeakHashMap<Object, BasePopupEvent.EventObserver> eventObserverMap;

    enum ShowMode {
        RELATIVE_TO_ANCHOR,
        SCREEN,
        POSITION
    }

    static final long DEFAULT_KEYBOARD_SHOW_DELAY = 350;
    static final int DEFAULT_OVERLAY_STATUS_BAR_MODE = OVERLAY_MASK | OVERLAY_CONTENT;
    static final int DEFAULT_OVERLAY_NAVIGATION_BAR_MODE = OVERLAY_MASK;
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
    boolean preventInitShowAnimation;
    boolean preventInitDismissAnimation;

    Animation mMaskViewShowAnimation;
    Animation mMaskViewDismissAnimation;

    boolean isDefaultMaskViewShowAnimation;
    boolean isDefaultMaskViewDismissAnimation;

    long showDuration;
    long dismissDuration;
    long showKeybaordDelay = DEFAULT_KEYBOARD_SHOW_DELAY;

    int animationStyleRes;

    boolean isStartShowing = false;
    //callback
    BasePopupWindow.OnDismissListener mOnDismissListener;
    BasePopupWindow.OnBeforeShowCallback mOnBeforeShowCallback;
    BasePopupWindow.OnPopupWindowShowListener mOnPopupWindowShowListener;

    //option
    BasePopupWindow.GravityMode horizontalGravityMode = BasePopupWindow.GravityMode.RELATIVE_TO_ANCHOR;
    BasePopupWindow.GravityMode verticalGravityMode = BasePopupWindow.GravityMode.RELATIVE_TO_ANCHOR;

    int popupGravity = Gravity.NO_GRAVITY;
    int offsetX;
    int offsetY;
    int maskOffsetX;
    int maskOffsetY;
    int preMeasureWidth;
    int preMeasureHeight;

    int popupViewWidth = 0;
    int popupViewHeight = 0;
    int layoutDirection = LayoutDirection.LTR;
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
    KeyboardUtils.OnKeyboardChangeListener mUserKeyboardStateChangeListener;
    BasePopupWindow.KeyEventListener mKeyEventListener;

    int mSoftInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
    ViewGroup.MarginLayoutParams layoutParams;

    int maxWidth, maxHeight, minWidth, minHeight;

    int keybaordAlignViewId;
    View keybaordAlignView;

    InnerShowInfo mShowInfo;

    ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;
    LinkedViewLayoutChangeListenerWrapper mLinkedViewLayoutChangeListenerWrapper;

    View mLinkedTarget;

    Rect navigationBarBounds;
    Rect cutoutSafeRect;

    int lastOverLayStatusBarMode, overlayStatusBarMode = DEFAULT_OVERLAY_STATUS_BAR_MODE;
    int lastOverlayNavigationBarMode, overlayNavigationBarMode = DEFAULT_OVERLAY_NAVIGATION_BAR_MODE;


    //unsafe
    BasePopupUnsafe.OnFitWindowManagerLayoutParamsCallback mOnFitWindowManagerLayoutParamsCallback;

    BasePopupHelper(BasePopupWindow popupWindow) {
        mAnchorViewBound = new Rect();
        navigationBarBounds = new Rect();
        cutoutSafeRect = new Rect();
        this.mPopupWindow = popupWindow;
        this.eventObserverMap = new WeakHashMap<>();
        this.mMaskViewShowAnimation = new AlphaAnimation(0f, 1f);
        this.mMaskViewDismissAnimation = new AlphaAnimation(1f, 0f);
        this.mMaskViewShowAnimation.setFillAfter(true);
        this.mMaskViewShowAnimation.setInterpolator(new DecelerateInterpolator());
        this.mMaskViewShowAnimation.setDuration(Resources.getSystem()
                .getInteger(android.R.integer.config_shortAnimTime));
        isDefaultMaskViewShowAnimation = true;
        this.mMaskViewDismissAnimation.setFillAfter(true);
        this.mMaskViewDismissAnimation.setInterpolator(new DecelerateInterpolator());
        this.mMaskViewDismissAnimation.setDuration(Resources.getSystem()
                .getInteger(android.R.integer.config_shortAnimTime));
        isDefaultMaskViewDismissAnimation = true;
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
            int measureWidth = View.MeasureSpec.makeMeasureSpec(Math.max(w, 0),
                    w == ViewGroup.LayoutParams.WRAP_CONTENT ? View.MeasureSpec.UNSPECIFIED : View.MeasureSpec.EXACTLY);
            int measureHeight = View.MeasureSpec.makeMeasureSpec(Math.max(w, h),
                    h == ViewGroup.LayoutParams.WRAP_CONTENT ? View.MeasureSpec.UNSPECIFIED : View.MeasureSpec.EXACTLY);
            mContentView.measure(measureWidth, measureHeight);
            preMeasureWidth = mContentView.getMeasuredWidth();
            preMeasureHeight = mContentView.getMeasuredHeight();
            mContentView.setFocusableInTouchMode(true);
        }
    }

    void checkAndSetGravity(ViewGroup.LayoutParams p) {
        //如果设置过gravity，则采取设置的gravity，顶替掉xml设置的（针对lazypopup）
        //https://github.com/razerdp/BasePopup/issues/310
        if (p == null || this.popupGravity != Gravity.NO_GRAVITY) return;
        if (p instanceof LinearLayout.LayoutParams) {
            this.popupGravity = ((LinearLayout.LayoutParams) p).gravity;
        } else if (p instanceof FrameLayout.LayoutParams) {
            this.popupGravity = ((FrameLayout.LayoutParams) p).gravity;
        }
    }

    //region Animation

    void startShowAnimate(int width, int height) {
        if (!preventInitShowAnimation) {
            if (initShowAnimation(width, height) == null) {
                initShowAnimator(width, height);
            }
        }
        //动画只初始化一次，后续请自行通过setAnimation/setAnimator实现
        preventInitShowAnimation = true;
        //通知蒙层动画，此时duration已经计算完毕
        Message msg = Message.obtain();
        msg.what = BasePopupEvent.EVENT_SHOW;
        sendEvent(msg);
        if (mShowAnimation != null) {
            mShowAnimation.cancel();
            mPopupWindow.mDisplayAnimateView.startAnimation(mShowAnimation);
        } else if (mShowAnimator != null) {
            mShowAnimator.setTarget(mPopupWindow.getDisplayAnimateView());
            mShowAnimator.cancel();
            mShowAnimator.start();
        }
    }

    void startDismissAnimate(int width, int height) {
        if (!preventInitDismissAnimation) {
            if (initDismissAnimation(width, height) == null) {
                initDismissAnimator(width, height);
            }
        }
        //动画只初始化一次，后续请自行通过setAnimation/setAnimator实现
        preventInitDismissAnimation = true;
        if (mDismissAnimation != null) {
            mDismissAnimation.cancel();
            mPopupWindow.mDisplayAnimateView.startAnimation(mDismissAnimation);
            if (mOnDismissListener != null) {
                mOnDismissListener.onDismissAnimationStart();
            }
            setFlag(CUSTOM_ON_ANIMATE_DISMISS, true);
        } else if (mDismissAnimator != null) {
            mDismissAnimator.setTarget(mPopupWindow.getDisplayAnimateView());
            mDismissAnimator.cancel();
            mDismissAnimator.start();
            if (mOnDismissListener != null) {
                mOnDismissListener.onDismissAnimationStart();
            }
            setFlag(CUSTOM_ON_ANIMATE_DISMISS, true);
        }
    }

    Animation initShowAnimation(int width, int height) {
        if (mShowAnimation == null) {
            mShowAnimation = mPopupWindow.onCreateShowAnimation(width, height);
            if (mShowAnimation != null) {
                showDuration = PopupUtils.getAnimationDuration(mShowAnimation, 0);
                setToBlur(mBlurOption);
            }
        }
        return mShowAnimation;
    }

    Animator initShowAnimator(int width, int height) {
        if (mShowAnimator == null) {
            mShowAnimator = mPopupWindow.onCreateShowAnimator(width, height);
            if (mShowAnimator != null) {
                showDuration = PopupUtils.getAnimatorDuration(mShowAnimator, 0);
                setToBlur(mBlurOption);
            }
        }
        return mShowAnimator;
    }

    Animation initDismissAnimation(int width, int height) {
        if (mDismissAnimation == null) {
            mDismissAnimation = mPopupWindow.onCreateDismissAnimation(width, height);
            if (mDismissAnimation != null) {
                dismissDuration = PopupUtils.getAnimationDuration(mDismissAnimation, 0);
                setToBlur(mBlurOption);
            }
        }
        return mDismissAnimation;
    }

    Animator initDismissAnimator(int width, int height) {
        if (mDismissAnimator == null) {
            mDismissAnimator = mPopupWindow.onCreateDismissAnimator(width, height);
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


    int getPreMeasureHeight() {
        return preMeasureHeight;
    }

    boolean isPopupFadeEnable() {
        return (flag & FADE_ENABLE) != 0;
    }

    boolean isWithAnchor() {
        return (flag & WITH_ANCHOR) != 0;
    }

    boolean isFitsizable() {
        return (flag & FITSIZE) != 0;
    }

    BasePopupHelper withAnchor(boolean showAsDropDown) {
        setFlag(WITH_ANCHOR, showAsDropDown);
        return this;
    }

    BasePopupHelper setShowLocation(int x, int y) {
        mAnchorViewBound.set(x, y, x + 1, y + 1);
        return this;
    }


    int getPopupGravity() {
        return Gravity.getAbsoluteGravity(popupGravity, layoutDirection);
    }

    BasePopupHelper setLayoutDirection(int layoutDirection) {
        this.layoutDirection = layoutDirection;
        return this;
    }

    BasePopupHelper setPopupGravity(BasePopupWindow.GravityMode mode, int popupGravity) {
        setPopupGravityMode(mode, mode);
        this.popupGravity = popupGravity;
        return this;
    }

    BasePopupHelper setPopupGravityMode(BasePopupWindow.GravityMode horizontalGravityMode,
                                        BasePopupWindow.GravityMode verticalGravityMode) {
        this.horizontalGravityMode = horizontalGravityMode;
        this.verticalGravityMode = verticalGravityMode;
        return this;
    }

    int getOffsetX() {
        return offsetX;
    }

    int getOffsetY() {
        return offsetY;
    }


    boolean isAutoShowInputMethod() {
        return (flag & AUTO_INPUT_METHOD) != 0;
    }

    boolean isAutoLocatePopup() {
        return (flag & AUTO_LOCATED) != 0;
    }

    boolean isOutSideDismiss() {
        return (flag & OUT_SIDE_DISMISS) != 0;
    }

    boolean isOutSideTouchable() {
        return (flag & OUT_SIDE_TOUCHABLE) != 0;
    }

    BasePopupHelper getAnchorLocation(View v) {
        if (v == null) return this;
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        mAnchorViewBound.set(location[0],
                location[1],
                location[0] + v.getWidth(),
                location[1] + v.getHeight());
        return this;
    }

    public Rect getAnchorViewBound() {
        return mAnchorViewBound;
    }

    boolean isBackPressEnable() {
        return (flag & BACKPRESS_ENABLE) != 0;
    }

    boolean isOverlayStatusbar() {
        return (flag & OVERLAY_STATUS_BAR) != 0;
    }

    boolean isOverlayNavigationBar() {
        return (flag & OVERLAY_NAVIGATION_BAR) != 0;
    }

    void refreshNavigationBarBounds() {
        PopupUiUtils.getNavigationBarBounds(navigationBarBounds, mPopupWindow.getContext());
    }

    int getNavigationBarSize() {
        return Math.min(navigationBarBounds.width(), navigationBarBounds.height());
    }

    int getNavigationBarGravity() {
        return PopupUiUtils.getNavigationBarGravity(navigationBarBounds);
    }

    public int getCutoutGravity() {
        getSafeInsetBounds(cutoutSafeRect);
        if (cutoutSafeRect.left > 0) {
            return Gravity.LEFT;
        }
        if (cutoutSafeRect.top > 0) {
            return Gravity.TOP;
        }
        if (cutoutSafeRect.right > 0) {
            return Gravity.RIGHT;
        }
        if (cutoutSafeRect.bottom > 0) {
            return Gravity.BOTTOM;
        }
        return Gravity.NO_GRAVITY;
    }

    void getSafeInsetBounds(Rect r) {
        if (r == null) return;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            r.setEmpty();
            return;
        }
        try {
            DisplayCutout cutout = mPopupWindow.getContext()
                    .getWindow()
                    .getDecorView()
                    .getRootWindowInsets()
                    .getDisplayCutout();
            if (cutout == null) {
                r.setEmpty();
                return;
            }
            r.set(cutout.getSafeInsetLeft(), cutout.getSafeInsetTop(),
                    cutout.getSafeInsetRight(), cutout.getSafeInsetBottom());
        } catch (Exception e) {
            PopupLog.e(e);
        }
    }

    BasePopupHelper overlayStatusbar(boolean overlay) {
        if (!overlay && PopupUiUtils.isActivityFullScreen(mPopupWindow.getContext())) {
            Log.e(BasePopupWindow.TAG, "setOverlayStatusbar: 全屏Activity下没有StatusBar，此处不能设置为false");
            overlay = true;
        }
        setFlag(OVERLAY_STATUS_BAR, overlay);
        if (!overlay) {
            lastOverLayStatusBarMode = overlayStatusBarMode;
            overlayStatusBarMode = 0;
        } else {
            overlayStatusBarMode = lastOverLayStatusBarMode;
        }
        return this;
    }

    BasePopupHelper setOverlayStatusbarMode(int mode) {
        if (!isOverlayStatusbar()) {
            lastOverLayStatusBarMode = mode;
        } else {
            lastOverLayStatusBarMode = overlayStatusBarMode = mode;
        }
        return this;
    }

    BasePopupHelper overlayNavigationBar(boolean overlay) {
        setFlag(OVERLAY_NAVIGATION_BAR, overlay);
        if (!overlay) {
            lastOverlayNavigationBarMode = overlayNavigationBarMode;
            overlayNavigationBarMode = 0;
        } else {
            overlayNavigationBarMode = lastOverlayNavigationBarMode;
        }
        return this;
    }

    BasePopupHelper setOverlayNavigationBarMode(int mode) {
        if (!isOverlayNavigationBar()) {
            lastOverlayNavigationBarMode = mode;
        } else {
            lastOverlayNavigationBarMode = overlayNavigationBarMode = mode;
        }
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
    @NonNull
    ViewGroup.MarginLayoutParams getLayoutParams() {
        if (layoutParams == null) {
            int w = popupViewWidth == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : popupViewWidth;
            int h = popupViewHeight == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : popupViewHeight;
            layoutParams = new ViewGroup.MarginLayoutParams(w, h);
        }
        if (layoutParams.width > 0) {
            if (minWidth > 0) {
                layoutParams.width = Math.max(layoutParams.width, minWidth);
            }
            if (maxWidth > 0) {
                layoutParams.width = Math.min(layoutParams.width, maxWidth);
            }
        }
        if (layoutParams.height > 0) {
            if (minHeight > 0) {
                layoutParams.height = Math.max(layoutParams.height, minHeight);
            }
            if (maxHeight > 0) {
                layoutParams.height = Math.min(layoutParams.height, maxHeight);
            }
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

    int getMaxHeight() {
        return maxHeight;
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

    int getMinHeight() {
        return minHeight;
    }


    boolean isResizeable() {
        return (flag & FITSIZE) != 0;
    }

    public BasePopupHelper linkTo(View anchorView) {
        if (anchorView == null) {
            if (mLinkedViewLayoutChangeListenerWrapper != null) {
                mLinkedViewLayoutChangeListenerWrapper.detach();
                mLinkedViewLayoutChangeListenerWrapper = null;
            }
            mLinkedTarget = null;
            return this;
        }
        mLinkedTarget = anchorView;
        return this;
    }

    boolean isSyncMaskAnimationDuration() {
        return (flag & BasePopupFlag.SYNC_MASK_ANIMATION_DURATION) != 0;
    }

    boolean isAlignAnchorWidth() {
        if (isWithAnchor()) {
            //point mode时，由于是一像素，因此忽略
            if (mShowInfo != null && mShowInfo.positionMode) {
                return false;
            }
            return (flag & BasePopupFlag.AS_WIDTH_AS_ANCHOR) != 0;
        }
        return false;
    }

    boolean isAlignAnchorHeight() {
        if (isWithAnchor()) {
            //point mode时，由于是一像素，因此忽略
            if (mShowInfo != null && mShowInfo.positionMode) {
                return false;
            }
            return (flag & BasePopupFlag.AS_HEIGHT_AS_ANCHOR) != 0;
        }
        return false;
    }

    //-----------------------------------------controller-----------------------------------------
    void prepare(View v, boolean positionMode) {
        if (mShowInfo == null) {
            mShowInfo = new InnerShowInfo(v, positionMode);
        } else {
            mShowInfo.mAnchorView = v;
            mShowInfo.positionMode = positionMode;
        }
        if (positionMode) {
            setShowMode(BasePopupHelper.ShowMode.POSITION);
        } else {
            setShowMode(v == null ? BasePopupHelper.ShowMode.SCREEN : BasePopupHelper.ShowMode.RELATIVE_TO_ANCHOR);
        }
        getAnchorLocation(v);
        applyToPopupWindow();
    }

    private void applyToPopupWindow() {
        if (mPopupWindow == null || mPopupWindow.mPopupWindowProxy == null) return;
        mPopupWindow.mPopupWindowProxy.setSoftInputMode(mSoftInputMode);
        mPopupWindow.mPopupWindowProxy.setAnimationStyle(animationStyleRes);
        mPopupWindow.mPopupWindowProxy.setTouchable((flag & TOUCHABLE) != 0);
        mPopupWindow.mPopupWindowProxy.setFocusable((flag & TOUCHABLE) != 0);
    }

    void onDismiss() {
        if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP ||
                android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
            showCount--;
            showCount = Math.max(0, showCount);
        }
        if (isAutoShowInputMethod()) {
            KeyboardUtils.close(mPopupWindow.getContext());
        }

        if (mLinkedViewLayoutChangeListenerWrapper != null) {
            mLinkedViewLayoutChangeListenerWrapper.detach();
        }
    }

    void setFlag(int flag, boolean added) {
        if (!added) {
            this.flag &= ~flag;
        } else {
            this.flag |= flag;
            if (flag == AUTO_LOCATED) {
                this.flag |= WITH_ANCHOR;
            }
        }
    }

    boolean onDispatchKeyEvent(KeyEvent event) {
        if (mKeyEventListener != null && mKeyEventListener.onKey(event)) {
            return true;
        }
        return mPopupWindow.onDispatchKeyEvent(event);
    }

    boolean onInterceptTouchEvent(MotionEvent event) {
        return mPopupWindow.onInterceptTouchEvent(event);
    }

    boolean onTouchEvent(MotionEvent event) {
        return mPopupWindow.onTouchEvent(event);
    }

    boolean onBackPressed() {
        return mPopupWindow.onBackPressed();
    }

    void onShow() {
        prepareShow();
        if ((flag & CUSTOM_ON_UPDATE) != 0) return;
        if (mShowAnimation == null || mShowAnimator == null) {
            mPopupWindow.mDisplayAnimateView.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            mPopupWindow.mDisplayAnimateView.getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(
                                            this);
                            startShowAnimate(mPopupWindow.mDisplayAnimateView.getWidth(),
                                    mPopupWindow.mDisplayAnimateView.getHeight());
                        }
                    });
        } else {
            startShowAnimate(mPopupWindow.mDisplayAnimateView.getWidth(),
                    mPopupWindow.mDisplayAnimateView.getHeight());
        }
        //针对官方的坑（两个popup切换页面后重叠）
        if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP ||
                android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
            showCount++;
        }
    }

    void onAttachToWindow() {
        isStartShowing = false;
        if (mPopupWindow != null) {
            mPopupWindow.onShowing();
        }
        if (mOnPopupWindowShowListener != null) {
            mOnPopupWindowShowListener.onShowing();
        }
    }

    void onPopupLayout(@NonNull Rect popupRect, @NonNull Rect anchorRect) {
        if (mPopupWindow != null) {
            mPopupWindow.onPopupLayout(popupRect, anchorRect);
        }
    }

    private void prepareShow() {
        if (mGlobalLayoutListener == null) {
            mGlobalLayoutListener = KeyboardUtils.observerKeyboardChange(mPopupWindow.getContext(),
                    new KeyboardUtils.OnKeyboardChangeListener() {
                        @Override
                        public void onKeyboardChange(Rect keyboardBounds, boolean isVisible) {
                            BasePopupHelper.this.onKeyboardChange(
                                    keyboardBounds,
                                    isVisible);
                            if (!mPopupWindow.isShowing()) {
                                PopupUiUtils.safeRemoveGlobalLayoutListener(
                                        mPopupWindow.getContext()
                                                .getWindow()
                                                .getDecorView(),
                                        mGlobalLayoutListener);
                                return;
                            }
                        }
                    });
        }
        PopupUiUtils.safeAddGlobalLayoutListener(mPopupWindow.getContext()
                        .getWindow()
                        .getDecorView(),
                mGlobalLayoutListener);

        if (mLinkedTarget != null) {
            if (mLinkedViewLayoutChangeListenerWrapper == null) {
                mLinkedViewLayoutChangeListenerWrapper = new LinkedViewLayoutChangeListenerWrapper(
                        mLinkedTarget);
            }
            if (!mLinkedViewLayoutChangeListenerWrapper.isAdded) {
                mLinkedViewLayoutChangeListenerWrapper.attach();
            }
        }
    }


    void dismiss(boolean animateDismiss) {
        if (mPopupWindow == null || !mPopupWindow.onBeforeDismissInternal(mOnDismissListener)) {
            return;
        }
        if (mPopupWindow.mDisplayAnimateView == null || animateDismiss && (flag & CUSTOM_ON_ANIMATE_DISMISS) != 0) {
            return;
        }
        isStartShowing = false;
        Message msg = BasePopupEvent.getMessage(BasePopupEvent.EVENT_DISMISS);
        if (animateDismiss) {
            startDismissAnimate(mPopupWindow.mDisplayAnimateView.getWidth(),
                    mPopupWindow.mDisplayAnimateView.getHeight());
            msg.arg1 = 1;
            mPopupWindow.mDisplayAnimateView.removeCallbacks(dismissAnimationDelayRunnable);
            mPopupWindow.mDisplayAnimateView.postDelayed(dismissAnimationDelayRunnable,
                    Math.max(dismissDuration, 0));
        } else {
            msg.arg1 = 0;
            mPopupWindow.superDismiss();
        }
        BasePopupUnsafe.StackFetcher.remove(mPopupWindow);
        sendEvent(msg);
    }

    private Runnable dismissAnimationDelayRunnable = new Runnable() {
        @Override
        public void run() {
            flag &= ~CUSTOM_ON_ANIMATE_DISMISS;
            if (mPopupWindow != null) {
                //popup可能已经释放引用了
                mPopupWindow.superDismiss();
            }
        }
    };

    void forceDismiss() {
        if (mDismissAnimation != null) mDismissAnimation.cancel();
        if (mDismissAnimator != null) mDismissAnimator.cancel();
        if (mPopupWindow != null) {
            KeyboardUtils.close(mPopupWindow.getContext());
        }
        if (dismissAnimationDelayRunnable != null) {
            dismissAnimationDelayRunnable.run();
        }
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
        if (mUserKeyboardStateChangeListener != null) {
            mUserKeyboardStateChangeListener.onKeyboardChange(keyboardBounds, isVisible);
        }
    }

    void update(View v, boolean positionMode) {
        if (!mPopupWindow.isShowing() || mPopupWindow.mContentView == null) return;
        if (v == null && mShowInfo != null) {
            v = mShowInfo.mAnchorView;
        }
        prepare(v, positionMode);
        mPopupWindow.mPopupWindowProxy.update();
    }

    void dispatchOutSideEvent(MotionEvent event, boolean touchInMask, boolean isMaskPressed) {
        if (mPopupWindow != null) {
            mPopupWindow.dispatchOutSideEvent(event, touchInMask, isMaskPressed);
        }
    }

    static class InnerShowInfo {
        View mAnchorView;
        boolean positionMode;

        InnerShowInfo(View mAnchorView, boolean positionMode) {
            this.mAnchorView = mAnchorView;
            this.positionMode = positionMode;
        }
    }

    class LinkedViewLayoutChangeListenerWrapper implements ViewTreeObserver.OnPreDrawListener {

        private View mTarget;
        private boolean isAdded;
        private float lastX, lastY;
        private int lastWidth, lastHeight, lastVisible;
        private boolean lastShowState, hasChange;
        Rect lastLocationRect = new Rect();
        Rect newLocationRect = new Rect();

        public LinkedViewLayoutChangeListenerWrapper(View target) {
            mTarget = target;
        }

        void attach() {
            if (mTarget == null || isAdded) return;
            mTarget.getGlobalVisibleRect(lastLocationRect);
            refreshViewParams();
            mTarget.getViewTreeObserver().addOnPreDrawListener(this);
            isAdded = true;
        }

        void detach() {
            if (mTarget == null || !isAdded) return;
            try {
                mTarget.getViewTreeObserver().removeOnPreDrawListener(this);
            } catch (Exception e) {
            }
            isAdded = false;
        }

        void refreshViewParams() {
            if (mTarget == null) return;

            //之所以不直接用getGlobalVisibleRect，是因为getGlobalVisibleRect需要不断的找到parent然后获取位置，因此先比较自身属性，然后进行二次验证
            float curX = mTarget.getX();
            float curY = mTarget.getY();
            int curWidth = mTarget.getWidth();
            int curHeight = mTarget.getHeight();
            int curVisible = mTarget.getVisibility();
            boolean isShow = mTarget.isShown();

            hasChange = (curX != lastX ||
                    curY != lastY ||
                    curWidth != lastWidth ||
                    curHeight != lastHeight ||
                    curVisible != lastVisible) && isAdded;
            if (!hasChange) {
                //不排除是recyclerview中那样子的情况，因此这里进行二次验证，获取view在屏幕中的位置
                mTarget.getGlobalVisibleRect(newLocationRect);
                if (!newLocationRect.equals(lastLocationRect)) {
                    lastLocationRect.set(newLocationRect);
                    //处理可能的在recyclerview回收的事情
                    if (!handleShowChange(mTarget, lastShowState, isShow)) {
                        hasChange = true;
                    }
                }
            }

            lastX = curX;
            lastY = curY;
            lastWidth = curWidth;
            lastHeight = curHeight;
            lastVisible = curVisible;
            lastShowState = isShow;
        }

        private boolean handleShowChange(View target, boolean lastShowState, boolean isShow) {
            if (lastShowState && !isShow) {
                if (mPopupWindow.isShowing()) {
                    dismiss(false);
                    return true;
                }
            } else if (!lastShowState && isShow) {
                if (!mPopupWindow.isShowing()) {
                    mPopupWindow.tryToShowPopup(target, false);
                    return true;
                }
            }
            return false;
        }


        @Override
        public boolean onPreDraw() {
            if (mTarget == null) return true;
            refreshViewParams();
            if (hasChange) {
                update(mTarget, false);
            }
            return true;
        }
    }

    @Nullable
    static Activity findActivity(Object parent) {
        return findActivity(parent, true);
    }

    @Nullable
    static Activity findActivity(Object parent, boolean returnTopIfNull) {
        Activity act = null;
        if (parent instanceof Context) {
            act = PopupUtils.getActivity((Context) parent);
        } else if (parent instanceof Fragment) {
            act = ((Fragment) parent).getActivity();
        } else if (parent instanceof Dialog) {
            act = PopupUtils.getActivity(((Dialog) parent).getContext());
        }
        if (act == null && returnTopIfNull) {
            act = BasePopupSDK.getInstance().getTopActivity();
        }
        return act;
    }

    @Nullable
    static View findDecorView(Object parent) {
        View decorView = null;
        Window window = null;
        if (parent instanceof Dialog) {
            window = ((Dialog) parent).getWindow();
        } else if (parent instanceof DialogFragment) {
            if (((DialogFragment) parent).getDialog() == null) {
                decorView = ((DialogFragment) parent).getView();
            } else {
                window = ((DialogFragment) parent).getDialog().getWindow();
            }
        } else if (parent instanceof Fragment) {
            decorView = ((Fragment) parent).getView();
        } else if (parent instanceof Context) {
            Activity act = PopupUtils.getActivity((Context) parent);
            decorView = act == null ? null : act.findViewById(android.R.id.content);
        }

        if (decorView != null) {
            return decorView;
        } else {
            return window == null ? null : window.getDecorView();
        }
    }

    @Override
    public void clear(boolean destroy) {
        if (mPopupWindow != null && mPopupWindow.mDisplayAnimateView != null) {
            //神奇的是，这个方式有可能失效，runnable根本就没有被remove掉
            mPopupWindow.mDisplayAnimateView.removeCallbacks(dismissAnimationDelayRunnable);
        }
        if (eventObserverMap != null) {
            eventObserverMap.clear();
        }
        PopupUiUtils.releaseAnimation(mShowAnimation, mDismissAnimation, mShowAnimator, mDismissAnimator, mMaskViewShowAnimation, mMaskViewDismissAnimation);
        if (mBlurOption != null) {
            mBlurOption.clear();
        }
        if (mShowInfo != null) {
            mShowInfo.mAnchorView = null;
        }
        if (mGlobalLayoutListener != null) {
            PopupUiUtils.safeRemoveGlobalLayoutListener(mPopupWindow.getContext()
                            .getWindow()
                            .getDecorView(),
                    mGlobalLayoutListener);
        }

        if (mLinkedViewLayoutChangeListenerWrapper != null) {
            mLinkedViewLayoutChangeListenerWrapper.detach();
        }

        dismissAnimationDelayRunnable = null;
        mShowAnimation = null;
        mDismissAnimation = null;
        mShowAnimator = null;
        mDismissAnimator = null;
        mMaskViewShowAnimation = null;
        mMaskViewDismissAnimation = null;
        eventObserverMap = null;
        mPopupWindow = null;
        mOnPopupWindowShowListener = null;
        mOnDismissListener = null;
        mOnBeforeShowCallback = null;
        mBlurOption = null;
        mBackgroundDrawable = null;
        mBackgroundView = null;
        mAutoShowInputEdittext = null;
        mKeyboardStateChangeListener = null;
        mShowInfo = null;
        mLinkedViewLayoutChangeListenerWrapper = null;
        mLinkedTarget = null;
        mGlobalLayoutListener = null;
        mUserKeyboardStateChangeListener = null;
        mKeyEventListener = null;
        keybaordAlignView = null;
        mOnFitWindowManagerLayoutParamsCallback = null;
    }
}
