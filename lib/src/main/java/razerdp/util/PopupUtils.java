package razerdp.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;

import java.util.List;

import razerdp.basepopup.BasePopupSDK;

/**
 * Created by 大灯泡 on 2018/8/15.
 */
public class PopupUtils {

    /**
     * 是否正常的drawable
     *
     * @param drawable
     * @return
     */
    public static boolean isBackgroundInvalidated(Drawable drawable) {
        return drawable == null
                || (drawable instanceof ColorDrawable) && ((ColorDrawable) drawable).getColor() == Color.TRANSPARENT;
    }


    public static View clearViewFromParent(View v) {
        if (v == null) return v;
        ViewParent p = v.getParent();
        if (p instanceof ViewGroup) {
            ((ViewGroup) p).removeView(v);
        }
        return v;

    }

    public static boolean isListEmpty(List<?> datas) {
        return datas == null || datas.size() <= 0;
    }


    public static Activity getActivity(Context from) {
        final int limit = 20;
        Context result = from;
        if (result instanceof Activity) {
            return (Activity) result;
        }
        int tryCount = 0;
        while (result instanceof ContextWrapper) {
            if (result instanceof Activity) {
                return (Activity) result;
            }
            if (tryCount > limit) {
                //break endless loop
                return BasePopupSDK.getInstance().getTopActivity();
            }
            result = ((ContextWrapper) result).getBaseContext();
            tryCount++;
        }
        return BasePopupSDK.getInstance().getTopActivity();
    }

    public static float range(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }

    public static double range(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }

    public static int range(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    public static long range(long value, long min, long max) {
        return Math.max(min, Math.min(value, max));
    }

    public static long getAnimationDuration(Animation animation, long defaultDuration) {
        if (animation == null) return defaultDuration;
        long result = animation.getDuration();
        return result < 0 ? defaultDuration : result;
    }

    public static long getAnimatorDuration(Animator animator, long defaultDuration) {
        if (animator == null) return defaultDuration;
        long duration = 0;
        if (animator instanceof AnimatorSet) {
            AnimatorSet set = ((AnimatorSet) animator);
            duration = set.getDuration();
            if (duration < 0) {
                for (Animator childAnimation : set.getChildAnimations()) {
                    duration = Math.max(duration, childAnimation.getDuration());
                }
            }
        } else {
            duration = animator.getDuration();
        }
        return duration < 0 ? defaultDuration : duration;
    }

    public static boolean isActivityAlive(Activity act) {
        if (act == null) return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return !act.isFinishing() && !act.isDestroyed();
        } else {
            return !act.isFinishing();
        }
    }

    public static <I, O> O cast(I input, Class<O> outClass, O... defaultValue) {
        if (input != null && outClass.isAssignableFrom(input.getClass())) {
            try {
                return outClass.cast(input);
            } catch (ClassCastException e) {
            }
        }
        if (defaultValue != null && defaultValue.length > 0) {
            return defaultValue[0];
        }
        return null;

    }
}
