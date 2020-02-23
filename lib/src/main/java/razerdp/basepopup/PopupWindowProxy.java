package razerdp.basepopup;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.lang.reflect.Field;

import razerdp.util.PopupUtils;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2017/1/12.
 * <p>
 * 与basePopupWindow强引用(或者说与PopupController强引用)
 */

class PopupWindowProxy extends PopupWindow implements ClearMemoryObject {
    private static final String TAG = "PopupWindowProxy";

    private BasePopupHelper mHelper;
    WindowManagerProxy mWindowManagerProxy;
    private boolean oldFocusable = true;
    private boolean isHandledFullScreen;

    private static final int FULL_SCREEN_FLAG = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_FULLSCREEN;
    private int[] anchorLocation = new int[2];

    PopupWindowProxy(View contentView, BasePopupHelper mHelper) {
        super(contentView);
        this.mHelper = mHelper;
        init();
    }

    private void init() {
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable());
        tryToHookWindowManager();
    }

    @Override
    public void setContentView(View contentView) {
        super.setContentView(contentView);
        tryToHookWindowManager();
    }


    private void tryToHookWindowManager() {
        if (mWindowManagerProxy != null || mHelper == null) return;
        try {
            Field fieldWindowManager = PopupWindow.class.getDeclaredField("mWindowManager");
            fieldWindowManager.setAccessible(true);
            final WindowManager windowManager = (WindowManager) fieldWindowManager.get(this);
            if (windowManager == null) return;
            mWindowManagerProxy = new WindowManagerProxy(windowManager, mHelper);
            fieldWindowManager.set(this, mWindowManagerProxy);

            Field fieldScroll = PopupWindow.class.getDeclaredField("mOnScrollChangedListener");
            fieldScroll.setAccessible(true);
            fieldScroll.set(this, null);

        } catch (Exception ignore) {
            PopupLog.e(TAG, ignore);
        }
    }

    @Override
    public void update() {
        try {
            if (mHelper != null && mWindowManagerProxy != null) {
                mWindowManagerProxy.update();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleFullScreenFocusable() {
        oldFocusable = isFocusable();
        setFocusable(false);
        isHandledFullScreen = true;
    }

    private void restoreFocusable() {
        if (mWindowManagerProxy != null) {
            mWindowManagerProxy.updateFocus(oldFocusable);
        }
        isHandledFullScreen = false;
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (isShowing()) return;
        Activity activity = PopupUtils.getActivity(parent.getContext());
        if (activity == null) {
            Log.e(TAG, "please make sure that context is instance of activity");
            return;
        }
        onBeforeShowExec(activity);
        super.showAtLocation(parent, gravity, x, y);
        onAfterShowExec(activity);
    }

    void onBeforeShowExec(Activity act) {
        if (needObserverUiVisibilityChange(act)) {
            handleFullScreenFocusable();
        }
    }

    void onAfterShowExec(Activity act) {
        if (isHandledFullScreen) {
            getContentView().setSystemUiVisibility(FULL_SCREEN_FLAG);
            restoreFocusable();
        }
    }

    boolean needObserverUiVisibilityChange(Activity act) {
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

    @Override
    public void dismiss() {
        mHelper.dismiss(true);
    }

    void superDismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clear(false);
        }
    }

    @Override
    public void clear(boolean destroy) {
        if (mWindowManagerProxy != null) {
            mWindowManagerProxy.clear(destroy);
        }
        PopupUtils.clearViewFromParent(getContentView());
        if (destroy) {
            mHelper = null;
            mWindowManagerProxy = null;
        }
    }
}
