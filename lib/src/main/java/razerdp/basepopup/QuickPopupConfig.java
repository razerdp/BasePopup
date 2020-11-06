package razerdp.basepopup;

import android.animation.Animator;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import java.util.HashMap;

import razerdp.blur.PopupBlurOption;
import razerdp.util.KeyboardUtils;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.ScaleConfig;

/**
 * Created by 大灯泡 on 2018/8/23.
 */
public class QuickPopupConfig implements BasePopupFlag, ClearMemoryObject {
    protected int contentViewLayoutid;

    protected Animation mShowAnimation;
    protected Animation mDismissAnimation;

    protected Animator mShowAnimator;
    protected Animator mDismissAnimator;

    public int flag = IDLE;

    protected BasePopupWindow.OnDismissListener mDismissListener;
    protected KeyboardUtils.OnKeyboardChangeListener mOnKeyboardChangeListener;
    protected BasePopupWindow.KeyEventListener mKeyEventListener;
    protected BasePopupWindow.OnBlurOptionInitListener mOnBlurOptionInitListener;
    protected PopupBlurOption mPopupBlurOption;
    protected int gravity = Gravity.CENTER;
    protected int alignBackgroundGravity = Gravity.TOP;

    protected int offsetX;
    protected int offsetY;
    protected int maskOffsetX;
    protected int maskOffsetY;
    protected int overlayStatusBarMode = BasePopupHelper.DEFAULT_OVERLAY_STATUS_BAR_MODE;
    protected int overlayNavigationBarMode = BasePopupHelper.DEFAULT_OVERLAY_NAVIGATION_BAR_MODE;

    protected int minWidth;
    protected int maxWidth;
    protected int minHeight;
    protected int maxHeight;

    protected Drawable background = new ColorDrawable(BasePopupWindow.DEFAULT_BACKGROUND_COLOR);

    protected View mLinkedView;

    HashMap<Integer, Pair<View.OnClickListener, Boolean>> mListenersHolderMap;

    volatile boolean destroyed;


