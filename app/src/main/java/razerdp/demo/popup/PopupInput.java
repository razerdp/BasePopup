package razerdp.demo.popup;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.PopupInputBinding;
import razerdp.demo.model.common.CommonInputInfo;
import razerdp.demo.utils.UIHelper;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2019/9/22.
 *
 * @see CommonInputInfo
 */
public class PopupInput extends BasePopupWindow {
    PopupInputBinding mBinding;

    public PopupInput(Context context) {
        super(context);
        setContentView(R.layout.popup_input);
        mBinding.tvSend.setOnClickListener(v -> {
            UIHelper.toast(mBinding.edInput.getText().toString());
            dismiss();
        });
    }

    @Override
    public void onViewCreated(View contentView) {
        mBinding = PopupInputBinding.bind(contentView);
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
