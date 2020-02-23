package razerdp.demo.model.common;

import android.view.View;

import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.popup.options.PopupControlOption;

/**
 * Created by 大灯泡 on 2020/2/23.
 */
public class CommonControllerInfo extends DemoCommonUsageInfo {
    public boolean backpressEnable = true;
    public boolean outSideTouchAble;
    public boolean outSideDissmiss = true;
    public boolean blurEnable;
    public boolean showNewOne;

    DemoPopup mDemoPopup;
    PopupControlOption mPopupControlOption;

    public CommonControllerInfo() {
        title = "控制展示";
    }

    @Override
    public void toShow(View v) {
        DemoPopup popup;
        if (!showNewOne) {
            if (mDemoPopup == null) {
                mDemoPopup = new DemoPopup(v.getContext());
            }
            popup = mDemoPopup;
        } else {
            popup = new DemoPopup(v.getContext());
        }

        popup.setText("PopupWindow控制Demo\n(点我dismiss)");
        popup.setOffsetX((int) (Math.random() * 300))
                .setOffsetY((int) (Math.random() * 300));
        popup.getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });

        popup.setBackPressEnable(backpressEnable)
                .setOutSideTouchable(outSideTouchAble)
                .setOutSideDismiss(outSideDissmiss)
                .setBlurBackgroundEnable(blurEnable)
                .showPopupWindow();

    }

    @Override
    public void toOption(View v) {
        if (mPopupControlOption == null) {
            mPopupControlOption = new PopupControlOption(v.getContext());
            mPopupControlOption.setInfo(this);
        }
        mPopupControlOption.showPopupWindow();
    }
}
