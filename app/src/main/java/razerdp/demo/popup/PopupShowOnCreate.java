package razerdp.demo.popup;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;

/**
 * Created by 大灯泡 on 2020/2/3.
 */
public class PopupShowOnCreate extends BasePopupWindow {
    private onErrorPrintListener mOnErrorPrintListener;

    public PopupShowOnCreate(Context context) {
        super(context);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultScaleAnimation(true);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultScaleAnimation(false);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_show_on_create);
    }


    @Override
    protected void onLogInternal(String msg) {
        super.onLogInternal(msg);
        if (mOnErrorPrintListener != null) {
            mOnErrorPrintListener.onPrintError(msg);
        }
    }

    public PopupShowOnCreate setOnErrorPrintListener(onErrorPrintListener onErrorPrintListener) {
        mOnErrorPrintListener = onErrorPrintListener;
        return this;
    }

    public interface onErrorPrintListener {
        void onPrintError(String msg);
    }
}
