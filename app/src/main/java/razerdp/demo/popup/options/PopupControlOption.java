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
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_option_control);
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
        mInfo.backpressEnable = mCheckBackpressenable.isChecked();
        mInfo.outSideTouchAble = mCheckSetOutSideTouchable.isChecked();
        mInfo.outSideDissmiss = mCheckSetOutSideDismiss.isChecked();
        mInfo.blurEnable = mCheckBlur.isChecked();
        mInfo.showNewOne = mCheckOpenNewOne.isChecked();
        dismiss();
    }
}
