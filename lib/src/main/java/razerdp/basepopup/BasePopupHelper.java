package razerdp.basepopup;

import android.animation.Animator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.PopupWindow;

import razerdp.blur.PopupBlurOption;
import razerdp.library.R;

/**
 * Created by 大灯泡 on 2017/12/12.
 * <p>
 * popupoption
 */
final class BasePopupHelper {
    //是否自动弹出输入框(default:false)
    private boolean autoShowInputMethod = false;

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
    private int internalOffsetX;
    private int internalOffsetY;
    private int preMeasureWidth;
    private int preMeasureHeight;

    private int popupViewWidth;
    private int popupViewHeight;
    //锚点view的location
    private int[] mAnchorViewLocation;
    private int mAnchorViewHeight;
    //是否自动适配popup的位置
    private boolean isAutoLocatePopup;
    //是否showAsDropDown
    private boolean isShowAsDropDown;
    //点击popup外部是否消失
    private boolean dismissWhenTouchOutside = true;
    //是否全屏
    private boolean fullScreen = false;
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

    BasePopupHelper() {
        mAnchorViewLocation = new int[2];
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
        return this;
    }

    int getPopupViewWidth() {
        return popupViewWidth;
    }

    BasePopupHelper setPopupViewWidth(int popupViewWidth) {
        this.popupViewWidth = popupViewWidth;
        return this;
    }

    int getPopupViewHeight() {
        return popupViewHeight;
    }

    BasePopupHelper setPopupViewHeight(int popupViewHeight) {
        this.popupViewHeight = popupViewHeight;
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
        popupWindow.setAnimationStyle(needPopupFadeAnima ? R.style.PopupAnimaFade : 0);
        return this;
    }

    boolean isShowAsDropDown() {
        return isShowAsDropDown;
    }

    BasePopupHelper setShowAsDropDown(boolean showAsDropDown) {
        this.isShowAsDropDown = showAsDropDown;
        return this;
    }

    int getPopupGravity() {
        return popupGravity;
    }

    BasePopupHelper setPopupGravity(int popupGravity) {
        this.popupGravity = popupGravity;
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

    boolean isAutoLocatePopup() {
        return isAutoLocatePopup;
    }

    BasePopupHelper setAutoLocatePopup(boolean autoLocatePopup) {
        isAutoLocatePopup = autoLocatePopup;
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

    BasePopupHelper getAnchorLocation(View v) {
        if (v == null) return this;
        v.getLocationOnScreen(mAnchorViewLocation);
        mAnchorViewHeight = v.getHeight();
        return this;
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

    int getInternalOffsetX() {
        return internalOffsetX + offsetX;
    }

    BasePopupHelper setInternalOffsetX(int internalOffsetX) {
        this.internalOffsetX = internalOffsetX;
        return this;
    }

    int getInternalOffsetY() {
        return internalOffsetY + offsetY;
    }

    BasePopupHelper setInternalOffsetY(int internalOffsetY) {
        this.internalOffsetY = internalOffsetY;
        return this;
    }

    public boolean isBackPressEnable() {
        return backPressEnable;
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
        return duration < 0 ? 300 : duration;
    }

    long getExitAnimationDuration() {
        long duration = 0;
        if (mDismissAnimation != null) {
            duration = mDismissAnimation.getDuration();
        } else if (mDismissAnimator != null) {
            duration = mDismissAnimator.getDuration();
        }
        return duration < 0 ? 300 : duration;
    }

    public Drawable getPopupBackground() {
        return mBackgroundDrawable;
    }

    public BasePopupHelper setPopupBackground(Drawable background) {
        mBackgroundDrawable = background;
        return this;
    }

    public boolean isAlignBackground() {
        return mAlignBackground;
    }

    public BasePopupHelper setAlignBackgound(boolean mAlignBackground) {
        this.mAlignBackground = mAlignBackground;
        return this;
    }

    public boolean isAllowToBlur() {
        return mBlurOption != null && mBlurOption.isAllowToBlur();
    }

}
