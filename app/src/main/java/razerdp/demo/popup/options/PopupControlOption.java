package razerdp.demo.popup.options;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.PopupOptionControlBinding;
import razerdp.demo.model.common.CommonControllerInfo;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2020/02/23.
 */
public class PopupControlOption extends BaseOptionPopup<CommonControllerInfo> {
    PopupOptionControlBinding mBinding;

    public PopupControlOption(Context context) {
        super(context);
        setContentView(R.layout.popup_option_control);
        mBinding.tvGo.setOnClickListener(v -> ok());
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        mBinding = PopupOptionControlBinding.bind(contentView);
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

    void ok() {
        mInfo.backpressEnable = mBinding.checkBackpressenable.isChecked();
        mInfo.outSideTouchAble = mBinding.checkSetOutSideTouchable.isChecked();
        mInfo.outSideDissmiss = mBinding.checkSetOutSideDismiss.isChecked();
        mInfo.blurEnable = mBinding.checkBlur.isChecked();
        mInfo.showNewOne = mBinding.checkOpenNewOne.isChecked();
        dismiss();
    }
}
