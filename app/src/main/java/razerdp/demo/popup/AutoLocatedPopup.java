package razerdp.demo.popup;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.utils.ToastUtils;

/**
 * Created by 大灯泡 on 2016/11/23.
 * <p>
 * 自动定位的popup，空间不足显示在上面
 */
public class AutoLocatedPopup extends BasePopupWindow implements View.OnClickListener {

    private View popupView;

    public AutoLocatedPopup(Activity context) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setAutoLocatePopup(true);
        bindEvent();
    }


    @Override
    protected Animation initShowAnimation() {
        return getDefaultAlphaAnimation();
    }


    @Override
    public View getClickToDismissView() {
        return null;
    }

    @Override
    public View onCreatePopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_menu, null);
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
