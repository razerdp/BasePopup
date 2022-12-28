package razerdp.basepopup;

import android.animation.Animator;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.Keep;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import razerdp.blur.PopupBlurOption;
import razerdp.util.KeyboardUtils;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.ScaleConfig;
import razerdp.util.log.PopupLog;
import razerdp.widget.QuickPopup;

/**
 * Created by 大灯泡 on 2018/8/23.
 */
@Keep
public class QuickPopupConfig implements BasePopupFlag, ClearMemoryObject {
    static final Map<String, Method> INVOKE_MAP = new HashMap<>();

    static Class<?> getClass(Object obj) {
        if (obj instanceof Integer) {
            return int.class;
        }
        if (obj instanceof Boolean) {
            return boolean.class;
        }
        if (obj instanceof Double) {
            return double.class;
        }
        if (obj instanceof Float) {
            return Float.class;
        }
        if (obj instanceof Long) {
            return long.class;
        }
        if (obj instanceof Animation) {
            return Animation.class;
        }
        if (obj instanceof Animator) {
            return Animator.class;
        }
        if (obj instanceof Drawable) {
            return Drawable.class;
        }
        return obj.getClass();
    }

    static boolean AppendInvokeMap(String name, Class<?> paramClass) {
        if (INVOKE_MAP.containsKey(name)) {
            return true;
        }
        Method m = FindMethod(name, paramClass);
        if (m != null) {
            INVOKE_MAP.put(name, m);
            return true;
        }
        return false;
    }

    static Method FindMethod(String methodName, Class<?> parameterTypes) {
        try {
            return QuickPopup.class.getMethod(methodName, parameterTypes);
        } catch (Exception e) {
            PopupLog.e("not found", methodName, parameterTypes.getName());
            return null;
        }
    }

    void set(String name, Object obj) {
        if (AppendInvokeMap(name, getClass(obj))) {
            invokeParams.put(name, obj);
        }
    }

    protected Map<String, Object> invokeParams;

    protected int contentViewLayoutid;

    public int flag = IDLE;

    protected BasePopupWindow.OnBlurOptionInitListener mOnBlurOptionInitListener;
    protected PopupBlurOption mPopupBlurOption;
    HashMap<Integer, Pair<View.OnClickListener, Boolean>> mListenersHolderMap;
    volatile boolean destroyed;

