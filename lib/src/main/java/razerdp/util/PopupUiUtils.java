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
        Activity act = PopupUtils.scanForActivity(context, 50);
        if (act == null) return false;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            if (window == null) return false;
            Display display = window.getWindowManager().getDefaultDisplay();
            display.getRealSize(point);

            View decorView = window.getDecorView();
            Configuration conf = context.getResources().getConfiguration();
            if (Configuration.ORIENTATION_LANDSCAPE == conf.orientation) {
                View contentView = decorView.findViewById(android.R.id.content);
                return (point.x != contentView.getWidth());
            } else {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                return (rect.bottom != point.y);
            }
        } else {
            ViewGroup decorView = (ViewGroup) act.getWindow().getDecorView();
            if (decorView != null) {
                final int childCount = decorView.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = decorView.getChildAt(i);
                    String resourceEntryName;
                    try {
                        resourceEntryName = act.getResources().getResourceEntryName(child.getId());
                    } catch (Exception e) {
                        continue;
                    }
                    if (child.getId() != View.NO_ID
                            && child.isShown()
                            && TextUtils.equals("navigationBarBackground", resourceEntryName)) {
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
            return context.getResources().getDisplayMetrics().heightPixels + (orientation == PORTRAIT ? (!checkHasNavigationBar(context) ? getNavigationBarHeightInternal(context) : 0) : 0);
        } else {
            if (mRealSizes[orientation] == null) {
                WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                if (windowManager == null) {
                    return context.getResources().getDisplayMetrics().heightPixels + (orientation == PORTRAIT ? (!checkHasNavigationBar(context) ? getNavigationBarHeightInternal(context) : 0) : 0);
                }
                Display display = windowManager.getDefaultDisplay();
                Point point = new Point();
                display.getRealSize(point);
                mRealSizes[orientation] = point;
            }
            return mRealSizes[orientation].y - (orientation == PORTRAIT ? getNavigationBarHeight(context) : 0);
        }
    }

    public static int getScreenWidthCompat(Context context) {
        int orientation = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? PORTRAIT : LANDSCAPE;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return context.getResources().getDisplayMetrics().widthPixels + (orientation == LANDSCAPE ? (!checkHasNavigationBar(context) ? getNavigationBarHeightInternal(context) : 0) : 0);
        } else {
            if (mRealSizes[orientation] == null) {
                WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                if (windowManager == null) {
                    return context.getResources().getDisplayMetrics().widthPixels + (orientation == LANDSCAPE ? (!checkHasNavigationBar(context) ? getNavigationBarHeightInternal(context) : 0) : 0);
                }
                Display display = windowManager.getDefaultDisplay();
                Point point = new Point();
                display.getRealSize(point);
                mRealSizes[orientation] = point;
            }
            return mRealSizes[orientation].x - (orientation == LANDSCAPE ? getNavigationBarHeight(context) : 0);
        }
    }

}
