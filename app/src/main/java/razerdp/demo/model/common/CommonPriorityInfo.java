package razerdp.demo.model.common;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import razerdp.basepopup.BasePopupWindow;
import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.popup.options.PopupPriorityOption;

/**
 * Created by 大灯泡 on 2021/7/1.
 */
public class CommonPriorityInfo extends DemoCommonUsageInfo {
    public BasePopupWindow.Priority priority1 = BasePopupWindow.Priority.NORMAL;
    public BasePopupWindow.Priority priority2 = BasePopupWindow.Priority.NORMAL;

    DemoPopup mDemoPopup1;
    DemoPopup mDemoPopup2;

    PopupPriorityOption popupPriorityOption;

    public CommonPriorityInfo() {
        title = "优先级控制";
        name = "DemoPopup";
        javaUrl = "https://github.com/razerdp/BasePopup/blob/master/app/src/main/java/razerdp/demo/popup/DemoPopup.java";
        resUrl = "https://github.com/razerdp/BasePopup/blob/master/app/src/main/res/layout/popup_demo.xml";
    }

    @Override
    public void toShow(View v) {
        if (mDemoPopup1 == null) {
            mDemoPopup1 = new DemoPopup(v.getContext());
            mDemoPopup1.getContentView().setBackgroundColor(Color.RED);
            mDemoPopup1.getTextView().setTextColor(Color.WHITE);
            mDemoPopup1.setText("点我展示弹窗2");
            mDemoPopup1.getContentView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDemo2(v.getContext());
                }
            });
        }
        mDemoPopup1.setPriority(priority1).showPopupWindow();
    }

    void showDemo2(Context context) {
        if (mDemoPopup2 == null) {
            mDemoPopup2 = new DemoPopup(context);
            mDemoPopup2.getTextView().setTextColor(Color.WHITE);
            mDemoPopup2.setText("弹窗2");
            mDemoPopup2.getContentView().setBackgroundColor(Color.BLUE);
        }
        mDemoPopup2.setPriority(priority2).setOffsetX(100).setOffsetY(100).showPopupWindow();
    }

    @Override
    public void toOption(View v) {
        if (popupPriorityOption == null) {
            popupPriorityOption = new PopupPriorityOption(v.getContext());
            popupPriorityOption.setInfo(this);
        }
        popupPriorityOption.showPopupWindow();
    }
}
