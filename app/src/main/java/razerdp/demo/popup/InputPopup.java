package razerdp.demo.popup;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.utils.ToastUtils;

/**
 * Created by 大灯泡 on 2016/1/18.
 * 带输入法的popup
 */
public class InputPopup extends BasePopupWindow implements View.OnClickListener {
    private Button mCancelButton;
    private Button mCompeleteButton;
    private EditText mInputEdittext;

    public InputPopup(Context context) {
        super(context);
        mCancelButton = (Button) findViewById(R.id.btn_cancel);
        mCompeleteButton = (Button) findViewById(R.id.btn_Compelete);
        mInputEdittext = (EditText) findViewById(R.id.ed_input);

        setClipChildren(false);
        setAutoShowInputMethod(mInputEdittext, true);
        setPopupGravity(Gravity.CENTER);
        bindEvent();
    }

    public EditText getInputEdittext() {
        return mInputEdittext;
    }

    private void bindEvent() {
        mCancelButton.setOnClickListener(this);
        mCompeleteButton.setOnClickListener(this);
    }

    //=============================================================super methods


    @Override
    public Animator onCreateShowAnimator() {
        return getDefaultSlideFromBottomAnimationSet();
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_input);
    }

    @Override
    public Animator onCreateDismissAnimator() {
        AnimatorSet set = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            set = new AnimatorSet();
            if (getDisplayAnimateView() != null) {
                set.playTogether(
                        ObjectAnimator.ofFloat(getDisplayAnimateView(), "translationY", 0, 250).setDuration(400),
                        ObjectAnimator.ofFloat(getDisplayAnimateView(), "alpha", 1, 0.4f).setDuration(250 * 3 / 2));
            }
        }
        return set;
    }

    //=============================================================click event
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_Compelete:
                ToastUtils.ToastMessage(getContext(), mInputEdittext.getText().toString());
                dismiss();
                break;
            default:
                break;
        }

    }
}
