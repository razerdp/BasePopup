package razerdp.demo.popup.options;

import android.content.Context;
import android.view.animation.Animation;
import android.widget.RadioGroup;

import razerdp.basepopup.R;
import razerdp.demo.model.common.CommonPriorityInfo;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2021/7/1.
 */
public class PopupPriorityOption extends BaseOptionPopup<CommonPriorityInfo> {
    RadioGroup rd1;
    RadioGroup rd2;

    public PopupPriorityOption(Context context) {
        super(context);
        setContentView(R.layout.popup_option_priority);
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
        switch (rd1.getCheckedRadioButtonId()) {
            case R.id.rd_low_1:
                mInfo.priority1 = Priority.LOW;
                break;
            case R.id.rd_normal_1:
                mInfo.priority1 = Priority.NORMAL;
                break;
            case R.id.rd_high_1:
                mInfo.priority1 = Priority.HIGH;
                break;
        }
        switch (rd2.getCheckedRadioButtonId()) {
            case R.id.rd_low_2:
                mInfo.priority2 = Priority.LOW;
                break;
            case R.id.rd_normal_2:
                mInfo.priority2 = Priority.NORMAL;
                break;
            case R.id.rd_high_2:
                mInfo.priority2 = Priority.HIGH;
                break;
        }
        dismiss();
    }
}