    public QuickPopupConfig() {
        //https://github.com/razerdp/BasePopup/issues/152
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            flag &= ~FADE_ENABLE;
        }
    }

    public static QuickPopupConfig generateDefault() {
        //https://github.com/razerdp/BasePopup/issues/152
        return new QuickPopupConfig()
                .withShowAnimation(AnimationHelper.asAnimation()
                        .withScale(ScaleConfig.CENTER)
                        .toShow())
                .withDismissAnimation(AnimationHelper.asAnimation()
                        .withScale(ScaleConfig.CENTER)
                        .toDismiss())
                .fadeInAndOut(Build.VERSION.SDK_INT != Build.VERSION_CODES.M);
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
        setFlag(BLUR_BACKGROUND, blurBackground);
        this.mOnBlurOptionInitListener = mInitListener;
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
        setFlag(FADE_ENABLE, fadeEnable);
        return this;
    }

    public QuickPopupConfig offsetX(int offsetX) {
        this.offsetX = offsetX;
        return this;
    }

    public QuickPopupConfig maskOffsetX(int offsetX) {
        this.maskOffsetX = offsetX;
        return this;
    }


    public QuickPopupConfig offsetY(int offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    public QuickPopupConfig maskOffsetY(int offsetY) {
        this.maskOffsetY = offsetY;
        return this;
    }

    public QuickPopupConfig overlayStatusbarMode(int mode) {
        this.overlayStatusBarMode = mode;
        return this;
    }

    public QuickPopupConfig overlayNavigationBarMode(int mode) {
        this.overlayNavigationBarMode = mode;
        return this;
    }

    public QuickPopupConfig overlayStatusbar(boolean overlay) {
        if (!overlay) {
            this.flag &= ~BasePopupFlag.OVERLAY_STATUS_BAR;
        } else {
            this.flag |= BasePopupFlag.OVERLAY_STATUS_BAR;
        }
        return this;
    }

    public QuickPopupConfig overlayNavigationBar(boolean overlay) {
        if (!overlay) {
            this.flag &= ~BasePopupFlag.OVERLAY_NAVIGATION_BAR;
        } else {
            this.flag |= BasePopupFlag.OVERLAY_NAVIGATION_BAR;
        }
        return this;
    }

    public QuickPopupConfig alignBackground(boolean alignBackground) {
        setFlag(ALIGN_BACKGROUND, alignBackground);
        return this;
    }

    public QuickPopupConfig alignBackgroundGravity(int gravity) {
        this.alignBackgroundGravity = gravity;
        return this;
    }

    public QuickPopupConfig autoLocated(boolean autoLocated) {
        setFlag(AUTO_LOCATED, autoLocated);
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
        setFlag(CLIP_CHILDREN, clipChildren);
        return this;
    }

    /**
     * @deprecated 请使用 {@link #outSideTouchable(boolean)}
     */
    @Deprecated
    public QuickPopupConfig allowInterceptTouchEvent(boolean allowInterceptTouchEvent) {
        setFlag(OUT_SIDE_TOUCHABLE, !allowInterceptTouchEvent);
        return this;
    }

    public QuickPopupConfig outSideTouchable(boolean outSideTouchable) {
        setFlag(OUT_SIDE_TOUCHABLE, outSideTouchable);
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

    /**
     * @deprecated 请使用 {@link #outSideDismiss(boolean)}
     */
    @Deprecated
    public QuickPopupConfig dismissOnOutSideTouch(boolean dismissOutSide) {
        setFlag(OUT_SIDE_DISMISS, dismissOutSide);
        return this;
    }

    public QuickPopupConfig minWidth(int minWidth) {
        this.minWidth = minWidth;
        return this;
    }

    public QuickPopupConfig maxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    public QuickPopupConfig minHeight(int minHeight) {
        this.minHeight = minHeight;
        return this;
    }

    public QuickPopupConfig maxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    public QuickPopupConfig backpressEnable(boolean enable) {
        setFlag(BACKPRESS_ENABLE, enable);
        return this;
    }

    public QuickPopupConfig fullScreen(boolean fullscreen) {
        setFlag(OVERLAY_STATUS_BAR, fullscreen);
        return this;
    }

    public QuickPopupConfig fitSize(boolean keep) {
        setFlag(FITSIZE, keep);
        return this;
    }


    public QuickPopupConfig outSideDismiss(boolean outsideDismiss) {
        setFlag(OUT_SIDE_DISMISS, outsideDismiss);
        return this;
    }

    public QuickPopupConfig keyEventListener(BasePopupWindow.KeyEventListener keyEventListener) {
        this.mKeyEventListener = keyEventListener;
        return this;
    }

    public QuickPopupConfig keyBoardChangeListener(KeyboardUtils.OnKeyboardChangeListener listener) {
        this.mOnKeyboardChangeListener = listener;
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

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }


    public HashMap<Integer, Pair<View.OnClickListener, Boolean>> getListenersHolderMap() {
        return mListenersHolderMap;
    }

    public BasePopupWindow.OnBlurOptionInitListener getOnBlurOptionInitListener() {
        return mOnBlurOptionInitListener;
    }

    public int getAlignBackgroundGravity() {
        return alignBackgroundGravity;
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

    public int getContentViewLayoutid() {
        return contentViewLayoutid;
    }

    public View getLinkedView() {
        return mLinkedView;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    private void setFlag(int flag, boolean added) {
        if (!added) {
            this.flag &= ~flag;
        } else {
            this.flag |= flag;
        }
    }

    public int getMaskOffsetX() {
        return maskOffsetX;
    }

    public int getMaskOffsetY() {
        return maskOffsetY;
    }

    public int getOverlayStatusBarMode() {
        return overlayStatusBarMode;
    }

    public int getOverlayNavigationBarMode() {
        return overlayNavigationBarMode;
    }

    public KeyboardUtils.OnKeyboardChangeListener getOnKeyboardChangeListener() {
        return mOnKeyboardChangeListener;
    }

    public BasePopupWindow.KeyEventListener getKeyEventListener() {
        return mKeyEventListener;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void clear(boolean destroy) {
        this.destroyed = true;
        if (mPopupBlurOption != null) {
            mPopupBlurOption.clear();
        }
        mShowAnimation = null;
        mDismissAnimation = null;
        mShowAnimator = null;
        mDismissAnimator = null;
        mDismissListener = null;
        mOnBlurOptionInitListener = null;
        background = null;
        mLinkedView = null;
        if (mListenersHolderMap != null) {
            mListenersHolderMap.clear();
        }
        mKeyEventListener = null;
        mOnKeyboardChangeListener = null;
        mListenersHolderMap = null;
    }
}
