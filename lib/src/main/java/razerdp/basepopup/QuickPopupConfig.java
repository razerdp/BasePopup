package razerdp.basepopup;

import android.animation.Animator;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
public class QuickPopupConfig implements BasePopupFlag {
    protected int contentViewLayoutid;

    protected Animation mShowAnimation;
    protected Animation mDismissAnimation;

    protected Animator mShowAnimator;
    protected Animator mDismissAnimator;

    public int flag = IDLE;

    protected BasePopupWindow.OnDismissListener mDismissListener;

    protected WeakReference<BasePopupWindow.OnBlurOptionInitListener> mOnBlurOptionInitListener;
    protected PopupBlurOption mPopupBlurOption;
    protected int gravity = Gravity.CENTER;
    protected int alignBackgroundGravity = Gravity.TOP;

    protected int offsetX;
    protected int offsetY;

    protected int minWidth;
    protected int maxWidth;
    protected int minHeight;
    protected int maxHeight;

    protected Drawable background = new ColorDrawable(BasePopupWindow.DEFAULT_BACKGROUND_COLOR);

    protected View mLinkedView;


    HashMap<Integer, Pair<View.OnClickListener, Boolean>> mListenersHolderMap;


    public QuickPopupConfig() {
        //https://github.com/razerdp/BasePopup/issues/152
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            flag &= ~FADE_ENABLE;
        }
    }

    public static QuickPopupConfig generateDefault() {
        return new QuickPopupConfig()
                .withShowAnimation(SimpleAnimationUtils.getDefaultScaleAnimation(true))
                .withDismissAnimation(SimpleAnimationUtils.getDefaultScaleAnimation(false))
                .fadeInAndOut(Build.VERSION.SDK_INT != Build.VERSION_CODES.M);//https://github.com/razerdp/BasePopup/issues/152
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
        setFlag(FADE_ENABLE, fadeEnable);
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

    public QuickPopupConfig clipToScreen(boolean clipToScreen) {
        setFlag(CLIP_TO_SCREEN, clipToScreen);
        return this;
    }

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
        setFlag(FULL_SCREEN, fullscreen);
        return this;
    }

    public QuickPopupConfig keepSize(boolean keep) {
        setFlag(KEEP_SIZE, keep);
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
        if (mOnBlurOptionInitListener == null) return null;
        return mOnBlurOptionInitListener.get();
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
}
