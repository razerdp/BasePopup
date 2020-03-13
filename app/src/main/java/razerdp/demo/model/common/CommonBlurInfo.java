package razerdp.demo.model.common;

import android.view.View;

import razerdp.blur.PopupBlurOption;
import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.popup.options.PopupBlurDemoOption;

/**
 * Created by 大灯泡 on 2020/3/12.
 * 模糊相关
 */
public class CommonBlurInfo extends DemoCommonUsageInfo {
    public float scaled = 0.15f;
    public float blurRadius = 10;
    public boolean blurEnable = true;
    public boolean blurAnchorView = false;
    public int blurInTime = 500;
    public int blurOutTime = 500;

    PopupBlurDemoOption mPopupBlurDemoOption;
    DemoPopup mDemoPopup;

    public CommonBlurInfo() {
        title = "背景模糊";
    }

    @Override
    public void toShow(View v) {
        if (mDemoPopup == null) {
            mDemoPopup = new DemoPopup(v.getContext()).setText("背景模糊");
        }

        if (blurEnable) {
            if (blurAnchorView) {
                PopupBlurOption blurOption = new PopupBlurOption();
                blurOption.setBlurInDuration(blurInTime);
                blurOption.setBlurOutDuration(blurOutTime);
                blurOption.setBlurRadius(blurRadius);
                blurOption.setBlurPreScaleRatio(scaled);
                blurOption.setBlurView(v);
                mDemoPopup.setBlurOption(blurOption);
            } else {
                mDemoPopup.setBlurBackgroundEnable(true, blurOption -> {
                    blurOption.setBlurInDuration(blurInTime);
                    blurOption.setBlurOutDuration(blurOutTime);
                    blurOption.setBlurRadius(blurRadius);
                    blurOption.setBlurPreScaleRatio(scaled);
                });
            }
        } else {
            mDemoPopup.setBlurBackgroundEnable(false);
            mDemoPopup.setBlurOption(null);
        }
        mDemoPopup.showPopupWindow();
    }

    @Override
    public void toOption(View v) {
        if (mPopupBlurDemoOption == null) {
            mPopupBlurDemoOption = new PopupBlurDemoOption(v.getContext());
            mPopupBlurDemoOption.setInfo(this);
        }
        mPopupBlurDemoOption.showPopupWindow(v);
    }
}
