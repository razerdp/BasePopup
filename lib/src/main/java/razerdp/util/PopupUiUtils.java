package razerdp.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.util.SparseArray;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupComponentManager;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2018/12/23.
 */
public class PopupUiUtils {


    private static final List<String> NAVIGATION_BAR_NAMES = new ArrayList<>();

    static {
        NAVIGATION_BAR_NAMES.add("navigationbarbackground");
        NAVIGATION_BAR_NAMES.add("immersion_navigation_bar_view");
    }

    private static final SparseArray<Point> REAL_SIZE = new SparseArray<>();
    private static int statusBarHeight;


    public static int getNavigationBarHeight() {
        Resources resources = BasePopupComponentManager.getApplication().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            //获取NavigationBar的高度
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 方法参考
     * https://juejin.im/post/5bb5c4e75188255c72285b54
     */
    @SuppressLint("NewApi")
    public static boolean hasNavigationBar(Context context) {
        Activity act = PopupUtils.getActivity(context);
        if (!PopupUtils.isActivityAlive(act)) return false;
        ViewGroup decorView = (ViewGroup) act.getWindow().getDecorView();
        final int childCount = decorView.getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            View child = decorView.getChildAt(i);
            if (child.getId() == View.NO_ID || !child.isShown()) continue;
            String resourceEntryName;
            try {
                resourceEntryName = act.getResources().getResourceEntryName(child.getId());
                if (NAVIGATION_BAR_NAMES.contains(resourceEntryName.toLowerCase())) {
                    if ((decorView.getSystemUiVisibility() & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {
                        //如果有手势导航栏，navigationbar始终返回为true，此时需要进一步判断
                        //如果有手势导航栏，因为一般手势导航栏是很小的一块，因此可以当作木有。。。
                        return !hasGestureNavigation();
                    }
                }
            } catch (Exception e) {
                //do nothing
            }
        }
        return false;
    }

    public static int getScreenHeightCompat() {
        checkRealSize();

        int rotation = getScreenRotation();
        int result = REAL_SIZE.get(getScreenOrientation()).y;
        try {
            switch (rotation) {
                case Surface.ROTATION_0:
                case Surface.ROTATION_180:
                    result = REAL_SIZE.get(Configuration.ORIENTATION_PORTRAIT).y;
                    break;
                case Surface.ROTATION_90:
                case Surface.ROTATION_270:
                    result = REAL_SIZE.get(Configuration.ORIENTATION_LANDSCAPE).y;
                    break;
            }
        } catch (Exception e) {
            //部分魔改系统会返回错误的rotation，导致npe产生
            PopupLog.e(e);
        }

        return result;
    }

    private static void checkRealSize() {
        Resources resources = BasePopupComponentManager.getApplication().getResources();
        int orientation = getScreenOrientation();
        if (REAL_SIZE.get(orientation) != null) return;
        WindowManager windowManager = (WindowManager) BasePopupComponentManager.getApplication().getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (windowManager == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            point.x = resources.getDisplayMetrics().widthPixels;
            point.y = resources.getDisplayMetrics().heightPixels;
        } else {
            windowManager.getDefaultDisplay().getRealSize(point);
        }
        REAL_SIZE.put(orientation, point);
    }

    public static int getScreenWidthCompat() {
        checkRealSize();

        int rotation = getScreenRotation();
        int result = REAL_SIZE.get(getScreenOrientation()).x;
        try {
            switch (rotation) {
                case Surface.ROTATION_0:
                case Surface.ROTATION_180:
                    result = REAL_SIZE.get(Configuration.ORIENTATION_PORTRAIT).x;
                    break;
                case Surface.ROTATION_90:
                case Surface.ROTATION_270:
                    result = REAL_SIZE.get(Configuration.ORIENTATION_LANDSCAPE).x;
                    break;
            }
        } catch (Exception e) {
            //部分魔改系统会返回错误的rotation，导致npe产生
            PopupLog.e(e);
        }
        return result;
    }

    public static int getScreenOrientation() {
        return BasePopupComponentManager.getApplication().getResources().getConfiguration().orientation;
    }

    public static int getStatusBarHeight() {
        checkStatusBarHeight();
        return statusBarHeight;
    }

    private static void checkStatusBarHeight() {
        if (statusBarHeight != 0) return;
        int result = 0;
        //获取状态栏高度的资源id
        Resources resources = BasePopupComponentManager.getApplication().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        statusBarHeight = result;
    }

    /**
     * 获取屏幕旋转角度
     * <p>
     * 0表示是竖屏; 90表示是左横屏; 180表示是反向竖屏; 270表示是右横屏
     *
     * @return one of {@link Surface#ROTATION_0 },{@link Surface#ROTATION_90 },{@link Surface#ROTATION_180 },{@link Surface#ROTATION_270 }
     */
    public static int getScreenRotation() {
        try {
            return ((WindowManager) BasePopupComponentManager.getApplication().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        } catch (Exception e) {
            return Surface.ROTATION_0;
        }
    }

    public static void setBackground(View v, Drawable background) {
        if (v == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackground(background);
        } else {
            v.setBackgroundDrawable(background);
        }
    }


    //======================
    private static final String GESTURE_NAV_XVIVO = "navigation_gesture_on";
    private static final String GESTURE_NAV_XIAOMI = "force_fsg_nav_bar";
    private static final String GESTURE_NAVA_SAMSUNG = "navigationbar_hide_bar_enabled";

    /**
     * 是否拥有手势导航栏，蛋疼的一逼，各个ROM都有自己的参数
     */
    private static boolean hasGestureNavigation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) return false;
        if (RomUtils.isXiaomi()) {
            return Settings.Global.getInt(BasePopupComponentManager.getApplication().getContentResolver(), GESTURE_NAV_XIAOMI, 0) != 0;
        }
        if (RomUtils.isVivo()) {
            return Settings.Secure.getInt(BasePopupComponentManager.getApplication().getContentResolver(), GESTURE_NAV_XVIVO, 0) != 0;
        }
        if (RomUtils.isSamsung()) {
            return Settings.Global.getInt(BasePopupComponentManager.getApplication().getContentResolver(), GESTURE_NAVA_SAMSUNG, 0) != 0;
        }
        return false;
    }
}
