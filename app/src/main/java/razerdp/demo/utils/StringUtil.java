package razerdp.demo.utils;

import android.support.annotation.StringRes;
import android.text.TextUtils;

import razerdp.demo.app.AppContext;


/**
 * Created by xxqiang on 16/8/26.
 */

public class StringUtil {

    public static boolean noEmpty(CharSequence source) {
        return !TextUtils.isEmpty(source);
    }

    public static boolean noEmpty(CharSequence... source) {
        for (CharSequence charSequence : source) {
            if (TextUtils.isEmpty(charSequence)) return false;
        }
        return true;
    }


    public static boolean isEmptyWithNull(String source) {
        return source == null || TextUtils.isEmpty(source.trim()) || TextUtils.equals(source, "null") || TextUtils.equals(source, "Null") || TextUtils.equals(source, "NULL");
    }

    public static String trim(String source) {
        if (noEmpty(source)) {
            return source.trim();
        }
        return source;

    }

    public static boolean contains(String source, String key) {
        if (noEmpty(source)) {
            return source.contains(key);
        }
        return false;
    }

    public static CharSequence trim(CharSequence cs) {
        int len = cs.length();
        int st = 0;

        while ((st < len) && (cs.charAt(st) <= ' ')) {
            st++;
        }
        while ((st < len) && (cs.charAt(len - 1) <= ' ')) {
            len--;
        }
        return ((st > 0) || (len < cs.length())) ? cs.subSequence(st, len) : cs;
    }


    /**
     * 从资源文件拿到文字
     */
    public static String getString(@StringRes int strId, Object... objs) {
        if (strId == 0) return null;
        return AppContext.getAppContext().getResources().getString(strId, objs);
    }

}
