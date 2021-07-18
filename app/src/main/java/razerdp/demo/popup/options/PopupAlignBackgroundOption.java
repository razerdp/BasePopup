package razerdp.demo.popup.options;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import androidx.appcompat.widget.AppCompatCheckBox;
import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.R;
import razerdp.demo.model.common.CommonBackgroundAlignInfo;
import razerdp.demo.widget.DPTextView;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2019/9/21.
 */
public class PopupAlignBackgroundOption extends BaseOptionPopup<CommonBackgroundAlignInfo> {

    @BindView(R.id.check_left)
    AppCompatCheckBox mCheckLeft;
    @BindView(R.id.check_top)
    AppCompatCheckBox mCheckTop;
    @BindView(R.id.check_right)
    AppCompatCheckBox mCheckRight;
    @BindView(R.id.check_bottom)
    AppCompatCheckBox mCheckBottom;
    @BindView(R.id.check_alignbackground)
    AppCompatCheckBox mCheckAlignbackground;
    @BindView(R.id.check_blur)
    AppCompatCheckBox mCheckBlur;
    @BindView(R.id.tv_go)
    DPTextView mTvGo;


    public PopupAlignBackgroundOption(Context context) {
        super(context);
        setContentView(R.layout.popup_option_background_align);
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
        int gravity = Gravity.NO_GRAVITY;
        if (mCheckLeft.isChecked()) gravity |= Gravity.LEFT;
        if (mCheckTop.isChecked()) gravity |= Gravity.TOP;
        if (mCheckRight.isChecked()) gravity |= Gravity.RIGHT;
        if (mCheckBottom.isChecked()) gravity |= Gravity.BOTTOM;
        mInfo.alignGravity = gravity;
        mInfo.align = mCheckAlignbackground.isChecked();
        mInfo.blur = mCheckBlur.isChecked();
        dismiss();
    }
}
