package razerdp.demo.model.common;

import android.view.View;

import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.ui.ActivityLauncher;
import razerdp.demo.ui.updatetest.UpdateTestActivity;
import razerdp.demo.utils.UIHelper;

/**
 * Created by 大灯泡 on 2020/12/29.
 */
public class CommonUpdateInfo extends DemoCommonUsageInfo {
    public CommonUpdateInfo() {
        title = "Update()方法测试";
        name = "PopupUpdateTest";
        javaUrl = "https://github.com/razerdp/BasePopup/blob/master/app/src/main/java/razerdp/demo/popup/PopupUpdateTest.java";
        resUrl = "https://github.com/razerdp/BasePopup/blob/master/app/src/main/res/layout/popup_update_test.xml";
    }

    @Override
    public void toShow(View v) {
        ActivityLauncher.start(v.getContext(), UpdateTestActivity.class);
    }

    @Override
    public void toOption(View v) {
        UIHelper.toast("这里啥都没有哦");
    }
}
