package razerdp.basepopup;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
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

import razerdp.blur.PopupBlurOption;
import razerdp.interceptor.PopupWindowEventInterceptor;
import razerdp.library.R;

/**
 * Created by 大灯泡 on 2017/12/12.
 * <p>
 * popupoption
 */
final class BasePopupHelper implements PopupTouchController, PopupWindowActionListener, PopupWindowLocationListener, PopupKeyboardStateChangeListener {

    public static final int CONTENT_VIEW_ID = R.id.base_popup_content_root;

    static final int DEFAULT_WIDTH = ViewGroup.LayoutParams.WRAP_CONTENT;
    static final int DEFAULT_HEIGHT = ViewGroup.LayoutParams.WRAP_CONTENT;

    private int contentRootId = CONTENT_VIEW_ID;

    //是否自动弹出输入框(default:false)
    private boolean autoShowInputMethod = false;
    private static int showCount;

    //anima
    private Animation mShowAnimation;
    private Animator mShowAnimator;
    private Animation mDismissAnimation;
    private Animator mDismissAnimator;

    //callback
    private BasePopupWindow.OnDismissListener mOnDismissListener;
    private BasePopupWindow.OnBeforeShowCallback mOnBeforeShowCallback;

    //option
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

    //是否自动适配popup的位置
    private boolean isAutoLocatePopup;
    //是否showAsDropDown
    private boolean isShowAsDropDown;
    //点击popup外部是否消失
    private boolean dismissWhenTouchOutside = true;
    //是否全屏
    private boolean fullScreen = true;
    //是否需要淡入window动画
    private volatile boolean isPopupFadeEnable = true;
    //是否禁止后退键dismiss
    private boolean backPressEnable = true;
    //popup params
    private boolean interceptOutSideTouchEvent = true;
    //模糊option(为空的话则不模糊）
    private PopupBlurOption mBlurOption;
    //背景层是否对齐popup
    private boolean mAlignBackground = false;
    //背景颜色
    private Drawable mBackgroundDrawable = new ColorDrawable(Color.parseColor("#8f000000"));
    //背景对齐方向
    private int alignBackgroundGravity = Gravity.TOP;
    //背景View
    private View mBackgroundView;

    private PopupTouchController mTouchControllerDelegate;
    private PopupWindowActionListener mActionListener;
    private PopupWindowLocationListener mLocationListener;
    private PopupKeyboardStateChangeListener mKeyboardStateChangeListener;
    private PopupWindowEventInterceptor mEventInterceptor;


    private boolean mClipChildren = true;
    private boolean mClipToScreen = true;

    private int mSoftInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
    private ViewGroup.MarginLayoutParams mParaseFromXmlParams;
    private Point mOffsetCached = new Point();
    private Point mTempOffset = new Point();

    private boolean isCustomMeasureWidth;
    private boolean isCustomMeasureHeight;

