package razerdp.basepopup.proxy;

import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import razerdp.basepopup.HackPopupDecorView;

/**
 * Created by 大灯泡 on 2017/12/25.
 * <p>
 * 代理掉popup的windowmanager，在addView操作，拦截decorView的操作
 */
public class HackWindowManager implements WindowManager {
    private static final String TAG = "HackWindowManager";
    private WindowManager mWindowManager;

    HackPopupDecorView mHackPopupDecorView;

    public HackWindowManager(WindowManager windowManager) {
        mWindowManager = windowManager;
    }

    @Override
    public Display getDefaultDisplay() {
        return mWindowManager.getDefaultDisplay();
    }

    @Override
    public void removeViewImmediate(View view) {
        if (checkNeedProxy(view) && mHackPopupDecorView != null) {
            mWindowManager.removeViewImmediate(mHackPopupDecorView);
            mHackPopupDecorView = null;
        } else {
            mWindowManager.removeViewImmediate(view);
        }
    }

    @Override
    public void addView(View view, ViewGroup.LayoutParams params) {
        Log.i(TAG, "addView:  " + view.getClass().getSimpleName());

        if (checkNeedProxy(view)) {
            mHackPopupDecorView = new HackPopupDecorView(view.getContext());
            mHackPopupDecorView.addView(view);
            mWindowManager.addView(mHackPopupDecorView, params);
        } else {
            mWindowManager.addView(view, params);
        }
    }

    @Override
    public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
        if (checkNeedProxy(view) && mHackPopupDecorView != null) {
            mWindowManager.updateViewLayout(mHackPopupDecorView, params);
        } else {
            mWindowManager.updateViewLayout(view, params);
        }

    }

    @Override
    public void removeView(View view) {
        if (checkNeedProxy(view) && mHackPopupDecorView != null) {
            mWindowManager.removeView(mHackPopupDecorView);
        } else {
            mWindowManager.removeView(view);
        }
    }


    private boolean checkNeedProxy(View v) {
        if (v == null) return false;
        String viewSimpleClassName = v.getClass().getSimpleName();
        return TextUtils.equals(viewSimpleClassName, "PopupDecorView") || TextUtils.equals(viewSimpleClassName, "PopupViewContainer");

    }
}
