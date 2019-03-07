package razerdp.basepopup;

import android.animation.Animator;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import razerdp.blur.PopupBlurOption;
import razerdp.util.SimpleAnimationUtils;

/**
 * Created by 大灯泡 on 2018/8/23.
 */
public class QuickPopupConfig {
    int contentViewLayoutid;

    Animation mShowAnimation;
    Animation mDismissAnimation;

    Animator mShowAnimator;
    Animator mDismissAnimator;

    boolean fadeEnable = true;

    BasePopupWindow.OnDismissListener mDismissListener;

    boolean blurBackground;
    WeakReference<BasePopupWindow.OnBlurOptionInitListener> mOnBlurOptionInitListener;
    PopupBlurOption mPopupBlurOption;
    int gravity = Gravity.CENTER;

    int offsetX;
    int offsetY;

    boolean alignBackground;
    Drawable background;
    boolean autoLocated;

    boolean clipChildren;
    boolean clipToScreen = true;
    boolean allowInterceptTouchEvent = true;
    boolean dismissOutSide = true;

    View mLinkedView;


    HashMap<Integer, Pair<View.OnClickListener, Boolean>> mListenersHolderMap;


    public static QuickPopupConfig generateDefault() {
        return new QuickPopupConfig()
                .withShowAnimation(SimpleAnimationUtils.getDefaultScaleAnimation(true))
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
        this.offsetX = offsetX;
        return this;
    }

    public QuickPopupConfig offsetY(int offsetY) {
        this.offsetY = offsetY;
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

    public QuickPopupConfig gravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public QuickPopupConfig clipChildren(boolean clipChildren) {
        this.clipChildren = clipChildren;
        return this;
    }

    public QuickPopupConfig clipToScreen(boolean clipToScreen) {
        this.clipToScreen = clipToScreen;
        return this;
    }

    public QuickPopupConfig allowInterceptTouchEvent(boolean allowInterceptTouchEvent) {
        this.allowInterceptTouchEvent = allowInterceptTouchEvent;
        return this;
    }

    public QuickPopupConfig linkTo(View linkedView) {
        mLinkedView = linkedView;
        return this;
    }

    QuickPopupConfig contentViewLayoutid(int contentViewLayoutid) {
        this.contentViewLayoutid = contentViewLayoutid;
        return this;
    }

    public QuickPopupConfig dismissOnOutSideTouch(boolean dismissOutSide) {
        this.dismissOutSide = dismissOutSide;
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

    public int getGravity() {
        return gravity;
    }

    public boolean isAllowInterceptTouchEvent() {
        return allowInterceptTouchEvent;
    }

    public boolean isClipChildren() {
        return clipChildren;
    }

    public int getContentViewLayoutid() {
        return contentViewLayoutid;
    }

    public boolean isClipToScreen() {
        return clipToScreen;
    }

    public View getLinkedView() {
        return mLinkedView;
    }

    public boolean isDismissOutSide() {
        return dismissOutSide;
    }
}
