package razerdp.demo.model.lifecycle;

import android.view.View;

import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.ui.ActivityLauncher;
import razerdp.demo.ui.lifecycle.ShowOnCreateActivity;

/**
 * Created by 大灯泡 on 2020/2/3.
 */
public class ShowOnCreateInfo extends DemoCommonUsageInfo {
    public ShowOnCreateInfo() {
        title = "在activity#onCreate中弹窗";
    }

    @Override
    public void toShow(View v) {
        ActivityLauncher.start(v.getContext(), ShowOnCreateActivity.class);
    }

    @Override
    public void toOption(View v) {

    }
}
