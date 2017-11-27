package razerdp.basepopup;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 大灯泡 on 2017/11/27.
 * <p>
 * PopupWindow兼容类
 */

final class PopupCompatManager {
    private static final String TAG = "PopupCompatManager";

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


    //-----------------------------------------compat-----------------------------------------
    interface PopupWindowImpl {
        void showAsDropDown(BasePopupWindowProxy popupWindow, View anchor, int xoff, int yoff, int gravity);

        void showAtLocation(BasePopupWindowProxy popupWindow, View parent, int gravity, int x, int y);
    }

    static {
        int buildVersion = Build.VERSION.SDK_INT;
        if (buildVersion == 24) {
            IMPL = new Impl24();
        } else if (buildVersion > 24) {
            IMPL = new ImplOver24();
        } else {
            IMPL = new ImplBefore24();
        }
    }

    //base impl
    static abstract class BaseImpl implements PopupWindowImpl {

        abstract void showAsDropDownImpl(Activity activity, BasePopupWindowProxy popupWindow, View anchor, int xoff, int yoff, int gravity);

        @Override
        public void showAsDropDown(BasePopupWindowProxy popupWindow, View anchor, int xoff, int yoff, int gravity) {
            if (!checkPopupNotShowing(popupWindow)) return;
            Activity activity = popupWindow.scanForActivity(anchor.getContext());
            if (activity == null) {
                Log.e(TAG, "please make sure that context is instance of activity");
                return;
            }
            //复位重试次数#issue 45(https://github.com/razerdp/BasePopup/issues/45)
            popupWindow.resetTryScanActivityCount();
            showAsDropDownImpl(activity, popupWindow, anchor, xoff, yoff, gravity);
        }

        boolean checkPopupNotShowing(BasePopupWindowProxy popupWindow) {
            return popupWindow != null && !popupWindow.callSuperIsShowing();
        }


        /**
         * 修复popup显示时退出沉浸的问题
         *
         * @param view
         */
        void initSystemBar(View view) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN;
                    boolean isFullScreen = view.getSystemUiVisibility() == uiOptions;
                    if (isFullScreen) {
                        hideSystemUI(view);
                    } else {
                        //showSystemUI(view);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void hideSystemUI(View view) {
            if (view == null) return;
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                    view.setSystemUiVisibility(uiOptions);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void showSystemUI(View view) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                    view.setSystemUiVisibility(uiOptions);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //api before 24
    static class ImplBefore24 extends BaseImpl {


        @Override
        void showAsDropDownImpl(Activity activity, BasePopupWindowProxy popupWindow, View anchor, int xoff, int yoff, int gravity) {

        }

        @Override
        public void showAsDropDown(BasePopupWindowProxy popupWindow, View anchor, int xoff, int yoff, int gravity) {
            if (!checkPopupNotShowing(popupWindow)) return;
            popupWindow.callSuperShowAsDropDown(anchor, xoff, yoff, gravity);
            initSystemBar(popupWindow.getContentView());
        }

        @Override
        public void showAtLocation(BasePopupWindowProxy popupWindow, View parent, int gravity, int x, int y) {
            if (!checkPopupNotShowing(popupWindow)) return;
            popupWindow.callSuperShowAtLocation(parent, gravity, x, y);
            initSystemBar(popupWindow.getContentView());
        }
    }

    //api 24
    static class Impl24 extends BaseImpl {

        @Override
        void showAsDropDownImpl(Activity activity, BasePopupWindowProxy popupWindow, View anchor, int xoff, int yoff, int gravity) {
            int[] anchorLocation = new int[2];
            anchor.getLocationInWindow(anchorLocation);

            xoff = anchorLocation[0] + xoff;
            yoff = anchorLocation[1] + anchor.getHeight() + yoff;

            popupWindow.callSuperShowAtLocation(activity.getWindow().getDecorView(), Gravity.NO_GRAVITY, xoff, yoff);
            initSystemBar(popupWindow.getContentView());
        }

        @Override
        public void showAtLocation(BasePopupWindowProxy popupWindow, View parent, int gravity, int x, int y) {
            if (!checkPopupNotShowing(popupWindow)) return;
            popupWindow.callSuperShowAtLocation(parent, gravity, x, y);
            initSystemBar(popupWindow.getContentView());
        }
    }

    //api over 24
    static class ImplOver24 extends BaseImpl {

        @Override
        void showAsDropDownImpl(Activity activity, BasePopupWindowProxy popupWindow, View anchor, int xoff, int yoff, int gravity) {
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.callSuperShowAsDropDown(anchor, xoff, yoff, gravity);
            initSystemBar(popupWindow.getContentView());
        }

        @Override
        public void showAtLocation(BasePopupWindowProxy popupWindow, View parent, int gravity, int x, int y) {
            if (!checkPopupNotShowing(popupWindow)) return;
            popupWindow.callSuperShowAtLocation(parent, gravity, x, y);
            initSystemBar(popupWindow.getContentView());
        }
    }
}
