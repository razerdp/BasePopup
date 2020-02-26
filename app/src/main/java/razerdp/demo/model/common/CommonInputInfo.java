package razerdp.demo.model.common;

import android.view.View;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.popup.PopupInput;
import razerdp.demo.popup.options.PopupInputOption;

/**
 * Created by 大灯泡 on 2019/9/22.
 */
public class CommonInputInfo extends DemoCommonUsageInfo {

    public boolean adjust = true;
    public boolean autoOpen = true;
    public int alignMode = BasePopupWindow.FLAG_KEYBOARD_ALIGN_TO_ROOT | BasePopupWindow.FLAG_KEYBOARD_ANIMATE_ALIGN;


    public CommonInputInfo() {
        title = "输入法适配";
    }

    PopupInput mPopupInput;
    PopupInputOption mPopupInputOption;

    @Override
    public void toShow(View v) {
        if (mPopupInput == null) {
            mPopupInput = new PopupInput(v.getContext());
        }
        mPopupInput.setAdjustInputMethod(adjust)
                .setAutoShowInputMethod(mPopupInput.findViewById(R.id.ed_input), autoOpen)
                .setAdjustInputMode(R.id.ed_input, alignMode)
                .showPopupWindow();
    }

    @Override
    public void toOption(View v) {
        if (mPopupInputOption == null) {
            mPopupInputOption = new PopupInputOption(v.getContext());
            mPopupInputOption.setInfo(this);
        }
        mPopupInputOption.showPopupWindow();

    }
}
