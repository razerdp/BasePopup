package razerdp.basepopup.popup;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import razerdp.basepopup.R;
import razerdp.basepopup.base.BasePopupWindow;
import razerdp.basepopup.utils.ToastUtils;

/**
 * Created by 大灯泡 on 2016/1/18.
 * 带输入法的popup
 */
public class InputPopup extends BasePopupWindow implements View.OnClickListener{
    private Button mCancelButton;
    private Button mCompeleteButton;
    private EditText mInputEdittext;

    public InputPopup(Activity context) {
        super(context);
        mCancelButton= (Button) mPopupView.findViewById(R.id.btn_cancel);
        mCompeleteButton= (Button) mPopupView.findViewById(R.id.btn_Compelete);
        mInputEdittext= (EditText) mPopupView.findViewById(R.id.ed_input);

        setAutoShowInputMethod(true);
        bindEvent();
    }

    @Override
    protected Animation getShowAnimation() {
        return null;
    }

    private void bindEvent() {
        mCancelButton.setOnClickListener(this);
        mCompeleteButton.setOnClickListener(this);
    }

    //=============================================================super methods


    @Override
    public Animator getShowAnimator() {
        return getDefaultSlideFromBottomAnimationSet();
    }

    @Override
    public View getInputView() {
        return mInputEdittext;
    }

    @Override
    protected View getClickToDismissView() {
        return mPopupView;
    }

    @Override
    public View getPopupView() {
        return LayoutInflater.from(mContext).inflate(R.layout.popup_input,null);
    }

    @Override
    public View getAnimaView() {
        return mPopupView.findViewById(R.id.popup_anima);
    }

    @Override
    public Animator getExitAnimator() {
        AnimatorSet set = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            set = new AnimatorSet();
            if (getAnimaView() != null) {
                set.playTogether(
                        ObjectAnimator.ofFloat(getAnimaView(), "translationY", 0, 250).setDuration(400),
                        ObjectAnimator.ofFloat(getAnimaView(), "alpha", 1, 0.4f).setDuration(250 * 3 / 2));
            }
        }
        return set;
    }

    //=============================================================click event
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_Compelete:
                ToastUtils.ToastMessage(mContext,mInputEdittext.getText().toString());
                dismiss();
                break;
            default:
                break;
        }

    }
}
