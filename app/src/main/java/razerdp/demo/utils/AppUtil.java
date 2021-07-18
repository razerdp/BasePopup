package razerdp.demo.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import java.util.List;

import razerdp.demo.app.AppContext;

/**
 * Created by 大灯泡 on 2019/4/18
 * <p>
 * Description：
 */
public class AppUtil {
    /**
     * 检查包是否存在
     *
     * @param packname
     * @return
     */
    public static boolean isAppInstalled(String packname) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = AppContext.getAppContext()
                    .getPackageManager()
                    .getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    /**
     * android M以上不再适用
     */
    public static boolean isAppRunning(String packageName) {
        ActivityManager am = ((ActivityManager) AppContext.getAppContext()
                .getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> appProcesses = am.getRunningAppProcesses();

        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (TextUtils.equals(appProcess.processName, packageName)) {
                return true;
            }
        }
        return false;
    }


    public static void openPackage(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) return;
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public static boolean isOverM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isOverN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static boolean isOver10() {
        return Build.VERSION.SDK_INT >= 29;
    }
}
