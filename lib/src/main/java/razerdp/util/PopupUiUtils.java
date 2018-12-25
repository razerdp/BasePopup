package razerdp.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

/**
 * Created by 大灯泡 on 2018/12/23.
 */
public class PopupUiUtils {
    private static final int PORTRAIT = 0;
    private static final int LANDSCAPE = 1;
    private volatile static Point[] mRealSizes = new Point[2];
    private static final DisplayMetrics mCheckNavDisplayMetricsForReal = new DisplayMetrics();
    private static final DisplayMetrics mCheckNavDisplayMetricsForDisplay = new DisplayMetrics();
    private static final String XIAOMI_FULLSCREEN_GESTURE = "force_fsg_nav_bar";


    public static int getNavigationBarHeight(Context context) {
        if (!checkHasNavigationBar(context)) return 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        //获取NavigationBar的高度
        return resources.getDimensionPixelSize(resourceId);
    }

    @SuppressLint("NewApi")
    public static boolean checkHasNavigationBar(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            boolean hasMenuKey = ViewConfiguration.get(context)
                    .hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap
                    .deviceHasKey(KeyEvent.KEYCODE_BACK);

            return !hasMenuKey && !hasBackKey;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager == null) {
            return false;
        }
        Display d = windowManager.getDefaultDisplay();


        d.getRealMetrics(mCheckNavDisplayMetricsForReal);


        int realHeight = mCheckNavDisplayMetricsForReal.heightPixels;
        int realWidth = mCheckNavDisplayMetricsForReal.widthPixels;


        d.getMetrics(mCheckNavDisplayMetricsForDisplay);


        int displayHeight = mCheckNavDisplayMetricsForDisplay.heightPixels;
        int displayWidth = mCheckNavDisplayMetricsForDisplay.widthPixels;


        boolean result = (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;

        if (RomUtil.isMiui()) {
            //判断小米手势全面屏。
            return result && Settings.Global.getInt(context.getContentResolver(), "force_fsg_nav_bar", 0) == 0;
        } else {
            return result;
        }
    }

    public static int getScreenHeightCompat(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDisplayMetrics().heightPixels;
        } else {
            int orientation = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? PORTRAIT : LANDSCAPE;
            if (mRealSizes[orientation] == null) {
                WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                if (windowManager == null) {
                    return context.getResources().getDisplayMetrics().heightPixels;
                }
                Display display = windowManager.getDefaultDisplay();
                Point point = new Point();
                display.getRealSize(point);
                mRealSizes[orientation] = point;
            }
            return mRealSizes[orientation].y - getNavigationBarHeight(context);
        }
    }

    public static int getScreenWidthCompat(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDisplayMetrics().widthPixels;
        } else {
            int orientation = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? PORTRAIT : LANDSCAPE;
            if (mRealSizes[orientation] == null) {
                WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                if (windowManager == null) {
                    return context.getResources().getDisplayMetrics().widthPixels;
                }
                Display display = windowManager.getDefaultDisplay();
                Point point = new Point();
                display.getRealSize(point);
                mRealSizes[orientation] = point;
            }
            return mRealSizes[orientation].x;
        }
    }
}
