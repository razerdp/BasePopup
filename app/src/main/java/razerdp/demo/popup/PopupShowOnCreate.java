package razerdp.demo.popup;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.ScaleConfig;

/**
 * Created by 大灯泡 on 2020/2/3.
 */
public class PopupShowOnCreate extends BasePopupWindow {
    private onErrorPrintListener mOnErrorPrintListener;

    public PopupShowOnCreate(Context context) {
        super(context);
        setContentView(R.layout.popup_show_on_create);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return AnimationHelper.asAnimation()
                .withScale(ScaleConfig.CENTER)
                .toShow();
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return AnimationHelper.asAnimation()
                .withScale(ScaleConfig.CENTER)
                .toDismiss();
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
