package razerdp.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.SparseArray;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupComponentManager;

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
                        return true;
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
        switch (rotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                return REAL_SIZE.get(Configuration.ORIENTATION_PORTRAIT).y;
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                return REAL_SIZE.get(Configuration.ORIENTATION_LANDSCAPE).y;
        }
        return REAL_SIZE.get(Configuration.ORIENTATION_PORTRAIT).y;
    }

    private static void checkRealSize() {
        Resources resources = BasePopupComponentManager.getApplication().getResources();
        int orientation = resources.getConfiguration().orientation;
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
        switch (rotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                return REAL_SIZE.get(Configuration.ORIENTATION_PORTRAIT).x;
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                return REAL_SIZE.get(Configuration.ORIENTATION_LANDSCAPE).x;
        }
        return REAL_SIZE.get(Configuration.ORIENTATION_PORTRAIT).x;
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
}
