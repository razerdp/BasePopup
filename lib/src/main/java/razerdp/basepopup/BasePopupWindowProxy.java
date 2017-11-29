package razerdp.basepopup;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;

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

    public BasePopupWindowProxy(Context context, PopupController mController) {
        super(context);
        this.mController = mController;
    }

    public BasePopupWindowProxy(Context context, AttributeSet attrs, PopupController mController) {
        super(context, attrs);
        this.mController = mController;
    }

    public BasePopupWindowProxy(Context context, AttributeSet attrs, int defStyleAttr, PopupController mController) {
        super(context, attrs, defStyleAttr);
        this.mController = mController;
    }

    public BasePopupWindowProxy(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, PopupController mController) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mController = mController;
    }


    public BasePopupWindowProxy(View contentView, PopupController mController) {
        super(contentView);
        this.mController = mController;
    }

    public BasePopupWindowProxy(int width, int height, PopupController mController) {
        super(width, height);
        this.mController = mController;
    }

    public BasePopupWindowProxy(View contentView, int width, int height, PopupController mController) {
        super(contentView, width, height);
        this.mController = mController;
    }

    public BasePopupWindowProxy(View contentView, int width, int height, boolean focusable, PopupController mController) {
        super(contentView, width, height, focusable);
        this.mController = mController;
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
        super.dismiss();
    }
}
