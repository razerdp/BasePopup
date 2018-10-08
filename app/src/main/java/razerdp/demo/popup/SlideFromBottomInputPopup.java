package razerdp.demo.popup;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.utils.ToastUtils;

/**
 * Created by 大灯泡 on 2018/10/8.
 */
public class SlideFromBottomInputPopup extends BasePopupWindow {
    EditText mEditText;

    public SlideFromBottomInputPopup(Context context) {
        super(context);
        mEditText = findViewById(R.id.ed_input);
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.ToastMessage(getContext(), mEditText.getText().toString());
                dismiss();
            }
        });
        setAutoShowInputMethod(mEditText, true);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 500);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 500);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_slide_from_bottom_with_input);
    }
}
