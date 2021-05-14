package razerdp.demo.popup.options;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import androidx.appcompat.widget.AppCompatCheckBox;
import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.R;
import razerdp.demo.popup.PopupFriendCircle;
import razerdp.demo.widget.DPTextView;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2019/9/22.
 */
public class PopupCircleOption extends BaseOptionPopup {

    @BindView(R.id.check_outsidetouch)
    AppCompatCheckBox checkOutsidetouch;
    @BindView(R.id.check_blur)
    AppCompatCheckBox checkBlur;
    @BindView(R.id.check_link)
    AppCompatCheckBox checkLink;
    @BindView(R.id.tv_go)
    DPTextView tvGo;

    public PopupCircleOption(Context context) {
        super(context);
        setContentView(R.layout.popup_option_circle);
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

    @OnClick(R.id.tv_go)
    void ok() {
        PopupFriendCircle.blur = checkBlur.isChecked();
        PopupFriendCircle.outSideTouch = checkOutsidetouch.isChecked();
        PopupFriendCircle.link = checkLink.isChecked();
        dismiss();
    }
}
