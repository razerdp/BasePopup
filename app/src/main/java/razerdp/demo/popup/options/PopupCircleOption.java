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
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_option_circle);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0f, 450);
    }


    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0f, 1f, 450);
    }

    @OnClick(R.id.tv_go)
    void ok() {
        PopupFriendCircle.blur = checkBlur.isChecked();
        PopupFriendCircle.outSideTouch = checkOutsidetouch.isChecked();
        PopupFriendCircle.link = checkLink.isChecked();
        dismiss();
    }
}
