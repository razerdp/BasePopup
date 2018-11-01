package razerdp.util;

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
    public static boolean isListEmpty(Object...obj) {
        return obj == null || obj.length <= 0;
    }
}
