package razerdp.basepopup;

import android.animation.Animator;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.PopupWindow;

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
    //外部是否可用点击
    private boolean mOutsideTouchable;
    //是否需要淡入window动画
    private volatile boolean needPopupFadeAnima = true;

    private int mPopupLayoutId;


    //popup params
    private boolean focusable;
    private boolean outsideTouchable;
    private boolean hasBackground;

    public BasePopupHelper() {
        mAnchorViewLocation = new int[2];
    }

    public int getPopupLayoutId() {
        return mPopupLayoutId;
    }

    public BasePopupHelper setPopupLayoutId(int popupLayoutId) {
        mPopupLayoutId = popupLayoutId;
        return this;
    }

    public Animation getShowAnimation() {
        return mShowAnimation;
    }

    public BasePopupHelper setShowAnimation(Animation showAnimation) {
        if (mShowAnimation == showAnimation) return this;
        if (mShowAnimation != null) {
            mShowAnimation.cancel();
        }
        mShowAnimation = showAnimation;
        return this;
    }

    public Animator getShowAnimator() {
        return mShowAnimator;
    }

    public BasePopupHelper setShowAnimator(Animator showAnimator) {
        if (mShowAnimator == showAnimator) return this;
        if (mShowAnimator != null) {
            mShowAnimator.cancel();
        }
        mShowAnimator = showAnimator;
        return this;
    }

    public Animation getExitAnimation() {
        return mExitAnimation;
    }

    public BasePopupHelper setExitAnimation(Animation exitAnimation) {
        if (mExitAnimation == exitAnimation) return this;
        if (mExitAnimation != null) {
            mExitAnimation.cancel();
        }
        mExitAnimation = exitAnimation;
        return this;
    }

    public Animator getExitAnimator() {
        return mExitAnimator;
    }

    public BasePopupHelper setExitAnimator(Animator exitAnimator) {
        if (mExitAnimator == exitAnimator) return this;
        if (mExitAnimator != null) {
            mExitAnimator.cancel();
        }
        mExitAnimator = exitAnimator;
        return this;
    }

    public int getPopupViewWidth() {
        return popupViewWidth;
    }

    public BasePopupHelper setPopupViewWidth(int popupViewWidth) {
        this.popupViewWidth = popupViewWidth;
        return this;
    }

    public int getPopupViewHeight() {
        return popupViewHeight;
    }

    public BasePopupHelper setPopupViewHeight(int popupViewHeight) {
        this.popupViewHeight = popupViewHeight;
        return this;
    }

    public boolean isNeedPopupFadeAnima() {
        return needPopupFadeAnima;
    }

    public BasePopupHelper setNeedPopupFadeAnima(boolean needPopupFadeAnima) {
        this.needPopupFadeAnima = needPopupFadeAnima;
        return this;
    }

    public boolean isShowAtDown() {
        return showAtDown;
    }

    public BasePopupHelper setShowAtDown(boolean showAtDown) {
        this.showAtDown = showAtDown;
        return this;
    }

    public int getPopupGravity() {
        return popupGravity;
    }

    public BasePopupHelper setPopupGravity(int popupGravity) {
        this.popupGravity = popupGravity;
        return this;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public BasePopupHelper setOffsetX(int offsetX) {
        this.offsetX = offsetX;
        return this;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public BasePopupHelper setOffsetY(int offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    public boolean isAutoShowInputMethod() {
        return autoShowInputMethod;
    }

    public void setAutoShowInputMethod(boolean autoShowInputMethod) {
        this.autoShowInputMethod = autoShowInputMethod;
    }

    public boolean isAutoLocatePopup() {
        return isAutoLocatePopup;
    }

    public BasePopupHelper setAutoLocatePopup(boolean autoLocatePopup) {
        isAutoLocatePopup = autoLocatePopup;
        return this;
    }

    public BasePopupWindow.OnDismissListener getOnDismissListener() {
        return mOnDismissListener;
    }

    public BasePopupHelper setOnDismissListener(BasePopupWindow.OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
        return this;
    }

    public BasePopupWindow.OnBeforeShowCallback getOnBeforeShowCallback() {
        return mOnBeforeShowCallback;
    }

    public BasePopupHelper setOnBeforeShowCallback(BasePopupWindow.OnBeforeShowCallback onBeforeShowCallback) {
        mOnBeforeShowCallback = onBeforeShowCallback;
        return this;
    }

    public boolean isDismissWhenTouchOutside() {
        return dismissWhenTouchOutside;
    }

    public BasePopupHelper setDismissWhenTouchOutside(boolean dismissWhenTouchOutside) {
        this.dismissWhenTouchOutside = dismissWhenTouchOutside;
        if (dismissWhenTouchOutside) {
            //指定透明背景，back键相关
            focusable = true;
            outsideTouchable = true;
            hasBackground = true;
        } else {
            focusable = false;
            outsideTouchable = false;
            hasBackground = false;
        }
        if (outsideTouchable) {
            focusable = false;
        }
        return this;
    }

    public boolean isOutsideTouchable() {
        return mOutsideTouchable;
    }

    public BasePopupHelper setOutsideTouchable(boolean outsideClickable) {
        mOutsideTouchable = outsideClickable;
        focusable = false;
        return this;
    }

    public BasePopupHelper getAnchorLocation(View v) {
        if (v == null) return this;
        v.getLocationOnScreen(mAnchorViewLocation);
        return this;
    }

    public int getAnchorX() {
        return mAnchorViewLocation[0];
    }

    public int getAnchorY() {
        return mAnchorViewLocation[1];
    }


    void applyToPopupWindow(PopupWindow popupWindow) {
        if (popupWindow != null) {
            popupWindow.setAnimationStyle(needPopupFadeAnima ? R.style.PopupAnimaFade : 0);
            popupWindow.setSoftInputMode(autoShowInputMethod ? WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE : WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            popupWindow.setFocusable(focusable);
            popupWindow.setOutsideTouchable(outsideTouchable);
            popupWindow.setBackgroundDrawable(hasBackground ? new ColorDrawable() : null);
        }

    }


}
