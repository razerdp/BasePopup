package razerdp.util;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import razerdp.basepopup.BasePopupSDK;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2018/12/23.
 */
public class PopupUiUtils {

    public static final String POPUP_DECORVIEW = "android.widget.PopupWindow$PopupDecorView";
    public static final String POPUP_VIEWCONTAINER = "android.widget.PopupWindow$PopupViewContainer";
    public static final String POPUP_BACKGROUNDVIEW = "android.widget.PopupWindow$PopupBackgroundView";


    public static boolean isPopupDecorView(View view) {
        return view != null && TextUtils.equals(view.getClass().getName(), POPUP_DECORVIEW);
    }

    public static boolean isPopupViewContainer(View view) {
        return view != null && TextUtils.equals(view.getClass().getName(), POPUP_VIEWCONTAINER);
    }

    public static boolean isPopupBackgroundView(View view) {
        return view != null && TextUtils.equals(view.getClass().getName(), POPUP_BACKGROUNDVIEW);
    }

    private static int statusBarHeight;

    public static void requestFocus(View v) {
        if (v == null) return;
        if (v.isInTouchMode()) {
            v.requestFocusFromTouch();
        } else {
            v.requestFocus();
        }
    }

    public static int getScreenOrientation() {
        return BasePopupSDK.getApplication().getResources().getConfiguration().orientation;
    }

    public static int getStatusBarHeight() {
        checkStatusBarHeight();
        return statusBarHeight;
    }

    private static void checkStatusBarHeight() {
        if (statusBarHeight != 0) return;
        int result = 0;
        //获取状态栏高度的资源id
        Resources resources = BasePopupSDK.getApplication().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        statusBarHeight = result;
    }

    public static void setBackground(View v, Drawable background) {
        if (v == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackground(background);
        } else {
            v.setBackgroundDrawable(background);
        }
    }

    public static void safeAddGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener) {
        try {
            v.getViewTreeObserver().addOnGlobalLayoutListener(listener);
        } catch (Exception e) {
            PopupLog.e(e);
        }
    }

    public static void safeRemoveGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener) {
        try {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        } catch (Exception e) {
            PopupLog.e(e);
        }
    }

    public static boolean isActivityFullScreen(Activity act) {
        if (act == null || act.getWindow() == null) return false;
        return (act.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN;

    }
}
