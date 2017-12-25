package razerdp.basepopup;

import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by 大灯泡 on 2017/12/25.
 * <p>
 * 代理掉popup的windowmanager，在addView操作，拦截decorView的操作
 */
final class HackWindowManager implements WindowManager {
    private static final String TAG = "HackWindowManager";
    private WindowManager mWindowManager;
    private PopupController mPopupController;
    HackPopupDecorView mHackPopupDecorView;

    public HackWindowManager(WindowManager windowManager, PopupController popupController) {
        mWindowManager = windowManager;
        mPopupController = popupController;
    }

    @Override
    public Display getDefaultDisplay() {
        return mWindowManager.getDefaultDisplay();
    }

    @Override
    public void removeViewImmediate(View view) {
        if (checkProxyValided(view) && mHackPopupDecorView != null) {
            mWindowManager.removeViewImmediate(mHackPopupDecorView);
            mHackPopupDecorView.setPopupController(null);
            mHackPopupDecorView = null;
        } else {
            mWindowManager.removeViewImmediate(view);
        }
    }

    @Override
    public void addView(View view, ViewGroup.LayoutParams params) {
        Log.i(TAG, "addView:  " + view.getClass().getSimpleName());

        if (checkProxyValided(view)) {
            mHackPopupDecorView = new HackPopupDecorView(view.getContext());
            mHackPopupDecorView.setPopupController(mPopupController);
            mHackPopupDecorView.addView(view);
            mWindowManager.addView(mHackPopupDecorView, params);
        } else {
            mWindowManager.addView(view, params);
        }
    }

    @Override
    public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
        if (checkProxyValided(view) && mHackPopupDecorView != null) {
            mWindowManager.updateViewLayout(mHackPopupDecorView, params);
        } else {
            mWindowManager.updateViewLayout(view, params);
        }

    }

    @Override
    public void removeView(View view) {
        if (checkProxyValided(view) && mHackPopupDecorView != null) {
            mWindowManager.removeView(mHackPopupDecorView);
            mHackPopupDecorView.setPopupController(null);
            mHackPopupDecorView = null;
        } else {
            mWindowManager.removeView(view);
        }
    }


    private boolean checkProxyValided(View v) {
        if (v == null) return false;
        String viewSimpleClassName = v.getClass().getSimpleName();
        return TextUtils.equals(viewSimpleClassName, "PopupDecorView") || TextUtils.equals(viewSimpleClassName, "PopupViewContainer");
    }
}
