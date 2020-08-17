package razerdp.demo.model.common;

import android.view.View;

import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.ui.ActivityLauncher;
import razerdp.demo.ui.dialog.DialogActivity;
import razerdp.demo.ui.fullscreen.FullScreenActivity;
import razerdp.demo.utils.UIHelper;

/**
 * Created by 大灯泡 on 2020/08/17
 * <p>
 * Description：dialog activity
 */
public class CommonDialogActivityInfo extends DemoCommonUsageInfo {

    public CommonDialogActivityInfo() {
        title = "Dialog Activity";
    }

    @Override
    public void toShow(View v) {
        ActivityLauncher.start(v.getContext(), DialogActivity.class);
    }

    @Override
    public void toOption(View v) {
        UIHelper.toast("这里啥都没有哦");
    }
}
