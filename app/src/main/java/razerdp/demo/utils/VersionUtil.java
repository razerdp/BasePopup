package razerdp.demo.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import razerdp.demo.app.AppContext;


public class VersionUtil {

    public static String getAppVersionName() {
        try {
            String versionName = "";
            Context context = AppContext.getAppContext();
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_CONFIGURATIONS);
            versionName = packageInfo.versionName;
            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getAppVersionCode() {
        try {
            Context context = AppContext.getAppContext();
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();
            int versionCode = 0;
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_CONFIGURATIONS);
            versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
