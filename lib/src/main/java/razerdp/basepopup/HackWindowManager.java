package razerdp.basepopup;

import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

/**
 * Created by 大灯泡 on 2017/12/25.
 * <p>
 * 代理掉popup的windowmanager，在addView操作，拦截decorView的操作
 */
final class HackWindowManager implements WindowManager {
    private static final String TAG = "HackWindowManager";
    private WeakReference<WindowManager> mWindowManager;
    private WeakReference<PopupController> mPopupController;
    private WeakReference<HackPopupDecorView> mHackPopupDecorView;

    public HackWindowManager(WindowManager windowManager, PopupController popupController) {
        mWindowManager = new WeakReference<WindowManager>(windowManager);
        mPopupController = new WeakReference<PopupController>(popupController);
    }

    @Override
    public Display getDefaultDisplay() {
        return getWindowManager() == null ? null : getWindowManager().getDefaultDisplay();
    }

    @Override
    public void removeViewImmediate(View view) {
        if (getWindowManager() == null) return;
        if (checkProxyValided(view) && getHackPopupDecorView() != null) {
            HackPopupDecorView hackPopupDecorView = getHackPopupDecorView();
            getWindowManager().removeViewImmediate(hackPopupDecorView);
            hackPopupDecorView.setPopupController(null);
            mHackPopupDecorView.clear();
            mHackPopupDecorView = null;
        } else {
            getWindowManager().removeViewImmediate(view);
        }
    }

    @Override
    public void addView(View view, ViewGroup.LayoutParams params) {
        Log.i(TAG, "addView:  " + view.getClass().getSimpleName());
        if (getWindowManager() == null) return;
        if (checkProxyValided(view)) {
            final HackPopupDecorView hackPopupDecorView = new HackPopupDecorView(view.getContext());
            mHackPopupDecorView = new WeakReference<HackPopupDecorView>(hackPopupDecorView);
            hackPopupDecorView.setPopupController(getPopupController());
            hackPopupDecorView.addView(view, params);
            getWindowManager().addView(hackPopupDecorView, params);
        } else {
            getWindowManager().addView(view, params);
        }
    }

    @Override
    public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
        if (getWindowManager() == null) return;
        if (checkProxyValided(view) && getHackPopupDecorView() != null) {
            HackPopupDecorView hackPopupDecorView = getHackPopupDecorView();
            getWindowManager().updateViewLayout(hackPopupDecorView, params);
        } else {
            getWindowManager().updateViewLayout(view, params);
        }
    }

    @Override
    public void removeView(View view) {
        if (getWindowManager() == null) return;
        if (checkProxyValided(view) && getHackPopupDecorView() != null) {
            HackPopupDecorView hackPopupDecorView = getHackPopupDecorView();
            getWindowManager().removeView(hackPopupDecorView);
            hackPopupDecorView.setPopupController(null);
            mHackPopupDecorView.clear();
            mHackPopupDecorView = null;
        } else {
            getWindowManager().removeView(view);
        }
    }


    private boolean checkProxyValided(View v) {
        if (v == null) return false;
        String viewSimpleClassName = v.getClass().getSimpleName();
        return TextUtils.equals(viewSimpleClassName, "PopupDecorView") || TextUtils.equals(viewSimpleClassName, "PopupViewContainer");
    }

    private HackPopupDecorView getHackPopupDecorView() {
        if (mHackPopupDecorView == null) return null;
        return mHackPopupDecorView.get();
    }

    private WindowManager getWindowManager() {
        if (mWindowManager == null) return null;
        return mWindowManager.get();
    }

    private PopupController getPopupController() {
        if (mPopupController == null) return null;
        return mPopupController.get();
    }
}
