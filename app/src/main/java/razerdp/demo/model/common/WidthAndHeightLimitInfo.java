package razerdp.demo.model.common;

import android.view.View;

import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.ui.ActivityLauncher;
import razerdp.demo.ui.SizeLimitActivity;
import razerdp.demo.utils.UIHelper;

/**
 * Created by 大灯泡 on 2021/8/6.
 */
public class WidthAndHeightLimitInfo extends DemoCommonUsageInfo {
    public WidthAndHeightLimitInfo() {
        title = "测试宽高限制";
        name = "PopupWidthHeightLimit";
        javaUrl = "https://github.com/razerdp/BasePopup/blob/master/app/src/main/java/razerdp/demo/popup/PopupWidthHeightLimit.java";
        resUrl = "https://github.com/razerdp/BasePopup/blob/master/app/src/main/res/layout/popup_width_height_limit.xml";
    }

    @Override
    public void toShow(View v) {
        ActivityLauncher.start(v.getContext(), SizeLimitActivity.class);
    }


    @Override
    public void toOption(View v) {
        UIHelper.toast("这里啥都没有哦");
    }
}
