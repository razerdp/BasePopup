package razerdp.demo.utils;

import android.content.Context;
import android.content.res.Resources;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by 大灯泡 on 2017/4/18.
 * <p>
 * 常用工具类
 */

public class ToolUtil {
    public static boolean isListEmpty(List<?> datas) {
        return datas == null || datas.size() <= 0;
    }

    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }
}