    public QuickPopupConfig() {
        //https://github.com/razerdp/BasePopup/issues/152
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            flag &= ~FADE_ENABLE;
        }
        invokeParams = new HashMap<>();
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
        set("setShowAnimation", showAnimation);
        return this;
    }

    public QuickPopupConfig withDismissAnimation(Animation dismissAnimation) {
        set("setDismissAnimation", dismissAnimation);
        return this;
    }

    public QuickPopupConfig withShowAnimator(Animator showAnimator) {
        set("setShowAnimator", showAnimator);
        return this;
    }

    public QuickPopupConfig withDismissAnimator(Animator dismissAnimator) {
        set("setDismissAnimator", dismissAnimator);
        return this;
    }

    public QuickPopupConfig dismissListener(BasePopupWindow.OnDismissListener dismissListener) {
        set("setOnDismissListener", dismissListener);
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
        set("setOffsetX", offsetX);
        return this;
    }

    public QuickPopupConfig maskOffsetX(int offsetX) {
        set("setMaskOffsetX", offsetX);
        return this;
    }


    public QuickPopupConfig offsetY(int offsetY) {
        set("setOffsetY", offsetY);
        return this;
    }

    public QuickPopupConfig maskOffsetY(int offsetY) {
        set("setMaskOffsetY", offsetY);
        return this;
    }

    public QuickPopupConfig overlayStatusbarMode(int mode) {
        set("setOverlayStatusbarMode", mode);
        return this;
    }

    public QuickPopupConfig overlayNavigationBarMode(int mode) {
        set("setOverlayNavigationBarMode", mode);
        return this;
    }

    public QuickPopupConfig overlayStatusbar(boolean overlay) {
        set("setOverlayStatusbar", overlay);
        return this;
    }

    public QuickPopupConfig overlayNavigationBar(boolean overlay) {
        set("setOverlayNavigationBar", overlay);
        return this;
    }

    public QuickPopupConfig alignBackground(boolean alignBackground) {
        set("setAlignBackground", alignBackground);
        return this;
    }

    public QuickPopupConfig alignBackgroundGravity(int gravity) {
        set("setAlignBackgroundGravity", gravity);
        return this;
    }

    public QuickPopupConfig autoMirrorEnable(boolean autoMirrorEnable) {
        set("setAutoMirrorEnable", autoMirrorEnable);
        return this;
    }

    public QuickPopupConfig background(Drawable background) {
        set("setBackground", background);
        return this;
    }

    public QuickPopupConfig backgroundColor(int color) {
        return background(new ColorDrawable(color));
    }

    public QuickPopupConfig gravity(int gravity) {
        set("setPopupGravity", gravity);
        return this;
    }

    public QuickPopupConfig clipChildren(boolean clipChildren) {
        set("setClipChildren", clipChildren);
        return this;
    }


    public QuickPopupConfig outSideTouchable(boolean outSideTouchable) {
        set("setOutSideTouchable", outSideTouchable);
        return this;
    }

    public QuickPopupConfig linkTo(View linkedView) {
        set("linkTo", linkedView);
        return this;
    }

    QuickPopupConfig contentViewLayoutid(int contentViewLayoutid) {
        this.contentViewLayoutid = contentViewLayoutid;
        return this;
    }


    public QuickPopupConfig minWidth(int minWidth) {
        set("setMinWidth", minWidth);
        return this;
    }

    public QuickPopupConfig maxWidth(int maxWidth) {
        set("setMaxWidth", maxWidth);
        return this;
    }

    public QuickPopupConfig minHeight(int minHeight) {
        set("setMinHeight", minHeight);
        return this;
    }

    public QuickPopupConfig maxHeight(int maxHeight) {
        set("setMaxHeight", maxHeight);
        return this;
    }

    public QuickPopupConfig backpressEnable(boolean enable) {
        set("setBackPressEnable", enable);
        return this;
    }

    public QuickPopupConfig outSideDismiss(boolean outsideDismiss) {
        set("setOutSideDismiss", outsideDismiss);
        return this;
    }

    public QuickPopupConfig keyEventListener(BasePopupWindow.KeyEventListener keyEventListener) {
        set("setKeyEventListener", keyEventListener);
        return this;
    }

    public QuickPopupConfig keyBoardChangeListener(KeyboardUtils.OnKeyboardChangeListener listener) {
        set("setOnKeyboardChangeListener", listener);
        return this;
    }
    //-----------------------------------------getter-----------------------------------------


    public Map<String, Object> getInvokeParams() {
        return invokeParams;
    }

    public Method getMethod(String name) {
        if (INVOKE_MAP.containsKey(name)) {
            return INVOKE_MAP.get(name);
        }
        return null;
    }

    public PopupBlurOption getPopupBlurOption() {
        return mPopupBlurOption;
    }

    public HashMap<Integer, Pair<View.OnClickListener, Boolean>> getListenersHolderMap() {
        return mListenersHolderMap;
    }

    public BasePopupWindow.OnBlurOptionInitListener getOnBlurOptionInitListener() {
        return mOnBlurOptionInitListener;
    }

    public int getContentViewLayoutid() {
        return contentViewLayoutid;
    }

    private void setFlag(int flag, boolean added) {
        if (!added) {
            this.flag &= ~flag;
        } else {
            this.flag |= flag;
        }
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
        mOnBlurOptionInitListener = null;
        if (mListenersHolderMap != null) {
            mListenersHolderMap.clear();
        }
        mListenersHolderMap = null;
        invokeParams.clear();
        invokeParams = null;
    }
}
