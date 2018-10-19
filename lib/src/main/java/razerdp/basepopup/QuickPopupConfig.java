package razerdp.basepopup;

import android.animation.Animator;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import razerdp.blur.PopupBlurOption;
import razerdp.util.SimpleAnimationUtils;

/**
 * Created by 大灯泡 on 2018/8/23.
 */
public final class QuickPopupConfig {
    Animation mShowAnimation;
    Animation mDismissAnimation;

    Animator mShowAnimator;
    Animator mDismissAnimator;

    boolean fadeEnable = true;

    BasePopupWindow.OnDismissListener mDismissListener;

    boolean blurBackground;
    WeakReference<BasePopupWindow.OnBlurOptionInitListener> mOnBlurOptionInitListener;
    PopupBlurOption mPopupBlurOption;

    int offsetX;
    int offsetY;

    float offsetRatioOfPopupWidth;
    float offsetRatioOfPopupHeight;

    boolean alignBackground;

    boolean autoLocated;

    Drawable background;

    HashMap<Integer, Pair<View.OnClickListener, Boolean>> mListenersHolderMap;


    public static QuickPopupConfig generateDefault() {
        return new QuickPopupConfig()
                .withShowAnimation(SimpleAnimationUtils.getDefaultAlphaAnimation(true))
                .withDismissAnimation(SimpleAnimationUtils.getDefaultScaleAnimation(false))
                .fadeInAndOut(true);
    }

    public QuickPopupConfig withShowAnimation(Animation showAnimation) {
        mShowAnimation = showAnimation;
        return this;
    }

    public QuickPopupConfig withDismissAnimation(Animation dismissAnimation) {
        mDismissAnimation = dismissAnimation;
        return this;
    }

    public QuickPopupConfig withShowAnimator(Animator showAnimator) {
        mShowAnimator = showAnimator;
        return this;
    }

    public QuickPopupConfig withDismissAnimator(Animator dismissAnimator) {
        mDismissAnimator = dismissAnimator;
        return this;
    }

    public QuickPopupConfig dismissListener(BasePopupWindow.OnDismissListener dismissListener) {
        mDismissListener = dismissListener;
        return this;
    }

    public QuickPopupConfig blurBackground(boolean blurBackground) {
        return blurBackground(blurBackground, null);
    }

    public QuickPopupConfig blurBackground(boolean blurBackground, BasePopupWindow.OnBlurOptionInitListener mInitListener) {
        this.blurBackground = blurBackground;
        this.mOnBlurOptionInitListener = new WeakReference<>(mInitListener);
        return this;
    }

    public QuickPopupConfig withBlurOption(PopupBlurOption popupBlurOption) {
        mPopupBlurOption = popupBlurOption;
        return this;
    }

    public QuickPopupConfig withClick(int viewId, View.OnClickListener listener) {
        return withClick(viewId, listener, false);
    }

    public QuickPopupConfig withClick(int viewId, View.OnClickListener listener, boolean dismissWhenClick) {
        if (mListenersHolderMap == null) {
            mListenersHolderMap = new HashMap<>();
        }
        mListenersHolderMap.put(viewId, Pair.create(listener, dismissWhenClick));
        return this;
    }

    public QuickPopupConfig fadeInAndOut(boolean fadeEnable) {
        this.fadeEnable = fadeEnable;
        return this;
    }

    public QuickPopupConfig offsetX(int offsetX) {
        return offsetX(offsetX, 0);
    }

    public QuickPopupConfig offsetY(int offsetY) {
        return offsetY(offsetY, 0);
    }

    public QuickPopupConfig offsetX(int offsetX, float ratioOfPopupWidth) {
        this.offsetX = offsetX;
        this.offsetRatioOfPopupWidth = ratioOfPopupWidth;
        return this;
    }

    public QuickPopupConfig offsetY(int offsetY, float ratioOfPopupHeight) {
        this.offsetY = offsetY;
        this.offsetRatioOfPopupHeight = ratioOfPopupHeight;
        return this;
    }

    public QuickPopupConfig alignBackground(boolean alignBackground) {
        this.alignBackground = alignBackground;
        return this;
    }

    public QuickPopupConfig autoLocated(boolean autoLocated) {
        this.autoLocated = autoLocated;
        return this;
    }

    public QuickPopupConfig background(Drawable background) {
        this.background = background;
        return this;
    }

    public QuickPopupConfig backgroundColor(int color) {
        return background(new ColorDrawable(color));
    }
    //-----------------------------------------getter-----------------------------------------

    public Animation getShowAnimation() {
        return mShowAnimation;
    }

    public Animation getDismissAnimation() {
        return mDismissAnimation;
    }

    public Animator getShowAnimator() {
        return mShowAnimator;
    }

    public Animator getDismissAnimator() {
        return mDismissAnimator;
    }

    public PopupBlurOption getPopupBlurOption() {
        return mPopupBlurOption;
    }

    public boolean isBlurBackground() {
        return blurBackground;
    }

    public boolean isFadeEnable() {
        return fadeEnable;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public float getOffsetRatioOfPopupWidth() {
        return offsetRatioOfPopupWidth;
    }

    public float getOffsetRatioOfPopupHeight() {
        return offsetRatioOfPopupHeight;
    }

    public boolean isAlignBackground() {
        return alignBackground;
    }

    public HashMap<Integer, Pair<View.OnClickListener, Boolean>> getListenersHolderMap() {
        return mListenersHolderMap;
    }

    public BasePopupWindow.OnBlurOptionInitListener getOnBlurOptionInitListener() {
        if (mOnBlurOptionInitListener == null) return null;
        return mOnBlurOptionInitListener.get();
    }

    public boolean isAutoLocated() {
        return autoLocated;
    }

    public BasePopupWindow.OnDismissListener getDismissListener() {
        return mDismissListener;
    }

    public Drawable getBackground() {
        return background;
    }
}
