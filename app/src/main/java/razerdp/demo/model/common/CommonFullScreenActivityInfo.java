package razerdp.demo.model.common;

import android.view.View;

import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.ui.ActivityLauncher;
import razerdp.demo.ui.fullscreen.FullScreenActivity;
import razerdp.demo.utils.UIHelper;

/**
 * Created by 大灯泡 on 2020/05/17
 * <p>
 * Description：全屏
 */
public class CommonFullScreenActivityInfo extends DemoCommonUsageInfo {

    public CommonFullScreenActivityInfo() {
        title = "全屏Activity";
    }

    @Override
    public void toShow(View v) {
        ActivityLauncher.start(v.getContext(), FullScreenActivity.class);
    }

    @Override
    public void toOption(View v) {
        UIHelper.toast("这里啥都没有哦");
    }
}
