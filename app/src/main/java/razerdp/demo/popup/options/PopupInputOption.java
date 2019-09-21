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
    @BindView(R.id.tv_go)
    DPTextView mTvGo;

    public PopupInputOption(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_option_input);
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
        mInfo.alignMode = alignMode;
        mInfo.adjust = mCheckAjustInput.isChecked();
        mInfo.autoOpen = mCheckAutoOpen.isChecked();
        dismiss();
    }
}
