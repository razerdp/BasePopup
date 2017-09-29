package razerdp.basepopup;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * Created by 大灯泡 on 2017/1/12.
 * <p>
 * 与basePopupWindow强引用(或者说与PopupController强引用)
 */

public class PopupWindowProxy extends PopupWindow {
    private static final String TAG = "PopupWindowProxy";
    private final boolean isFixAndroidN = Build.VERSION.SDK_INT == 24;
    private final boolean isOverAndroidN = Build.VERSION.SDK_INT > 24;
    private static final int MAX_SCAN_ACTIVITY_COUNT = 50;
    private volatile int tryScanActivityCount = 0;


    private PopupController mController;

    public PopupWindowProxy(Context context, PopupController mController) {
        super(context);
        this.mController = mController;
    }

    public PopupWindowProxy(Context context, AttributeSet attrs, PopupController mController) {
        super(context, attrs);
        this.mController = mController;
    }

    public PopupWindowProxy(Context context, AttributeSet attrs, int defStyleAttr, PopupController mController) {
        super(context, attrs, defStyleAttr);
        this.mController = mController;
    }

    public PopupWindowProxy(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, PopupController mController) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mController = mController;
    }

    public PopupWindowProxy(PopupController mController) {
        this.mController = mController;
    }

    public PopupWindowProxy(View contentView, PopupController mController) {
        super(contentView);
        this.mController = mController;
    }

    public PopupWindowProxy(int width, int height, PopupController mController) {
        super(width, height);
        this.mController = mController;
    }

    public PopupWindowProxy(View contentView, int width, int height, PopupController mController) {
        super(contentView, width, height);
        this.mController = mController;
    }

    public PopupWindowProxy(View contentView, int width, int height, boolean focusable, PopupController mController) {
        super(contentView, width, height, focusable);
        this.mController = mController;
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
        if (isFixAndroidN && anchor != null) {
            int[] anchorLocation = new int[2];
            anchor.getLocationInWindow(anchorLocation);
            Activity activity = scanForActivity(anchor.getContext());
            if (activity == null) {
                Log.e(TAG, "please make sure that context is instance of activity");
                return;
            }

            xoff = anchorLocation[0] + xoff;
            yoff = anchorLocation[1] + anchor.getHeight() + yoff;
            setFocusable(false);
            super.showAtLocation((activity).getWindow().getDecorView(), Gravity.NO_GRAVITY, xoff, yoff);
            initSystemBar(getContentView());
            setFocusable(true);
            update();
        } else {
            if (isOverAndroidN) {
                setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            setFocusable(false);
            super.showAsDropDown(anchor, xoff, yoff, gravity);
            initSystemBar(getContentView());
            setFocusable(true);
            update();
        }
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        setFocusable(false);
        super.showAtLocation(parent, gravity, x, y);
        initSystemBar(getContentView());
        setFocusable(true);
        update();
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
    private Activity scanForActivity(Context cont) {
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

    /**
     * 修复popup显示时退出沉浸的问题
     *
     * @param view
     */
    void initSystemBar(View view) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN;
                boolean isFullScreen = view.getSystemUiVisibility() == uiOptions;
                if (isFullScreen) {
                    hideSystemUI(view);
                } else {
                    //showSystemUI(view);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideSystemUI(View view) {
        if (view == null) return;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                view.setSystemUiVisibility(uiOptions);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSystemUI(View view) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                view.setSystemUiVisibility(uiOptions);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
