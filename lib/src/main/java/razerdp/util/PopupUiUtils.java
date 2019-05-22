package razerdp.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by 大灯泡 on 2018/12/23.
 */
public class PopupUiUtils {

    private static final int PORTRAIT = 0;
    private static final int LANDSCAPE = 1;
    private volatile static Point[] mRealSizes = new Point[2];
    private static final Point point = new Point();
    private static Rect navigationBarRect = new Rect();
    private static int statusBarHeight;


    public static int getNavigationBarHeight(Context context) {
        if (!checkHasNavigationBar(context)) return 0;
        return getNavigationBarHeightInternal(context);
    }

    private static int getNavigationBarHeightInternal(Context context) {
        Resources resources = context.getResources();
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
    public static boolean checkHasNavigationBar(Context context) {
        navigationBarRect.setEmpty();
        Activity act = PopupUtils.scanForActivity(context, 15);
        if (act == null) return false;
        Configuration conf = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            if (window == null) return false;
            Display display = window.getWindowManager().getDefaultDisplay();
            display.getRealSize(point);

            View decorView = window.getDecorView();
            if (Configuration.ORIENTATION_LANDSCAPE == conf.orientation) {
                View contentView = decorView.findViewById(android.R.id.content);
                boolean result = point.x != contentView.getWidth();
                if (result) {
                    if (decorView.getLeft() > 0) {
                        //on left
                        navigationBarRect.set(1, 0, 0, 0);
                    } else {
                        //on right
                        navigationBarRect.set(0, 0, 1, 0);
                    }
                }
                return result;
            } else {
                decorView.getWindowVisibleDisplayFrame(navigationBarRect);
                boolean result = navigationBarRect.bottom != point.y;
                navigationBarRect.setEmpty();
                if (result) {
                    navigationBarRect.set(0, 0, 0, 1);
                }
                return result;
            }
        } else {
            ViewGroup decorView = (ViewGroup) act.getWindow().getDecorView();
            final int childCount = decorView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = decorView.getChildAt(i);
                if (child.getId() == View.NO_ID) continue;
                String resourceEntryName;
                try {
                    resourceEntryName = act.getResources().getResourceEntryName(child.getId());
                } catch (Exception e) {
                    continue;
                }
                if (!TextUtils.isEmpty(resourceEntryName) && child.getId() != View.NO_ID && child.isShown()) {
                    if (TextUtils.equals("navigationbarbackground", resourceEntryName.toLowerCase()) ||
                            TextUtils.equals("immersion_navigation_bar_view", resourceEntryName.toLowerCase())) {
                        if (conf.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            //横屏
                            if (child.getWidth() > child.getHeight()) {
                                //bottom
                                navigationBarRect.set(0, 0, 0, 1);
                                //navigation never on top
                            } else {
                                //left or right
                                if (child.getLeft() == 0) {
                                    navigationBarRect.set(1, 0, 0, 0);
                                } else {
                                    navigationBarRect.set(0, 0, 1, 0);
                                }
                            }
                        } else {
                            //bottom
                            navigationBarRect.set(0, 0, 0, 1);
                            //navigation never on top
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static int getScreenHeightCompat(Context context) {
        int orientation = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? PORTRAIT : LANDSCAPE;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //如果没有navigation，则需要加上，因为displayMetrics不包含navigation
            return context.getResources().getDisplayMetrics().heightPixels + (orientation == PORTRAIT ? getFixedPortratiNavigationBarHeight(context) : 0);
        } else {
            if (mRealSizes[orientation] == null) {
                WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                if (windowManager == null) {
                    return context.getResources().getDisplayMetrics().heightPixels + (orientation == PORTRAIT ? getFixedPortratiNavigationBarHeight(context) : 0);
                }
                Display display = windowManager.getDefaultDisplay();
                Point point = new Point();
                display.getRealSize(point);
                mRealSizes[orientation] = point;
            }
            //如果包含navigation，则减去，因为realsizes包含navigation
            int offset = checkHasNavigationBar(context) ? navigationBarRect.bottom != 0 ? getNavigationBarHeightInternal(context) : 0 : 0;
            return mRealSizes[orientation].y - offset;
        }
    }

    public static int getScreenWidthCompat(Context context) {
        int orientation = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? PORTRAIT : LANDSCAPE;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //如果没有navigation，则需要加上，因为displayMetrics不包含navigation
            return context.getResources().getDisplayMetrics().widthPixels + (orientation == LANDSCAPE ? getFixedLandScapeNavigationBarHeight(context) : 0);
        } else {
            if (mRealSizes[orientation] == null) {
                WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                if (windowManager == null) {
                    return context.getResources().getDisplayMetrics().widthPixels + (orientation == LANDSCAPE ? getFixedLandScapeNavigationBarHeight(context) : 0);
                }
                Display display = windowManager.getDefaultDisplay();
                Point point = new Point();
                display.getRealSize(point);
                mRealSizes[orientation] = point;
            }
            //如果包含navigation，则减去，因为realsizes包含navigation
            int offset = checkHasNavigationBar(context) ? navigationBarRect.right != 0 ? getNavigationBarHeightInternal(context) : 0 : 0;
            return mRealSizes[orientation].x - offset;
        }
    }

    private static int getFixedPortratiNavigationBarHeight(Context context) {
        //只有navigation在下面，才返回
        if (!checkHasNavigationBar(context)) {
            if (navigationBarRect.bottom != 0) {
                return getNavigationBarHeightInternal(context);
            }
        }
        return 0;
    }

    private static int getFixedLandScapeNavigationBarHeight(Context context) {
        //只有navigation在右边，才返回
        if (!checkHasNavigationBar(context)) {
            if (navigationBarRect.right != 0) {
                return getNavigationBarHeightInternal(context);
            }
        }
        return 0;
    }

    public static int getScreenOrientation(Context context) {
        if (context == null) return Configuration.ORIENTATION_PORTRAIT;
        return context.getResources().getConfiguration().orientation;
    }

    public static int getStatusBarHeight(Context context) {
        checkStatusBarHeight(context);
        return statusBarHeight;
    }

    private static void checkStatusBarHeight(Context context) {
        if (statusBarHeight != 0 || context == null) return;
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        statusBarHeight = result;
    }
}
