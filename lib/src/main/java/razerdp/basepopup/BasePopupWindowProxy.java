package razerdp.basepopup;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.lang.reflect.Field;

import razerdp.util.log.LogTag;
import razerdp.util.log.PopupLogUtil;

/**
 * Created by 大灯泡 on 2017/11/27.
 * <p>
 * BasePopup代理
 */

abstract class BasePopupWindowProxy extends PopupWindow {
    private static final String TAG = "BasePopupWindowProxy";

    private static final int MAX_SCAN_ACTIVITY_COUNT = 50;
    private volatile int tryScanActivityCount = 0;
    private PopupController mController;
    private HackWindowManager hackWindowManager;

    public BasePopupWindowProxy(Context context, PopupController mController) {
        super(context);
        this.mController = mController;
        init();
    }

    public BasePopupWindowProxy(Context context, AttributeSet attrs, PopupController mController) {
        super(context, attrs);
        this.mController = mController;
        init();
    }

    public BasePopupWindowProxy(Context context, AttributeSet attrs, int defStyleAttr, PopupController mController) {
        super(context, attrs, defStyleAttr);
        this.mController = mController;
        init();
    }

    public BasePopupWindowProxy(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, PopupController mController) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mController = mController;
        init();
    }

    public BasePopupWindowProxy(View contentView, PopupController mController) {
        super(contentView);
        this.mController = mController;
        init();
    }

    public BasePopupWindowProxy(int width, int height, PopupController mController) {
        super(width, height);
        this.mController = mController;
        init();
    }

    public BasePopupWindowProxy(View contentView, int width, int height, PopupController mController) {
        super(contentView, width, height);
        this.mController = mController;
        init();
    }

    public BasePopupWindowProxy(View contentView, int width, int height, boolean focusable, PopupController mController) {
        super(contentView, width, height, focusable);
        this.mController = mController;
        init();
    }

    void bindPopupHelper(BasePopupHelper mHelper) {
        if (hackWindowManager == null) {
            tryToProxyWindowManagerMethod(this);
        }
        hackWindowManager.bindPopupHelper(mHelper);
    }

    private void init() {
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable());
        tryToProxyWindowManagerMethod(this);
    }

    @Override
    public void setContentView(View contentView) {
        super.setContentView(contentView);
        tryToProxyWindowManagerMethod(this);
    }

    void callSuperShowAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        Activity activity = scanForActivity(anchor.getContext());
        if (activity == null) {
            Log.e(TAG, "please make sure that context is instance of activity");
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            super.showAsDropDown(anchor, xoff, yoff, gravity);
        } else {
            super.showAsDropDown(anchor, xoff, yoff);
        }

    }

    void callSuperShowAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
    }


    boolean callSuperIsShowing() {
        return super.isShowing();
    }

    void resetTryScanActivityCount() {
        //复位重试次数#issue 45(https://github.com/razerdp/BasePopup/issues/45)
        tryScanActivityCount = 0;
    }


    /**
     * fix context cast exception
     * <p>
     * android.view.ContextThemeWrapper
     * <p>
     * https://github.com/razerdp/BasePopup/pull/26
     *
     * @param cont
     * @return
     * @author: hshare
     */
    Activity scanForActivity(Context cont) {
        if (cont == null) {
            return null;
        } else if (cont instanceof Activity) {
            return (Activity) cont;
        } else if (cont instanceof ContextWrapper) {
            if (tryScanActivityCount > MAX_SCAN_ACTIVITY_COUNT) {
                //break endless loop
                return null;
            }
            tryScanActivityCount++;
            PopupLogUtil.trace(LogTag.i, TAG, "scanForActivity: " + tryScanActivityCount);
            return scanForActivity(((ContextWrapper) cont).getBaseContext());
        }
        return null;
    }


    @Override
    public void dismiss() {
        if (mController == null) return;

        boolean performDismiss = mController.onBeforeDismiss();
        if (!performDismiss) return;
        boolean dismissAtOnce = mController.callDismissAtOnce();
        if (dismissAtOnce) {
            callSuperDismiss();
        }
    }

    void callSuperDismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            Log.e(TAG, "dismiss error");
            e.printStackTrace();
        }
    }

    /**
     * 尝试代理掉windowmanager
     *
     * @param popupWindow
     */
    private void tryToProxyWindowManagerMethod(PopupWindow popupWindow) {
        try {
            if (mController == null || hackWindowManager != null) return;
            Field fieldWindowManager = PopupWindow.class.getDeclaredField("mWindowManager");
            fieldWindowManager.setAccessible(true);
            final WindowManager windowManager = (WindowManager) fieldWindowManager.get(popupWindow);
            if (windowManager == null) return;
            hackWindowManager = new HackWindowManager(windowManager, mController);
            fieldWindowManager.set(popupWindow, hackWindowManager);
            PopupLogUtil.trace(LogTag.i, TAG, "尝试代理WindowManager成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
