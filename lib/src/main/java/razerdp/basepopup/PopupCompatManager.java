package razerdp.basepopup;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by 大灯泡 on 2017/11/27.
 * <p>
 * PopupWindow兼容类
 */

final class PopupCompatManager {
    private static final String TAG = "PopupCompatManager";
    private static final int FULL_SCREEN_FLAG = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_FULLSCREEN;
    private static final PopupWindowImpl IMPL;

    public static void showAsDropDown(BasePopupWindowProxy popupWindow, View anchor, int xoff, int yoff, int gravity) {
        if (IMPL != null) {
            IMPL.showAsDropDown(popupWindow, anchor, xoff, yoff, gravity);
        }
    }

    public static void showAtLocation(BasePopupWindowProxy popupWindow, View parent, int gravity, int x, int y) {
        if (IMPL != null) {
            IMPL.showAtLocation(popupWindow, parent, gravity, x, y);
        }
    }

    public static void clear(BasePopupWindowProxy popupWindow) {
        if (IMPL != null) {
            IMPL.clear(popupWindow);
        }
    }


    //-----------------------------------------compat-----------------------------------------
    interface PopupWindowImpl {
        void showAsDropDown(BasePopupWindowProxy popupWindow, View anchor, int xoff, int yoff, int gravity);

        void showAtLocation(BasePopupWindowProxy popupWindow, View parent, int gravity, int x, int y);

        void clear(BasePopupWindowProxy popupWindow);
    }

    static {
        IMPL = new Impl();
    }

    //base impl
    static abstract class BaseImpl implements PopupWindowImpl {

        abstract void showAsDropDownImpl(Activity activity, BasePopupWindowProxy popupWindow, View anchor, int xoff, int yoff, int gravity);

        abstract void showAtLocationImpl(Activity activity, BasePopupWindowProxy popupWindow, View parent, int gravity, int x, int y);

        @Override
        public void showAsDropDown(BasePopupWindowProxy popupWindow, View anchor, int xoff, int yoff, int gravity) {
            if (isPopupShowing(popupWindow)) return;
            Activity activity = popupWindow.scanForActivity(anchor.getContext());
            if (activity == null) {
                Log.e(TAG, "please make sure that context is instance of activity");
                return;
            }
            onBeforeShowExec(popupWindow, activity);
            showAsDropDownImpl(activity, popupWindow, anchor, xoff, yoff, gravity);
            onAfterShowExec(popupWindow, activity);
        }

        @Override
        public void showAtLocation(BasePopupWindowProxy popupWindow, View parent, int gravity, int x, int y) {
            if (isPopupShowing(popupWindow)) return;
            Activity activity = popupWindow.scanForActivity(parent.getContext());
            if (activity == null) {
                Log.e(TAG, "please make sure that context is instance of activity");
                return;
            }
            onBeforeShowExec(popupWindow, activity);
            showAtLocationImpl(activity, popupWindow, parent, gravity, x, y);
            onAfterShowExec(popupWindow, activity);
        }

        @Override
        public void clear(BasePopupWindowProxy popupWindow) {

        }


        protected void onBeforeShowExec(BasePopupWindowProxy popupWindowProxy, Activity act) {
            if (needListenUiVisibilityChange(act)) {
                popupWindowProxy.handleFullScreenFocusable();
            }
        }

        protected void onAfterShowExec(BasePopupWindowProxy popupWindowProxy, Activity act) {
            if (popupWindowProxy.isHandledFullScreen()) {
                popupWindowProxy.getContentView().setSystemUiVisibility(FULL_SCREEN_FLAG);
                popupWindowProxy.restoreFocusable();
            }
        }

        boolean isPopupShowing(BasePopupWindowProxy popupWindow) {
            return popupWindow != null && popupWindow.callSuperIsShowing();
        }
    }


    static class Impl extends BaseImpl {
        int[] anchorLocation = new int[2];

        @Override
        void showAsDropDownImpl(Activity activity, BasePopupWindowProxy popupWindow, View anchor, int xoff, int yoff, int gravity) {
            if (anchor != null) {
                anchor.getLocationInWindow(anchorLocation);
                xoff = anchorLocation[0];
                yoff = anchorLocation[1] + anchor.getHeight();
            }
            //offset在basepopupwindow已经计算好，在windowmanagerproxy里面进行适配
            popupWindow.callSuperShowAtLocation(anchor, Gravity.NO_GRAVITY, xoff, yoff);
        }

        @Override
        void showAtLocationImpl(Activity activity, BasePopupWindowProxy popupWindow, View parent, int gravity, int x, int y) {
            popupWindow.callSuperShowAtLocation(parent, gravity, x, y);
        }
    }

    private static boolean needListenUiVisibilityChange(Activity act) {
        if (act == null) return false;
        try {
            View decorView = act.getWindow().getDecorView();
            WindowManager.LayoutParams lp = act.getWindow().getAttributes();
            int i = lp.flags;
            int f = decorView.getWindowSystemUiVisibility();

            return (i & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0
                    && ((f & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) != 0 || (f & View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION) != 0);
        } catch (Exception e) {
            return false;
        }
    }
}
