package razerdp.demo.model.common;

import android.view.View;

import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.ui.ActivityLauncher;
import razerdp.demo.ui.ScreenRotateActivity;
import razerdp.demo.utils.UIHelper;

/**
 * Created by 大灯泡 on 2020/05/17
 * <p>
 * Description：屏幕旋转
 */
public class ScreenRotateActivityInfo extends DemoCommonUsageInfo {

    public ScreenRotateActivityInfo() {
        title = "屏幕旋转";
    }

    @Override
    public void toShow(View v) {
        ActivityLauncher.start(v.getContext(), ScreenRotateActivity.class);
    }

    @Override
    public void toOption(View v) {
        UIHelper.toast("这里啥都没有哦");
    }
}
