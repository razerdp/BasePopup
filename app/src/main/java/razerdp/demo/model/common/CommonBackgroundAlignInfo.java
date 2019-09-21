package razerdp.demo.model.common;

import android.view.Gravity;
import android.view.View;

import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.popup.options.PopupAlignBackgroundOption;
import razerdp.util.SimpleAnimationUtils;

/**
 * Created by 大灯泡 on 2019/9/21.
 */
public class CommonBackgroundAlignInfo extends DemoCommonUsageInfo {
    DemoPopup mDemoPopup;
    PopupAlignBackgroundOption mPopupAlignBackgroundOption;

    public boolean align;
    public boolean blur;
    public int alignGravity;

    public CommonBackgroundAlignInfo() {
        title = "背景蒙层控制";
    }

    @Override
    public void toShow(View v) {
        if (mDemoPopup == null) {
            mDemoPopup = new DemoPopup(v.getContext()).setText("背景蒙层控制");
            mDemoPopup.setShowAnimation(SimpleAnimationUtils.getTranslateVerticalAnimation(1f, 0f, 500))
                    .setDismissAnimation(SimpleAnimationUtils.getTranslateVerticalAnimation(0f, 1f, 500));
            mDemoPopup.setPopupGravity(Gravity.TOP);
        }
        mDemoPopup.setAlignBackground(align)
                .setAlignBackgroundGravity(alignGravity)
                .setBlurBackgroundEnable(blur)
                .showPopupWindow(v);


    }

    @Override
    public void toOption(View v) {
        if (mPopupAlignBackgroundOption == null) {
            mPopupAlignBackgroundOption = new PopupAlignBackgroundOption(v.getContext());
            mPopupAlignBackgroundOption.setInfo(this);
        }
        mPopupAlignBackgroundOption.showPopupWindow();

    }
}
