package razerdp.demo.popup;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.utils.ToastUtils;

/**
 * Created by 大灯泡 on 2016/1/15.
 * 各种插值器的popup
 */
public class CustomInterpolatorPopup extends BasePopupWindow implements View.OnClickListener {

    private View popupView;
    private Animation mAnimation;

    public CustomInterpolatorPopup(Context context) {
        super(context);
        bindEvent();
    }


    @Override
    protected Animation onCreateShowAnimation() {
        return mAnimation;
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultAlphaAnimation(false);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_normal);
    }

    private void bindEvent() {
        if (popupView != null) {
            popupView.findViewById(R.id.tx_1).setOnClickListener(this);
            popupView.findViewById(R.id.tx_2).setOnClickListener(this);
            popupView.findViewById(R.id.tx_3).setOnClickListener(this);
        }
    }

    public void setCustomAnimation(Animation anima) {
        setShowAnimation(anima);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tx_1:
                ToastUtils.ToastMessage(getContext(), "click tx_1");
                break;
            case R.id.tx_2:
                ToastUtils.ToastMessage(getContext(), "click tx_2");
                break;
            case R.id.tx_3:
                ToastUtils.ToastMessage(getContext(), "click tx_3");
                break;
            default:
                break;
        }

    }
}
