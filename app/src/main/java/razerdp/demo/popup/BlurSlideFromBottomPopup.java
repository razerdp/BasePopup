package razerdp.demo.popup;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.utils.ToastUtils;

/**
 * Created by 大灯泡 on 2017/12/27.
 * 从底部滑上来同时模糊的popup
 */
public class BlurSlideFromBottomPopup extends BasePopupWindow implements View.OnClickListener {

    private View popupView;

    public BlurSlideFromBottomPopup(Activity context) {
        super(context);
        bindEvent();
    }

    @Override
    protected Animation initShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0f, 300);
    }

    @Override
    protected Animation initExitAnimation() {
        return getTranslateVerticalAnimation(0f, 1f, 300);
    }

    @Override
    public View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    @Override
    public View onCreatePopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_blur_slide_from_bottom, null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView != null) {
            popupView.findViewById(R.id.tx_1).setOnClickListener(this);
            popupView.findViewById(R.id.tx_2).setOnClickListener(this);
            popupView.findViewById(R.id.tx_3).setOnClickListener(this);
        }

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
