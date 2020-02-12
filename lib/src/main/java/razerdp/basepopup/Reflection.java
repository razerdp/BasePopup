package razerdp.basepopup;

import android.content.Context;
import android.os.Build;

import java.lang.reflect.Method;

import razerdp.util.log.PopupLog;

import static android.os.Build.VERSION.SDK_INT;

/**
 * Created by 大灯泡 on 2018/10/12.
 * <p>
 * 针对android p and over 的反射绕过
 * <p>
 * 思路参考：http://weishu.me/2019/03/16/another-free-reflection-above-android-p/
 * <p>
 * 感谢维数大佬的分享~
 * <p>
 * 本控件基于https://github.com/tiann/FreeReflection/blob/master/library/src/main/java/me/weishu/reflection/Reflection.java修改
 */
final class Reflection {
    private static final String TAG = "Reflection";

    private static Object sVmRuntime;
    private static Method setHiddenApiExemptions;

    static {
        if (SDK_INT >= Build.VERSION_CODES.P) {
            try {
                Method forName = Class.class.getDeclaredMethod("forName", String.class);
                Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);

                Class<?> vmRuntimeClass = (Class<?>) forName.invoke(null, "dalvik.system.VMRuntime");
                Method getRuntime = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null);
                setHiddenApiExemptions = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", new Class[]{String[].class});
                sVmRuntime = getRuntime.invoke(null);
            } catch (Throwable e) {
                PopupLog.e(TAG, "绕过代理失败，BasePopup特性将无发生效", e);
            }
        }
    }

    static int unseal(Context context) {
        if (SDK_INT < 28) {
            // Below Android P, ignore
            return 0;
        }

        // try exempt API first.
        if (exempt("Landroid/widget/PopupWindow")) {
            PopupLog.e(TAG, "解锁黑/灰名单成功");
            return 0;
        } else {
            PopupLog.e(TAG, "解锁黑/灰名单失败");
            return -1;
        }
    }

    /**
     * make the method exempted from hidden API check.
     *
     * @param method the method signature prefix.
     * @return true if success.
     */
    public static boolean exempt(String method) {
        return exempt(new String[]{method});
    }

    /**
     * make specific methods exempted from hidden API check.
     *
     * @param methods the method signature prefix, such as "Ldalvik/system", "Landroid" or even "L"
     * @return true if success
     */
    public static boolean exempt(String... methods) {
        if (sVmRuntime == null || setHiddenApiExemptions == null) {
            return false;
        }

        try {
            setHiddenApiExemptions.invoke(sVmRuntime, new Object[]{methods});
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * Make all hidden API exempted.
     *
     * @return true if success.
     */
    public static boolean exemptAll() {
        return exempt(new String[]{"L"});
    }
}
