package razerdp.demo.popup.options;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.PopupOptionCircleBinding;
import razerdp.demo.popup.PopupFriendCircle;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2019/9/22.
 */
public class PopupCircleOption extends BaseOptionPopup {
    PopupOptionCircleBinding mBinding;

    public PopupCircleOption(Context context) {
        super(context);
        setContentView(R.layout.popup_option_circle);
        mBinding.tvGo.setOnClickListener(v -> ok());
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        mBinding = PopupOptionCircleBinding.bind(contentView);
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
        PopupFriendCircle.blur = mBinding.checkBlur.isChecked();
        PopupFriendCircle.outSideTouch = mBinding.checkOutsidetouch.isChecked();
        PopupFriendCircle.link = mBinding.checkLink.isChecked();
        dismiss();
    }
}
