package razerdp.basepopup;

import android.animation.Animator;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;

import java.util.HashMap;

import razerdp.blur.PopupBlurOption;

/**
 * Created by 大灯泡 on 2018/8/23.
 */
public final class QuickPopupConfig {
    Animation mShowAnimation;
    Animation mDismissAnimation;

    Animator mShowAnimator;
    Animator mDismissAnimator;

    boolean fadeEnable;

    BasePopupWindow.OnDismissListener mDismissListener;

    boolean blurBackground;
    PopupBlurOption mPopupBlurOption;

    HashMap<Integer, Pair<View.OnClickListener, Boolean>> mListenersHolderMap;



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
        this.blurBackground = blurBackground;
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

    public BasePopupWindow.OnDismissListener getDismissListener() {
        return mDismissListener;
    }
}
