package razerdp.basepopup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.lang.reflect.Field;

import razerdp.util.PopupUtils;
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
    private BasePopupHelper mHelper;
    private WindowManagerProxy mWindowManagerProxy;
    private boolean oldFocusable = true;
    private boolean isHandledFullScreen;

    public BasePopupWindowProxy(Context context, BasePopupHelper helper) {
        super(context);
        this.mHelper = helper;
        init(context);
    }

    public BasePopupWindowProxy(Context context, AttributeSet attrs, BasePopupHelper helper) {
        super(context, attrs);
        this.mHelper = helper;
        init(context);
    }

    public BasePopupWindowProxy(Context context, AttributeSet attrs, int defStyleAttr, BasePopupHelper helper) {
        super(context, attrs, defStyleAttr);
        this.mHelper = helper;
        init(context);
    }

    public BasePopupWindowProxy(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, BasePopupHelper helper) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mHelper = helper;
        init(context);
    }

    public BasePopupWindowProxy(View contentView, BasePopupHelper helper) {
        super(contentView);
        this.mHelper = helper;
        init(contentView.getContext());
    }


    public BasePopupWindowProxy(View contentView, int width, int height, BasePopupHelper helper) {
        super(contentView, width, height);
        this.mHelper = helper;
        init(contentView.getContext());
    }

    public BasePopupWindowProxy(View contentView, int width, int height, boolean focusable, BasePopupHelper helper) {
        super(contentView, width, height, focusable);
        this.mHelper = helper;
        init(contentView.getContext());
    }

    void attachPopupHelper(BasePopupHelper mHelper) {
        if (mWindowManagerProxy == null) {
            tryToProxyWindowManagerMethod(this);
        }
        mWindowManagerProxy.attachPopupHelper(mHelper);
    }

    private void init(Context context) {
//       int i = Reflection.unseal(context);
//        Log.d(TAG, "init: "+i);
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
        return PopupUtils.scanForActivity(from, MAX_SCAN_ACTIVITY_COUNT);
    }


    @Override
    public void dismiss() {
        if (mHelper == null) return;
        boolean performDismiss = mHelper.onBeforeDismiss();
        if (!performDismiss) return;
        boolean dismissAtOnce = mHelper.callDismissAtOnce();
        if (dismissAtOnce) {
            callSuperDismiss();
        }
    }

    void callSuperDismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clear();
        }
    }

    private void clear() {
        if (mWindowManagerProxy != null) {
            mWindowManagerProxy.clear();
        }
        PopupUtils.clearViewFromParent(getContentView());
        PopupCompatManager.clear(this);
    }


    /**
     * 尝试代理掉windowmanager
     *
     * @param popupWindow
     */
    private void tryToProxyWindowManagerMethod(PopupWindow popupWindow) {
        if (mHelper == null || mWindowManagerProxy != null) return;
        PopupLogUtil.trace("cur api >> " + Build.VERSION.SDK_INT);
        troToProxyWindowManagerMethodBeforeP(popupWindow);
    }


    private void troToProxyWindowManagerMethodOverP(PopupWindow popupWindow) {
        try {
            WindowManager windowManager = PopupReflectionHelper.getInstance().getPopupWindowManager(popupWindow);
            if (windowManager == null) return;
            mWindowManagerProxy = new WindowManagerProxy(windowManager);
            PopupReflectionHelper.getInstance().setPopupWindowManager(popupWindow, mWindowManagerProxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void troToProxyWindowManagerMethodBeforeP(PopupWindow popupWindow) {
        try {
            Field fieldWindowManager = PopupWindow.class.getDeclaredField("mWindowManager");
            fieldWindowManager.setAccessible(true);
            final WindowManager windowManager = (WindowManager) fieldWindowManager.get(popupWindow);
            if (windowManager == null) return;
            mWindowManagerProxy = new WindowManagerProxy(windowManager);
            fieldWindowManager.set(popupWindow, mWindowManagerProxy);
            PopupLogUtil.trace(LogTag.i, TAG, "尝试代理WindowManager成功");
        } catch (NoSuchFieldException e) {
            if (Build.VERSION.SDK_INT >= 27) {
                troToProxyWindowManagerMethodOverP(popupWindow);
            } else {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void update() {
        try {
            if (mHelper != null) {
                if (mHelper.isInterceptTouchEvent()) {
                    if (mWindowManagerProxy != null) {
                        mWindowManagerProxy.update();
                    }
                } else {
                    super.update(mHelper.getAnchorX(), mHelper.getAnchorY() + mHelper.getAnchorHeight(), mHelper.getPopupViewWidth(), mHelper.getPopupViewHeight(), true);
                }
            } else {
                super.update();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isHandledFullScreen() {
        return isHandledFullScreen;
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

    WindowManagerProxy getWindowManagerProxy() {
        return mWindowManagerProxy;
    }
}
