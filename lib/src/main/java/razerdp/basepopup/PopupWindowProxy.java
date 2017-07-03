package razerdp.basepopup;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
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
    private final boolean isFixAndroidN = Build.VERSION.SDK_INT == 24;
    private final boolean isOverAndroidN = Build.VERSION.SDK_INT > 24;


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
            Activity activity = (Activity) anchor.getContext();

            xoff = anchorLocation[0] + xoff;
            yoff = anchorLocation[1] + anchor.getHeight() + yoff ;

            super.showAtLocation((activity).getWindow().getDecorView(), Gravity.NO_GRAVITY, xoff, yoff);
        } else {
            if (isOverAndroidN) {
                setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            super.showAsDropDown(anchor, xoff, yoff, gravity);
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
