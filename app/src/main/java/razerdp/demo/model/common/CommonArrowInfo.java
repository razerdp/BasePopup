package razerdp.demo.model.common;

import android.view.Gravity;
import android.view.View;

import razerdp.basepopup.BasePopupWindow;
import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.popup.PopupArrow;
import razerdp.demo.popup.options.PopupArrowOption;

/**
 * Created by 大灯泡 on 2020/5/6.
 */
public class CommonArrowInfo extends DemoCommonUsageInfo {
    public int gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
    public boolean blur;
    public BasePopupWindow.GravityMode gravityMode = BasePopupWindow.GravityMode.RELATIVE_TO_ANCHOR;

    PopupArrow mPopupArrow;
    PopupArrowOption mPopupArrowOption;

    public CommonArrowInfo() {
        title = "带箭头的Popup";
    }

    @Override
    public void toShow(View v) {
        if (mPopupArrow == null) {
            mPopupArrow = new PopupArrow(v.getContext());
        }
        mPopupArrow.setBlurBackgroundEnable(blur);
        mPopupArrow.setPopupGravity(gravityMode, gravity);
        mPopupArrow.showPopupWindow(v);
    }

    @Override
    public void toOption(View v) {
        if (mPopupArrowOption == null) {
            mPopupArrowOption = new PopupArrowOption(v.getContext());
            mPopupArrowOption.setInfo(this);
        }
        mPopupArrowOption.showPopupWindow(v);
    }
}
