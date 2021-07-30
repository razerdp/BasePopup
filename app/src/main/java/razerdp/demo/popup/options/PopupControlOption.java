package razerdp.demo.popup.options;

import android.content.Context;
import android.view.animation.Animation;

import androidx.appcompat.widget.AppCompatCheckBox;
import razerdp.basepopup.R;
import razerdp.demo.model.common.CommonControllerInfo;
import razerdp.demo.widget.DPTextView;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2020/02/23.
 */
public class PopupControlOption extends BaseOptionPopup<CommonControllerInfo> {
    AppCompatCheckBox mCheckBackpressenable;
    AppCompatCheckBox mCheckSetOutSideTouchable;
    AppCompatCheckBox mCheckSetOutSideDismiss;
    AppCompatCheckBox mCheckBlur;
    AppCompatCheckBox mCheckOpenNewOne;

    DPTextView mTvGo;

    public PopupControlOption(Context context) {
        super(context);
        setContentView(R.layout.popup_option_control);
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
        mInfo.backpressEnable = mCheckBackpressenable.isChecked();
        mInfo.outSideTouchAble = mCheckSetOutSideTouchable.isChecked();
        mInfo.outSideDissmiss = mCheckSetOutSideDismiss.isChecked();
        mInfo.blurEnable = mCheckBlur.isChecked();
        mInfo.showNewOne = mCheckOpenNewOne.isChecked();
        dismiss();
    }
}
