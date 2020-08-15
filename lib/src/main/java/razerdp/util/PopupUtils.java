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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;

import androidx.annotation.StringRes;

import java.util.List;

import razerdp.basepopup.BasePopupSDK;

import static android.view.Gravity.BOTTOM;
import static android.view.Gravity.CENTER;
import static android.view.Gravity.CENTER_HORIZONTAL;
import static android.view.Gravity.CENTER_VERTICAL;
import static android.view.Gravity.DISPLAY_CLIP_HORIZONTAL;
import static android.view.Gravity.DISPLAY_CLIP_VERTICAL;
import static android.view.Gravity.END;
import static android.view.Gravity.FILL;
import static android.view.Gravity.FILL_HORIZONTAL;
import static android.view.Gravity.FILL_VERTICAL;
import static android.view.Gravity.LEFT;
import static android.view.Gravity.RIGHT;
import static android.view.Gravity.START;
import static android.view.Gravity.TOP;

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
        return getActivity(from, true);
    }

    public static Activity getActivity(Context from, boolean returnTopIfNull) {
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
                break;
            }
            result = ((ContextWrapper) result).getBaseContext();
            tryCount++;
        }
        return returnTopIfNull ? BasePopupSDK.getInstance().getTopActivity() : null;
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

    public static String gravityToString(int gravity) {
        final String split = " | ";
        final StringBuilder result = new StringBuilder();
        if ((gravity & FILL) == FILL) {
            result.append("FILL").append(split);
        } else {
            if ((gravity & FILL_VERTICAL) == FILL_VERTICAL) {
                result.append("FILL_VERTICAL").append(split);
            } else {
                if ((gravity & TOP) == TOP) {
                    result.append("TOP").append(split);
                }
                if ((gravity & BOTTOM) == BOTTOM) {
                    result.append("BOTTOM").append(split);
                }
            }
            if ((gravity & FILL_HORIZONTAL) == FILL_HORIZONTAL) {
                result.append("FILL_HORIZONTAL").append(split);
            } else {
                if ((gravity & START) == START) {
                    result.append("START").append(split);
                } else if ((gravity & LEFT) == LEFT) {
                    result.append("LEFT").append(split);
                }
                if ((gravity & END) == END) {
                    result.append("END").append(split);
                } else if ((gravity & RIGHT) == RIGHT) {
                    result.append("RIGHT").append(split);
                }
            }
        }
        if ((gravity & CENTER) == CENTER) {
            result.append("CENTER").append(split);
        } else {
            if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == CENTER_VERTICAL) {
                result.append("CENTER_VERTICAL").append(split);
            }
            if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == CENTER_HORIZONTAL) {
                result.append("CENTER_HORIZONTAL").append(split);
            }
        }
        if (result.length() == 0) {
            result.append("NO GRAVITY").append(split);
        }
        if ((gravity & DISPLAY_CLIP_VERTICAL) == DISPLAY_CLIP_VERTICAL) {
            result.append("DISPLAY_CLIP_VERTICAL").append(split);
        }
        if ((gravity & DISPLAY_CLIP_HORIZONTAL) == DISPLAY_CLIP_HORIZONTAL) {
            result.append("DISPLAY_CLIP_HORIZONTAL").append(split);
        }
        result.delete(result.length() - split.length(), result.length());
        return result.toString();
    }

    public static <T> T requireNonNull(T obj) {
        if (obj == null)
            throw new NullPointerException();
        return obj;
    }

    /**
     * 从资源文件拿到文字
     */
    public static String getString(@StringRes int strId, Object... objs) {
        if (strId == 0) return null;
        try {
            return BasePopupSDK.getApplication().getResources().getString(strId, objs);
        } catch (Exception e) {
            return "";
        }
    }
}
