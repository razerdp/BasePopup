package razerdp.basepopup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

/**
 * Created by 大灯泡 on 2017/12/25.
 * <p>
 * 旨在用来拦截keyevent
 */
public class HackPopupDecorView extends FrameLayout {
    private static final String TAG = "HackPopupDecorView";
    private WeakReference<PopupController> mPopupController;

    public HackPopupDecorView(Context context) {
        super(context);
    }

    public HackPopupDecorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HackPopupDecorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.i(TAG, "dispatchKeyEvent: ");
        boolean intercept = getPopupController() != null && getPopupController().onDispatchKeyEvent(event);
        if (intercept) return true;
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (getKeyDispatcherState() == null) {
                return super.dispatchKeyEvent(event);
            }

            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                final KeyEvent.DispatcherState state = getKeyDispatcherState();
                if (state != null) {
                    state.startTracking(event, this);
                }
                return true;
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                final KeyEvent.DispatcherState state = getKeyDispatcherState();
                if (state != null && state.isTracking(event) && !event.isCanceled()) {
                    if (getPopupController() != null) {
                        return getPopupController().onBackPressed();
                    }
                }
            }
            return super.dispatchKeyEvent(event);
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    public PopupController getPopupController() {
        if (mPopupController == null) return null;
        return mPopupController.get();
    }

    public void setPopupController(PopupController popupController) {
        mPopupController = new WeakReference<PopupController>(popupController);
    }
}
