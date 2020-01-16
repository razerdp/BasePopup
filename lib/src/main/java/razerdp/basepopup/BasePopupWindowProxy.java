package razerdp.basepopup;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.lang.reflect.Field;

import razerdp.util.PopupUtils;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2017/11/27.
 * <p>
 * BasePopup代理
 */

abstract class BasePopupWindowProxy extends PopupWindow {
    private static final String TAG = "BasePopupWindowProxy";

    private BasePopupHelper mHelper;
    private WindowManagerProxy mWindowManagerProxy;
    private boolean oldFocusable = true;
    private boolean isHandledFullScreen;

    BasePopupWindowProxy(View contentView, int width, int height, BasePopupHelper helper) {
        super(contentView, width, height);
        this.mHelper = helper;
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
        return PopupUtils.getActivity(from);
    }


    @Override
    public void dismiss() {
        mHelper.dismiss(true);
    }

    void originalDismiss() {
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

    private void tryToHookWindowManager() {
        if (mWindowManagerProxy != null) return;
        try {
            Field fieldWindowManager = PopupWindow.class.getDeclaredField("mWindowManager");
            fieldWindowManager.setAccessible(true);
            final WindowManager windowManager = (WindowManager) fieldWindowManager.get(this);
            if (windowManager == null) return;
            mWindowManagerProxy = new WindowManagerProxy(windowManager);
            mWindowManagerProxy.attachPopupHelper(mHelper);
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
                    Rect anchorBound = mHelper.getAnchorViewBond();
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
