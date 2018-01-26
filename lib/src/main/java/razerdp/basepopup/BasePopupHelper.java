package razerdp.basepopup;

import android.animation.Animator;
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
    private Animation mExitAnimation;
    private Animator mExitAnimator;

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
    //是否自动适配popup的位置
    private boolean isAutoLocatePopup;
    //showasdropdown
    private boolean showAtDown;
    //点击popup外部是否消失
    private boolean dismissWhenTouchOutside;
    //是否全屏
    private boolean fullScreen = false;
    //是否需要淡入window动画
    private volatile boolean needPopupFadeAnima = true;

    private int mPopupLayoutId;

    private boolean backPressEnable = true;

    //popup params
    private boolean interCeptOutSideTouchEvent = true;

    //模糊option(为空的话则不模糊）
    private PopupBlurOption mBlurOption;

    BasePopupHelper() {
        mAnchorViewLocation = new int[2];
    }

    int getPopupLayoutId() {
        return mPopupLayoutId;
    }

    BasePopupHelper setPopupLayoutId(int popupLayoutId) {
        mPopupLayoutId = popupLayoutId;
        return this;
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

    Animation getExitAnimation() {
        return mExitAnimation;
    }

    BasePopupHelper setExitAnimation(Animation exitAnimation) {
        if (mExitAnimation == exitAnimation) return this;
        if (mExitAnimation != null) {
            mExitAnimation.cancel();
        }
        mExitAnimation = exitAnimation;
        return this;
    }

    Animator getExitAnimator() {
        return mExitAnimator;
    }

    BasePopupHelper setExitAnimator(Animator exitAnimator) {
        if (mExitAnimator == exitAnimator) return this;
        if (mExitAnimator != null) {
            mExitAnimator.cancel();
        }
        mExitAnimator = exitAnimator;
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

    boolean isNeedPopupFadeAnima() {
        return needPopupFadeAnima;
    }

    BasePopupHelper setNeedPopupFadeAnima(PopupWindow popupWindow, boolean needPopupFadeAnima) {
        if (popupWindow == null) return this;
        this.needPopupFadeAnima = needPopupFadeAnima;
        popupWindow.setAnimationStyle(needPopupFadeAnima ? R.style.PopupAnimaFade : 0);
        return this;
    }

    boolean isShowAtDown() {
        return showAtDown;
    }

    BasePopupHelper setShowAtDown(boolean showAtDown) {
        this.showAtDown = showAtDown;
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
        return interCeptOutSideTouchEvent;
    }

    BasePopupHelper setInterceptTouchEvent(PopupWindow popupWindow, boolean intecept) {
        if (popupWindow == null) return this;
        interCeptOutSideTouchEvent = intecept;
        return this;
    }

    BasePopupHelper getAnchorLocation(View v) {
        if (v == null) return this;
        v.getLocationOnScreen(mAnchorViewLocation);
        return this;
    }

    int getAnchorX() {
        return mAnchorViewLocation[0];
    }

    int getAnchorY() {
        return mAnchorViewLocation[1];
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
        if (mExitAnimation != null) {
            duration = mExitAnimation.getDuration();
        } else if (mExitAnimator != null) {
            duration = mExitAnimator.getDuration();
        }
        return duration < 0 ? 300 : duration;
    }

    public boolean isAllowToBlur() {
        return mBlurOption != null && mBlurOption.isAllowToBlur();
    }

}
