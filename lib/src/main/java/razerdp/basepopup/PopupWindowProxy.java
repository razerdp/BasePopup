package razerdp.basepopup;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import razerdp.library.R;
import razerdp.util.PopupUtils;

/**
 * Created by 大灯泡 on 2017/1/12.
 * <p>
 * 与basePopupWindow强引用(或者说与PopupController强引用)
 */

class PopupWindowProxy extends PopupWindow implements ClearMemoryObject {
    private static final String TAG = "PopupWindowProxy";
    BasePopupContextWrapper mBasePopupContextWrapper;

    private boolean oldFocusable = true;
    private boolean isHandledFullScreen;

    private static final int FULL_SCREEN_FLAG = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_FULLSCREEN;

    public PopupWindowProxy(BasePopupContextWrapper context) {
        super(context);
        mBasePopupContextWrapper = context;
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable());
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
    }


    @Override
    public void update() {
        try {
            mBasePopupContextWrapper.mWindowManagerProxy.update();
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
        updateFocusable(oldFocusable);
        setFocusable(oldFocusable);
        isHandledFullScreen = false;
    }

    void updateFocusable(boolean focusable) {
        if (mBasePopupContextWrapper != null && mBasePopupContextWrapper.mWindowManagerProxy != null) {
            mBasePopupContextWrapper.mWindowManagerProxy.updateFocus(focusable);
        }
    }

    void updateFlag(int mode, boolean updateImmediately, int... flags) {
        if (mBasePopupContextWrapper != null && mBasePopupContextWrapper.mWindowManagerProxy != null) {
            mBasePopupContextWrapper.mWindowManagerProxy.updateFlag(mode, updateImmediately, flags);
        }
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (isShowing()) return;
        Activity activity = PopupUtils.getActivity(parent.getContext(), false);
        if (activity == null) {
            Log.e(TAG, PopupUtils.getString(R.string.basepopup_error_non_act_context));
            return;
        }
        onBeforeShowExec(activity);
        super.showAtLocation(parent, gravity, x, y);
        onAfterShowExec(activity);
    }

    void onBeforeShowExec(Activity act) {
        if (isFullScreen(act)) {
            handleFullScreenFocusable();
        }
    }

    void onAfterShowExec(Activity act) {
        if (isHandledFullScreen) {
            int flag = FULL_SCREEN_FLAG;
            if (act != null) {
                flag = act.getWindow().getDecorView().getSystemUiVisibility();
            }
            getContentView().setSystemUiVisibility(flag);
            restoreFocusable();
        }
    }

    boolean isFullScreen(Activity act) {
        if (act == null) return false;
        try {
            View decorView = act.getWindow().getDecorView();
            WindowManager.LayoutParams lp = act.getWindow().getAttributes();
            int i = lp.flags;
            int f = decorView.getWindowSystemUiVisibility();

            return (i & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0
                    || ((f & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) != 0 || (f & View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION) != 0);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void dismiss() {
        if (mBasePopupContextWrapper != null && mBasePopupContextWrapper.helper != null) {
            mBasePopupContextWrapper.helper.dismiss(true);
        }
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

    WindowManagerProxy prevWindow() {
        if (mBasePopupContextWrapper == null || mBasePopupContextWrapper.mWindowManagerProxy == null) {
            return null;
        }
        return mBasePopupContextWrapper.mWindowManagerProxy.preWindow();
    }

    @Override
    public void clear(boolean destroy) {
        if (mBasePopupContextWrapper != null) {
            mBasePopupContextWrapper.clear(destroy);
        }
        PopupUtils.clearViewFromParent(getContentView());
        if (destroy) {
            mBasePopupContextWrapper = null;
        }
    }

    /**
     * 采取ContextWrapper来hook WindowManager，从此再也无需反射及各种黑科技了~
     * 感谢
     *
     * @xchengDroid https://github.com/xchengDroid  提供的方案
     */
    static class BasePopupContextWrapper extends ContextWrapper implements ClearMemoryObject {
        BasePopupHelper helper;
        WindowManagerProxy mWindowManagerProxy;

        public BasePopupContextWrapper(Context base, BasePopupHelper helper) {
            super(base);
            this.helper = helper;
        }

        @Override
        public Object getSystemService(String name) {
            if (TextUtils.equals(name, Context.WINDOW_SERVICE)) {
                if (mWindowManagerProxy != null) {
                    return mWindowManagerProxy;
                }
                WindowManager windowManager = (WindowManager) super.getSystemService(name);
                mWindowManagerProxy = new WindowManagerProxy(windowManager, helper);
                return mWindowManagerProxy;
            }
            return super.getSystemService(name);
        }

        @Override
        public void clear(boolean destroy) {
            if (mWindowManagerProxy != null) {
                mWindowManagerProxy.clear(destroy);
            }
            if (destroy) {
                helper = null;
                mWindowManagerProxy = null;
            }
        }
    }
}
