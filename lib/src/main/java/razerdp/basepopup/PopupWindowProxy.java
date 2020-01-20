package razerdp.basepopup;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
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
    private WindowManagerProxy mWindowManagerProxy;
    private boolean oldFocusable = true;
    private boolean isHandledFullScreen;

    private static final int FULL_SCREEN_FLAG = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_FULLSCREEN;
    private int[] anchorLocation = new int[2];

    PopupWindowProxy(View contentView, int width, int height, BasePopupHelper mHelper) {
        super(contentView, width, height);
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
            mWindowManagerProxy = new WindowManagerProxy(windowManager);
            mWindowManagerProxy.attachPopupHelper(mHelper);
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
            if (mHelper != null) {
                if (mHelper.isOutSideTouchable()) {
                    Rect anchorBound = mHelper.getAnchorViewBound();
                    super.update(anchorBound.left, anchorBound.bottom, mHelper.getPopupViewWidth(), mHelper.getPopupViewHeight(), true);
                }
                if (mWindowManagerProxy != null) {
                    mWindowManagerProxy.update();
                }
            } else {
                super.update();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void handleFullScreenFocusable() {
        oldFocusable = isFocusable();
        setFocusable(false);
        isHandledFullScreen = true;
    }

    void restoreFocusable() {
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

    /**
     * fix showAsDropDown when android api ver is over N
     * <p>
     * https://code.google.com/p/android/issues/detail?id=221001
     *
     * @param anchor
     * @param xoff
     * @param yoff
     * @param gravity
     */
    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (isShowing()) return;
        Activity activity = PopupUtils.getActivity(anchor.getContext());
        if (activity == null) {
            Log.e(TAG, "please make sure that context is instance of activity");
            return;
        }
        onBeforeShowExec(activity);
        anchor.getLocationInWindow(anchorLocation);
        xoff = anchorLocation[0];
        yoff = anchorLocation[1] + anchor.getHeight();
        //offset在basepopupwindow已经计算好，在windowmanagerproxy里面进行适配
        super.showAtLocation(anchor, Gravity.NO_GRAVITY, xoff, yoff);
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
