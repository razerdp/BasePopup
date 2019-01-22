package razerdp.basepopup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.HashMap;

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
        private final HashMap<String, OnSystemUiVisibilityChangeListenerWrapper> mSystemUiVisibilityChangeListenerBucket = new HashMap<>();

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
            Activity activity = popupWindow.scanForActivity(popupWindow.getContentView().getContext());
            if (activity == null) return;
            Window window = activity.getWindow();
            View decorView = window == null ? null : window.getDecorView();
            String decorStr = decorView == null ? null : String.valueOf(decorView.hashCode());
            OnSystemUiVisibilityChangeListenerWrapper targetWrapper = mSystemUiVisibilityChangeListenerBucket.get(decorStr);
            if (targetWrapper != null) {
                //清除引用
                targetWrapper.clear();
            }
            boolean die = false;
            if (activity.isFinishing()) {
                die = true;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity.isDestroyed()) {
                    die = true;
                }
            }
            if (die) {
                mSystemUiVisibilityChangeListenerBucket.remove(decorStr);
            }
        }


        protected void onBeforeShowExec(BasePopupWindowProxy popupWindowProxy, Activity act) {
            if (OnSystemUiVisibilityChangeListenerWrapper.needListenUiVisibilityChange(act)) {
                popupWindowProxy.setFocusable(false);
                Window window = act.getWindow();
                View decorView = window == null ? null : window.getDecorView();
                String decorStr = decorView == null ? null : String.valueOf(decorView.hashCode());
                if (TextUtils.isEmpty(decorStr)) return;
                OnSystemUiVisibilityChangeListenerWrapper targetWrapper = mSystemUiVisibilityChangeListenerBucket.get(decorStr);
                if (targetWrapper == null) {
                    targetWrapper = new OnSystemUiVisibilityChangeListenerWrapper(act);
                    mSystemUiVisibilityChangeListenerBucket.put(decorStr, targetWrapper);
                }
                if (!targetWrapper.isAttached()) {
                    targetWrapper.attached(act);
                }
                popupWindowProxy.getContentView().setSystemUiVisibility(OnSystemUiVisibilityChangeListenerWrapper.flags);
            }
        }

        protected void onAfterShowExec(BasePopupWindowProxy popupWindowProxy, Activity act) {
            popupWindowProxy.setFocusable(true);
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
            popupWindow.callSuperShowAtLocation(activity.getWindow().getDecorView(), Gravity.NO_GRAVITY, xoff, yoff);
        }

        @Override
        void showAtLocationImpl(Activity activity, BasePopupWindowProxy popupWindow, View parent, int gravity, int x, int y) {
            popupWindow.callSuperShowAtLocation(parent, gravity, x, y);
        }
    }

    static class OnSystemUiVisibilityChangeListenerWrapper implements View.OnSystemUiVisibilityChangeListener {
        private WeakReference<View.OnSystemUiVisibilityChangeListener> target;
        private WeakReference<View> targetView;
        private boolean isAttached;
        private int oldUiVisibility = 0;
        public static final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        public boolean isAttached() {
            return isAttached;
        }

        public OnSystemUiVisibilityChangeListenerWrapper(Activity act) {
            Window window = act.getWindow();
            View decorView = window == null ? null : window.getDecorView();
            checkTarget(decorView);
        }

        public OnSystemUiVisibilityChangeListenerWrapper(View targetView) {
            checkTarget(targetView);
        }

        public void attached(Activity act) {
            if (act == null) return;
            Window window = act.getWindow();
            View decorView = window == null ? null : window.getDecorView();
            if (decorView != null) {
                attached(decorView);
            }
        }

        public void attached(View targetView) {
            this.targetView = new WeakReference<>(targetView);
            checkTarget(targetView);
            targetView.setOnSystemUiVisibilityChangeListener(this);
            isAttached = true;
        }


        private void checkTarget(View targetView) {
            if (target == null) {
                target = new WeakReference<>(tryToGetListener(targetView));
            }
        }

        private View.OnSystemUiVisibilityChangeListener tryToGetListener(View targetView) {
            if (targetView == null) return null;
            return tryToReflectGetListener(targetView);
        }

        @SuppressLint("PrivateApi")
        private View.OnSystemUiVisibilityChangeListener tryToReflectGetListener(View targetView) {
            try {
                oldUiVisibility = targetView.getSystemUiVisibility();
                Field mListenerInfo = View.class.getDeclaredField("mListenerInfo");
                mListenerInfo.setAccessible(true);
                Object viewListenerInfo = mListenerInfo.get(targetView);
                if (viewListenerInfo != null) {
                    Field listenerField = viewListenerInfo.getClass().getDeclaredField("mOnSystemUiVisibilityChangeListener");
                    listenerField.setAccessible(true);
                    return (View.OnSystemUiVisibilityChangeListener) listenerField.get(viewListenerInfo);
                }
            } catch (NoSuchFieldException e) {
                if (Build.VERSION.SDK_INT >= 27) {
                    return tryToReflectOverP(targetView);
                }
                PopupLogUtil.trace(e);
            } catch (Exception e) {
                PopupLogUtil.trace(e);
            }
            return null;
        }

        private View.OnSystemUiVisibilityChangeListener tryToReflectOverP(View targetView) {
            try {
                return PopupReflectionHelper.getInstance().getSystemUiVisibilityChangeListener(targetView);
            } catch (Exception e) {
                PopupLogUtil.trace(e);
            }
            return null;
        }

        @Override
        public void onSystemUiVisibilityChange(int visibility) {
            if (target != null) {
                if (target.get() != null) {
                    target.get().onSystemUiVisibilityChange(visibility);
                }
            }
            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0 &&
                    targetView != null) {
                View decor = targetView.get();
                if (decor != null) {
                    decor.setSystemUiVisibility(flags);
                }
            }
        }

        public void clear() {
            if (targetView != null) {
                View weakTarget = targetView.get();
                View.OnSystemUiVisibilityChangeListener l = target == null ? null : target.get();
                if (weakTarget != null) {
                    weakTarget.setOnSystemUiVisibilityChangeListener(l);
                    restoreToOriginalSystemUi(weakTarget);
                    targetView.clear();
                }
            }
            targetView = null;
            target = null;
            isAttached = false;
        }

        private void restoreToOriginalSystemUi(View targetView) {
            targetView.setSystemUiVisibility(oldUiVisibility);
        }

        public static boolean needListenUiVisibilityChange(Activity act) {
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
}
