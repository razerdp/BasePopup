package razerdp.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.List;

import razerdp.basepopup.BasePopupSupporterManager;

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


    public static Activity scanForActivity(Context from, final int limit) {
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
                return BasePopupSupporterManager.getInstance().getTopActivity();
            }
            result = ((ContextWrapper) result).getBaseContext();
            tryCount++;
        }
        return BasePopupSupporterManager.getInstance().getTopActivity();
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
}
