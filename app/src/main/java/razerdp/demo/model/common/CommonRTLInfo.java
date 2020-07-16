package razerdp.demo.model.common;

import android.view.View;

import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.ui.ActivityLauncher;
import razerdp.demo.ui.rtl.RTLActivity;
import razerdp.demo.utils.UIHelper;

/**
 * Created by 大灯泡 on 2020/7/11.
 */
public class CommonRTLInfo extends DemoCommonUsageInfo {
    public CommonRTLInfo() {
        title = "RTL布局";
    }

    @Override
    public void toShow(View v) {
        ActivityLauncher.start(v.getContext(), RTLActivity.class);
    }

    @Override
    public void toOption(View v) {
        UIHelper.toast("这里啥都没有哦");
    }
}
