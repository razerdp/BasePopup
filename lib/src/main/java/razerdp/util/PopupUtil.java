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

/**
 * Created by 大灯泡 on 2018/8/15.
 */
public class PopupUtil {

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
                return null;
            }
            result = ((ContextWrapper) result).getBaseContext();
            tryCount++;
        }
        return null;
    }
}
