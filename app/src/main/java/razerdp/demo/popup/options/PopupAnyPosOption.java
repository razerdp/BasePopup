package razerdp.demo.popup.options;

import android.content.Context;

import androidx.appcompat.widget.AppCompatCheckBox;
import razerdp.basepopup.R;
import razerdp.demo.model.common.CommonAnyPosInfo;
import razerdp.demo.widget.DPTextView;

/**
 * Created by 大灯泡 on 2019/9/20.
 *
 * @see CommonAnyPosInfo
 */
public class PopupAnyPosOption extends BaseOptionPopup<CommonAnyPosInfo> {
    AppCompatCheckBox mCheckOutSideTouch;
    AppCompatCheckBox mCheckBlur;
    DPTextView mTvGo;

    public PopupAnyPosOption(Context context) {
        super(context);
        setContentView(R.layout.popup_option_anypos);
    }



    void ok() {
        mInfo.outSideTouchable = mCheckOutSideTouch.isChecked();
        mInfo.blur = mCheckBlur.isChecked();
        dismiss();
    }
}
