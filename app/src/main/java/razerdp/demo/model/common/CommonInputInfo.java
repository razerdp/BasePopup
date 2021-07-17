package razerdp.demo.model.common;

import android.view.Gravity;
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
    public int keyboardGravity = Gravity.BOTTOM;


    public CommonInputInfo() {
        title = "输入法适配";
        name = "PopupInput";
        javaUrl = "https://github.com/razerdp/BasePopup/blob/master/app/src/main/java/razerdp/demo/popup/PopupInput.java";
        resUrl = "https://github.com/razerdp/BasePopup/blob/master/app/src/main/res/layout/popup_input.xml";

    }

    PopupInput mPopupInput;
    PopupInputOption mPopupInputOption;

    @Override
    public void toShow(View v) {
        if (mPopupInput == null) {
            mPopupInput = new PopupInput(v.getContext());
        }
        mPopupInput.setKeyboardAdaptive(adjust)
                .setAutoShowKeyboard(mPopupInput.findViewById(R.id.ed_input), autoOpen)
                .setKeyboardGravity(keyboardGravity)
                .setKeyboardAdaptionMode(R.id.ed_input, alignMode)
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