    private int maxWidth, maxHeight;

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
                    mParaseFromXmlParams = new ViewGroup.MarginLayoutParams((ViewGroup.MarginLayoutParams) childParams);
                    if (isCustomMeasureWidth) {
                        mParaseFromXmlParams.width = popupViewWidth;
                    }
                    if (isCustomMeasureHeight) {
                        mParaseFromXmlParams.height = popupViewHeight;
                    }
                    tempLayout = null;
                    return result;
                }
                mParaseFromXmlParams = new ViewGroup.MarginLayoutParams(childParams);
                if (isCustomMeasureWidth) {
                    mParaseFromXmlParams.width = popupViewWidth;
                }
                if (isCustomMeasureHeight) {
                    mParaseFromXmlParams.height = popupViewHeight;
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
            setPopupGravity(((LinearLayout.LayoutParams) p).gravity);
        } else if (p instanceof FrameLayout.LayoutParams) {
            setPopupGravity(((FrameLayout.LayoutParams) p).gravity);
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
        if (showAnimation != null && mBlurOption != null && mBlurOption.getBlurInDuration() <= 0) {
            mBlurOption.setBlurInDuration(showAnimation.getDuration());
        }
        mShowAnimation = showAnimation;
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
        if (showAnimator != null && mBlurOption != null && mBlurOption.getBlurInDuration() <= 0) {
            mBlurOption.setBlurInDuration(showAnimator.getDuration());
        }
        mShowAnimator = showAnimator;
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
        if (dismissAnimation != null && mBlurOption != null && mBlurOption.getBlurOutDuration() <= 0) {
            mBlurOption.setBlurOutDuration(dismissAnimation.getDuration());
        }
        mDismissAnimation = dismissAnimation;
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
        if (dismissAnimator != null && mBlurOption != null && mBlurOption.getBlurOutDuration() <= 0) {
            mBlurOption.setBlurOutDuration(dismissAnimator.getDuration());
        }
        mDismissAnimator = dismissAnimator;
        return this;
    }

    public boolean isCustomMeasure() {
        return isCustomMeasureWidth || isCustomMeasureHeight;
    }

    int getPopupViewWidth() {
        if (isCustomMeasureWidth) {
            return popupViewWidth;
        } else {
            if (mParaseFromXmlParams != null) {
                return mParaseFromXmlParams.width;
            }
        }
        return popupViewWidth;
    }

    BasePopupHelper setPopupViewWidth(int popupViewWidth) {
        this.popupViewWidth = popupViewWidth;
        if (popupViewWidth != DEFAULT_WIDTH) {
            isCustomMeasureWidth = true;
            if (mParaseFromXmlParams != null) {
                mParaseFromXmlParams.width = popupViewWidth;
            }
        } else {
            isCustomMeasureWidth = false;
        }
        return this;
    }

    int getPopupViewHeight() {
        if (isCustomMeasureHeight) {
            return popupViewHeight;
        } else {
            if (mParaseFromXmlParams != null) {
                return mParaseFromXmlParams.height;
            }
        }
        return popupViewHeight;
    }

    BasePopupHelper setPopupViewHeight(int popupViewHeight) {
        this.popupViewHeight = popupViewHeight;
        if (popupViewHeight != DEFAULT_HEIGHT) {
            isCustomMeasureHeight = true;
            if (mParaseFromXmlParams != null) {
                mParaseFromXmlParams.height = popupViewHeight;
            }
        } else {
            isCustomMeasureHeight = false;
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
        return isPopupFadeEnable;
    }

    BasePopupHelper setPopupFadeEnable(PopupWindow popupWindow, boolean needPopupFadeAnima) {
        if (popupWindow == null) return this;
        this.isPopupFadeEnable = needPopupFadeAnima;
        return this;
    }

    boolean isShowAsDropDown() {
        return isShowAsDropDown;
    }

    BasePopupHelper setShowAsDropDown(boolean showAsDropDown) {
        this.isShowAsDropDown = showAsDropDown;
        return this;
    }

    BasePopupHelper setShowLocation(int x, int y) {
        mAnchorViewLocation[0] = x;
        mAnchorViewLocation[1] = y;
        mAnchorViewWidth = 1;
        mAnchorViewHeight = 1;
        return this;
    }

    int getPopupGravity() {
        return popupGravity;
    }

    BasePopupHelper setPopupGravity(int popupGravity) {
        if (popupGravity == this.popupGravity) return this;
        this.popupGravity = popupGravity;
        return this;
    }

    public BasePopupHelper setClipChildren(boolean clipChildren) {
        mClipChildren = clipChildren;
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
        return autoShowInputMethod;
    }

    BasePopupHelper setAutoShowInputMethod(PopupWindow popupWindow, boolean autoShowInputMethod) {
        if (popupWindow == null) return this;
        this.autoShowInputMethod = autoShowInputMethod;
        popupWindow.setSoftInputMode(autoShowInputMethod ? WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE : WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED);
        return this;
    }

    BasePopupHelper setSoftInputMode(int inputMethodType) {
        mSoftInputMode = inputMethodType;
        return this;
    }

    boolean isAutoLocatePopup() {
        return isAutoLocatePopup;
    }

    BasePopupHelper setAutoLocatePopup(boolean autoLocatePopup) {
        isAutoLocatePopup = autoLocatePopup;
        if (autoLocatePopup) {
            isShowAsDropDown = true;
        }
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

    boolean isDismissWhenTouchOutside() {
        return dismissWhenTouchOutside;
    }

    BasePopupHelper setDismissWhenTouchOutside(PopupWindow popupWindow, boolean dismissWhenTouchOutside) {
        if (popupWindow == null) return this;
        this.dismissWhenTouchOutside = dismissWhenTouchOutside;
        return this;
    }

    boolean isInterceptTouchEvent() {
        return interceptOutSideTouchEvent;
    }

    BasePopupHelper setInterceptTouchEvent(PopupWindow popupWindow, boolean intecept) {
        if (popupWindow == null) return this;
        interceptOutSideTouchEvent = intecept;
        return this;
    }

    BasePopupHelper setClipToScreen(boolean clipToScreen) {
        mClipToScreen = clipToScreen;
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

    BasePopupHelper cacheOffset(Point offset) {
        if (offset == null) return this;
        this.mOffsetCached.set(offset.x, offset.y);
        return this;
    }

    Point getCachedOffset() {
        return mOffsetCached;
    }

    public Point getTempOffset() {
        return mTempOffset;
    }

    public Point getTempOffset(int x, int y) {
        mTempOffset.set(x, y);
        return mTempOffset;
    }

    int getAnchorViewWidth() {
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
        return backPressEnable;
    }

    boolean isClipToScreen() {
        return mClipToScreen;
    }

    BasePopupHelper setBackPressEnable(PopupWindow popupWindow, boolean backPressEnable) {
        if (popupWindow == null) return this;
        this.backPressEnable = backPressEnable;
        return this;
    }

    boolean isFullScreen() {
        return fullScreen;
    }

    BasePopupHelper setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
        return this;
    }

    PopupBlurOption getBlurOption() {
        return mBlurOption;
    }

    BasePopupHelper applyBlur(PopupBlurOption option) {
        this.mBlurOption = option;
        return this;
    }

    long getShowAnimationDuration() {
        long duration = 0;
        if (mShowAnimation != null) {
            duration = mShowAnimation.getDuration();
        } else if (mShowAnimator != null) {
            duration = mShowAnimator.getDuration();
        }
        return duration < 0 ? 500 : duration;
    }

    long getExitAnimationDuration() {
        long duration = 0;
        if (mDismissAnimation != null) {
            duration = mDismissAnimation.getDuration();
        } else if (mDismissAnimator != null) {
            duration = mDismissAnimator.getDuration();
        }
        return duration < 0 ? 500 : duration;
    }

    Drawable getPopupBackground() {
        return mBackgroundDrawable;
    }

    BasePopupHelper setPopupBackground(Drawable background) {
        mBackgroundDrawable = background;
        return this;
    }

    boolean isAlignBackground() {
        return mAlignBackground;
    }

    BasePopupHelper setAlignBackgound(boolean mAlignBackground) {
        this.mAlignBackground = mAlignBackground;
        if (!mAlignBackground) {
            setAlignBackgroundGravity(Gravity.NO_GRAVITY);
        }
        return this;
    }

    int getAlignBackgroundGravity() {
        if (mAlignBackground && alignBackgroundGravity == Gravity.NO_GRAVITY) {
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
        return mClipChildren;
    }

    ViewGroup.MarginLayoutParams getParaseFromXmlParams() {
        return mParaseFromXmlParams;
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

    //-----------------------------------------controller-----------------------------------------
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
    public void onKeyboardChange(int keyboardHeight, boolean isVisible) {
        if (mKeyboardStateChangeListener != null) {
            mKeyboardStateChangeListener.onKeyboardChange(keyboardHeight, isVisible);
        }
    }
}
