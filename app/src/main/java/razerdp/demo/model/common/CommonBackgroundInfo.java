package razerdp.demo.model.common;

import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;

import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.popup.options.PopupBackgroundOption;

/**
 * Created by 大灯泡 on 2019/9/20.
 */
public class CommonBackgroundInfo extends DemoCommonUsageInfo {
    public Drawable background;
    public boolean blur;

    DemoPopup mDemoPopup;
    PopupBackgroundOption mPopupBackgroundOption;

    public CommonBackgroundInfo() {
        title = "背景控制";
    }

    @Override
    public void toShow(View v) {
        if (mDemoPopup == null) {
            mDemoPopup = new DemoPopup(v.getContext()).setText("背景控制");
        }
        mDemoPopup.setBlurBackgroundEnable(blur)
                .setBackground(background)
                .setPopupGravity(Gravity.CENTER)
                .showPopupWindow();

    }

    @Override
    public void toOption(View v) {
        if (mPopupBackgroundOption == null) {
            mPopupBackgroundOption = new PopupBackgroundOption(v.getContext());
            mPopupBackgroundOption.setInfo(this);
        }
        mPopupBackgroundOption.showPopupWindow();

    }
}
