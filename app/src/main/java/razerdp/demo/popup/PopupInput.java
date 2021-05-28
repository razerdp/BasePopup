package razerdp.demo.popup;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;

import butterknife.BindView;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.model.common.CommonInputInfo;
import razerdp.demo.utils.ButterKnifeUtil;
import razerdp.demo.utils.UIHelper;
import razerdp.demo.widget.DPTextView;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2019/9/22.
 *
 * @see CommonInputInfo
 */
public class PopupInput extends BasePopupWindow {
    @BindView(R.id.tv_send)
    DPTextView mTvSend;
    @BindView(R.id.ed_input)
    EditText mEdInput;

    public PopupInput(Context context) {
        super(context);
        setContentView(R.layout.popup_input);
        mTvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.toast(mEdInput.getText().toString());
                dismiss();
            }
        });
    }

    @Override
    public void onViewCreated(View contentView) {
        ButterKnifeUtil.bind(this, contentView);
    }


    @Override
    protected Animation onCreateShowAnimation() {
        return AnimationHelper.asAnimation()
                .withTranslation(TranslationConfig.FROM_BOTTOM)
                .toShow();
    }


    @Override
    protected Animation onCreateDismissAnimation() {
        return AnimationHelper.asAnimation()
                .withTranslation(TranslationConfig.TO_BOTTOM)
                .toDismiss();
    }
}
