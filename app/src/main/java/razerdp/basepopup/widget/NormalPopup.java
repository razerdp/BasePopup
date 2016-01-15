package razerdp.basepopup.widget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import razerdp.basepopup.R;
import razerdp.basepopup.basepopup.BasePopupWindow;
import razerdp.basepopup.utils.ToastUtils;

/**
 * Created by 大灯泡 on 2016/1/15.
 * 普通的popup
 */
public class NormalPopup extends BasePopupWindow implements View.OnClickListener{

    private View popupView;

    public NormalPopup(Activity context) {
        super(context);
        bindEvent();
    }



    @Override
    public Animation getAnimation() {
        return getDefaultScaleAnimation();
    }

    @Override
    public AnimationSet getAnimationSet() {
        return null;
    }

    @Override
    public View getInputView() {
        return null;
    }

    @Override
    public View getPopupView() {
        popupView= LayoutInflater.from(mContext).inflate(R.layout.popup_normal,null);
        return popupView;
    }

    @Override
    public View getAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView!=null){
            popupView.findViewById(R.id.tx_1).setOnClickListener(this);
            popupView.findViewById(R.id.tx_2).setOnClickListener(this);
            popupView.findViewById(R.id.tx_3).setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tx_1:
                ToastUtils.ToastMessage(mContext,"click tx_1");
                break;
            case R.id.tx_2:
                ToastUtils.ToastMessage(mContext,"click tx_2");
                break;
            case R.id.tx_3:
                ToastUtils.ToastMessage(mContext,"click tx_3");
                break;
            default:
                break;
        }

    }
}
