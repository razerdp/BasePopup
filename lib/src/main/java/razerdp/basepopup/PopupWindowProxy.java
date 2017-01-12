package razerdp.basepopup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by 大灯泡 on 2017/1/12.
 */

public class PopupWindowProxy extends PopupWindow {

    public PopupWindowProxy(Context context) {
        super(context);
    }

    public PopupWindowProxy(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PopupWindowProxy(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PopupWindowProxy(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public PopupWindowProxy() {
    }

    public PopupWindowProxy(View contentView) {
        super(contentView);
    }

    public PopupWindowProxy(int width, int height) {
        super(width, height);
    }

    public PopupWindowProxy(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public PopupWindowProxy(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    private OnPopupProxyBeforeDismissListener onPopupProxyBeforeDismissListener;

    public OnPopupProxyBeforeDismissListener getOnPopupProxyBeforeDismissListener() {
        return onPopupProxyBeforeDismissListener;
    }

    public void setOnPopupProxyBeforeDismissListener(OnPopupProxyBeforeDismissListener onPopupProxyBeforeDismissListener) {
        this.onPopupProxyBeforeDismissListener = onPopupProxyBeforeDismissListener;
    }

    @Override
    public void dismiss() {
        boolean dismissAtOnce = true;
        if (onPopupProxyBeforeDismissListener != null) {
            dismissAtOnce = onPopupProxyBeforeDismissListener.onBeforeDismiss();
        }
        if (dismissAtOnce) {
            callSuperDismiss();
        }
    }

    void callSuperDismiss() {
        super.dismiss();
    }


    public interface OnPopupProxyBeforeDismissListener {
        /**
         * dismiss at once
         * <p>
         * true for at once
         *
         * @return
         */
        boolean onBeforeDismiss();
    }
}
