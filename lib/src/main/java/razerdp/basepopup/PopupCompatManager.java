package razerdp.basepopup;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import razerdp.util.log.LogTag;
import razerdp.util.log.PopupLogUtil;

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
        private int tryScanActivityCount = 0;
        private static final int MAX_SCAN_ACTIVITY_COUNT = 50;

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
            //复位重试次数#issue 45(https://github.com/razerdp/BasePopup/issues/45)
            popupWindow.resetTryScanActivityCount();
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
            popupWindow.resetTryScanActivityCount();
            showAtLocationImpl(activity, popupWindow, parent, gravity, x, y);
            onAfterShowExec(popupWindow, activity);
        }


        protected void onBeforeShowExec(BasePopupWindowProxy popupWindowProxy, Activity act) {

        }

        protected void onAfterShowExec(BasePopupWindowProxy popupWindowProxy, Activity act) {
            fitSystemBar(act, popupWindowProxy.getContentView());
        }

        boolean isPopupShowing(BasePopupWindowProxy popupWindow) {
            return popupWindow != null && popupWindow.callSuperIsShowing();
        }

        void resetTryScanActivityCount() {
            tryScanActivityCount = 0;
        }


        /**
         * fix context cast exception
         * <p>
         * android.view.ContextThemeWrapper
         * <p>
         * https://github.com/razerdp/BasePopup/pull/26
         *
         * @param from
         * @return
         * @author: hshare
         * @author: razerdp optimize on 2018/4/25
         */
        Activity scanForActivity(Context from) {
            Context result = from;
            while (result instanceof ContextWrapper) {
                if (result instanceof Activity) {
                    resetTryScanActivityCount();
                    return (Activity) result;
                }
                if (tryScanActivityCount > MAX_SCAN_ACTIVITY_COUNT) {
                    //break endless loop
                    return null;
                }
                tryScanActivityCount++;
                PopupLogUtil.trace(LogTag.i, TAG, "scanForActivity: " + tryScanActivityCount);
                result = ((ContextWrapper) result).getBaseContext();
            }
            return null;
        }

        /**
         * 修复popup显示时退出沉浸的问题
         * 如果activity是全屏的，则popupwindow的window也应该
         */
        void fitSystemBar(Activity act, View popupContentView) {
            if (act == null) return;
            try {
                WindowManager.LayoutParams lp = act.getWindow().getAttributes();
                int i = lp.flags;
                if ((i & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0) {
                    //full screen
                    hideSystemUI(act, popupContentView);
                } else {
                    //showSystemUI(view);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void hideSystemUI(Activity act, View popupContentView) {
            if (popupContentView == null) return;
            try {
                int uiOptions = popupContentView.getSystemUiVisibility();
                int newUiOptions = uiOptions;

                if (Build.VERSION.SDK_INT >= 14) {
                    newUiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
                }

                if (Build.VERSION.SDK_INT >= 16) {
                    newUiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                }

                if (Build.VERSION.SDK_INT >= 19) {
                    newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                }

                popupContentView.setSystemUiVisibility(newUiOptions);
                PopupLogUtil.trace("hideSystemBar");
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
            popupWindow.callSuperShowAsDropDown(anchor, xoff, yoff, gravity);
        }

        @Override
        void showAtLocationImpl(Activity activity, BasePopupWindowProxy popupWindow, View parent, int gravity, int x, int y) {
            popupWindow.callSuperShowAtLocation(parent, gravity, x, y);
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
        }

        @Override
        void showAtLocationImpl(Activity activity, BasePopupWindowProxy popupWindow, View parent, int gravity, int x, int y) {
            popupWindow.callSuperShowAtLocation(parent, gravity, x, y);

        }
    }

    //api over 24
    static class ImplOver24 extends BaseImpl {

        @Override
        void showAsDropDownImpl(Activity activity, BasePopupWindowProxy popupWindow, View anchor, int xoff, int yoff, int gravity) {
            popupWindow.callSuperShowAsDropDown(anchor, xoff, yoff, gravity);
        }

        @Override
        void showAtLocationImpl(Activity activity, BasePopupWindowProxy popupWindow, View parent, int gravity, int x, int y) {
            popupWindow.callSuperShowAtLocation(parent, gravity, x, y);
        }
    }
}
