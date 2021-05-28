package razerdp.demo.popup.options;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import androidx.appcompat.widget.AppCompatCheckBox;
import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.R;
import razerdp.demo.model.common.CommonControllerInfo;
import razerdp.demo.widget.DPTextView;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2020/02/23.
 */
public class PopupControlOption extends BaseOptionPopup<CommonControllerInfo> {
    @BindView(R.id.check_backpressenable)
    AppCompatCheckBox mCheckBackpressenable;
    @BindView(R.id.check_setOutSideTouchable)
    AppCompatCheckBox mCheckSetOutSideTouchable;
    @BindView(R.id.check_setOutSideDismiss)
    AppCompatCheckBox mCheckSetOutSideDismiss;
    @BindView(R.id.check_blur)
    AppCompatCheckBox mCheckBlur;
    @BindView(R.id.check_open_new_one)
    AppCompatCheckBox mCheckOpenNewOne;

    @BindView(R.id.tv_go)
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

    @OnClick(R.id.tv_go)
    void ok() {
        mInfo.backpressEnable = mCheckBackpressenable.isChecked();
        mInfo.outSideTouchAble = mCheckSetOutSideTouchable.isChecked();
        mInfo.outSideDissmiss = mCheckSetOutSideDismiss.isChecked();
        mInfo.blurEnable = mCheckBlur.isChecked();
        mInfo.showNewOne = mCheckOpenNewOne.isChecked();
        dismiss();
    }
}
