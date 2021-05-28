package razerdp.demo.popup.options;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import androidx.appcompat.widget.AppCompatCheckBox;
import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.model.common.CommonInputInfo;
import razerdp.demo.widget.DPTextView;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2019/9/22.
 */
public class PopupInputOption extends BaseOptionPopup<CommonInputInfo> {
    @BindView(R.id.check_align_to_root)
    AppCompatCheckBox mCheckAlignToRoot;
    @BindView(R.id.check_align_to_view)
    AppCompatCheckBox mCheckAlignToView;
    @BindView(R.id.check_align_animate)
    AppCompatCheckBox mCheckAlignAnimate;
    @BindView(R.id.check_ajust_input)
    AppCompatCheckBox mCheckAjustInput;
    @BindView(R.id.check_auto_open)
    AppCompatCheckBox mCheckAutoOpen;
    @BindView(R.id.check_force)
    AppCompatCheckBox mCheckForce;
    @BindView(R.id.tv_go)
    DPTextView mTvGo;

    public PopupInputOption(Context context) {
        super(context);
        setContentView(R.layout.popup_option_input);
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
        int alignMode = 0;
        if (mCheckAlignAnimate.isChecked()) {
            alignMode |= BasePopupWindow.FLAG_KEYBOARD_ANIMATE_ALIGN;
        }
        if (mCheckAlignToRoot.isChecked()) {
            alignMode |= BasePopupWindow.FLAG_KEYBOARD_ALIGN_TO_ROOT;
        }
        if (mCheckAlignToView.isChecked()) {
            alignMode |= BasePopupWindow.FLAG_KEYBOARD_ALIGN_TO_VIEW;
        }
        if (mCheckForce.isChecked()) {
            alignMode |= BasePopupWindow.FLAG_KEYBOARD_FORCE_ADJUST;
        }
        mInfo.alignMode = alignMode;
        mInfo.adjust = mCheckAjustInput.isChecked();
        mInfo.autoOpen = mCheckAutoOpen.isChecked();
        dismiss();
    }
}
