package razerdp.demo.model.common;

import android.view.View;

import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.ui.ActivityLauncher;
import razerdp.demo.ui.AutoMirrorActivity;
import razerdp.demo.utils.UIHelper;

/**
 * Created by 大灯泡 on 2021/06/18
 * <p>
 * Description：自动镜像
 */
public class CommonAutoMirrorActivityInfo extends DemoCommonUsageInfo {

    public CommonAutoMirrorActivityInfo() {
        title = "自动镜像定位";
        sourceVisible = false;
    }

    @Override
    public void toShow(View v) {
        ActivityLauncher.start(v.getContext(), AutoMirrorActivity.class);
    }

    @Override
    public void toOption(View v) {
        UIHelper.toast("这里啥都没有哦");
    }
}
